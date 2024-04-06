package service;

import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private final InMemoryHistoryManager historyManager = (InMemoryHistoryManager) Managers.getDefaultHistory();
    private int seq = 0;
    private final Comparator<Task> comparator = Comparator.comparing(Task::getStartTime);
    private final Set<Task> prioritizedTasks = new TreeSet<>(comparator);


    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void removeAllTasks() {
        tasks.clear();
        prioritizedTasks.clear();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return tasks.get(id);
    }

    @Override
    public Task createTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        addTaskToPrioritizedList(task);
        return task;
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsValue(task)) {
            tasks.put(task.getId(), task);
            addTaskToPrioritizedList(task);
        }
    }

    @Override
    public void removeTaskById(int id) {
        tasks.remove(id);
        prioritizedTasks.removeIf(task -> task.getId() == id);
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void removeAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public Epic getEpicById(int id) {
        Task task = epics.get(id);
        historyManager.add(task);
        return epics.get(id);
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsValue(epic)) {
            Epic saved = epics.get(epic.getId());
            saved.setTitle(epic.getTitle());
            saved.setDescription(epic.getDescription());
            epics.put(epic.getId(), saved);
            calculateEpicTime(epic);
        }
    }

    @Override
    public void removeEpicById(int id) {
        Epic epic = epics.get(id);

        epic.getSubTasksID()
                .forEach(subTasks::remove);
        epics.remove(id);
    }

    @Override
    public List<SubTask> getAllSubTask() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void removeAllSubTasks() {
        subTasks.clear();
        epics.values()
                .forEach(epic -> epic.getSubTasksID().clear());
    }

    @Override
    public SubTask getSubTaskById(int id) {
        Task task = subTasks.get(id);
        historyManager.add(task);
        return subTasks.get(id);
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        Epic epic = epics.get(subTask.getEpicId());
        subTask.setId(generateId());
        subTasks.put(subTask.getId(), subTask);
        addTaskToPrioritizedList(subTask);
        epic.getSubTasksID().add(subTask.getId());
        calculateStatus(subTask);
        calculateEpicTime(epic);
        return subTask;
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (subTasks.containsValue(subTask)) {
            subTasks.put(subTask.getId(), subTask);
            calculateStatus(subTask);
        }
    }

    @Override
    public void removeSubTaskById(int id) {
        SubTask subTask = subTasks.get(id);
        Epic epic = epics.get(subTask.getEpicId());
        epic.getSubTasksID().remove(subTask.getId());
        subTasks.remove(id);
        calculateStatus(subTasks.get(id));
        calculateEpicTime(epic);
        prioritizedTasks.remove(subTask);
    }

    @Override
    public List<SubTask> getSubTaskByEpic(int epicID) {
        Epic epic = epics.get(epicID);
        ArrayList<SubTask> tasks = new ArrayList<>();
        epic.getSubTasksID()
                .forEach(id -> tasks.add(subTasks.get(id)));

        return tasks;
    }

  public List<Task> getHistory() {
        return historyManager.getHistory();
  }

    public InMemoryHistoryManager getHistoryManager() {
        return historyManager;
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    private int generateId() {
        return ++seq;
    }

    private void calculateStatus(SubTask subTask) {
        Epic epic = epics.get(subTask.getEpicId());
        int countStatusNew = 0;
        int countStatusDone = 0;
        int countSubTasks = 0;


        for (Integer id : epic.getSubTasksID()) {
            SubTask task = subTasks.get(id);
            countSubTasks++;
            if (task.getStatus() == TaskStatus.NEW) {
                countStatusNew++;
            } else if (task.getStatus() == TaskStatus.DONE) {
                countStatusDone++;
            }
        }
        if (countStatusNew == countSubTasks) {
            epic.setStatus(TaskStatus.NEW);
        } else if (countStatusDone == countSubTasks) {
            epic.setStatus(TaskStatus.DONE);
        } else epic.setStatus(TaskStatus.IN_PROGRESS);
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    private void calculateEpicTime(Epic epic) {
        List<SubTask> subtasks = getSubTaskByEpic(epic.getId());
        LocalDateTime startTime = subtasks.get(0).getStartTime();
        LocalDateTime endTime = subtasks.get(0).getEndTime();

        for (SubTask subtask : subtasks) {
            if (subtask.getStartTime().isBefore(startTime)) {
                startTime = subtask.getStartTime();
            } else if (subtask.getEndTime().isAfter(endTime)) {
                endTime = subtask.getEndTime();
            }
        }
        epic.setStartTime(startTime);
        epic.setEndTime(endTime);
        Duration duration = Duration.between(startTime, endTime);
        epic.setDuration(duration);
    }

    public void addTaskToPrioritizedList(Task task) {
        boolean isValidated = validation(task);
        if (!isValidated) {
            prioritizedTasks.add(task);
        } else {
            System.out.println("Задача не прошла валидацию");
        }
    }

    private boolean validation(Task task) {
        boolean isOverlapping = false;
        LocalDateTime startOfTask = task.getStartTime();
        LocalDateTime endOfTask = task.getEndTime();
        for (Task taskValue : prioritizedTasks) {
            if (taskValue.getStartTime() == null) {
                continue;
            }
            LocalDateTime startTime = taskValue.getStartTime();
            LocalDateTime endTime = taskValue.getEndTime();
            boolean isCovering = startTime.isBefore(startOfTask) && endTime.isAfter(endOfTask);
            boolean isOverlappingByEnd = startTime.isBefore(startOfTask) && endTime.isAfter(startOfTask);
            boolean isOverlappingByStart = startTime.isBefore(endOfTask) && endTime.isAfter(endOfTask);
            boolean isWithin = startTime.isAfter(startOfTask) && endTime.isBefore(endOfTask);
            isOverlapping = isCovering || isOverlappingByEnd || isOverlappingByStart || isWithin;
        }
        return isOverlapping;
    }
}
