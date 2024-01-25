package service;

import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private int seq = 0;


    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public void createTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
    }

    public void updateTask(Task task) {
        if (tasks.containsValue(task)) {
            tasks.put(task.getId(), task);
        }
    }

    public void removeTaskById(int id) {
        tasks.remove(id);
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public void removeAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public void createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
    }

    public void updateEpic(Epic epic) {
        if (epics.containsValue(epic)) {
            Epic saved = epics.get(epic.getId());
            saved.setTitle(epic.getTitle());
            saved.setDescription(epic.getDescription());
            epics.put(epic.getId(), saved);
        }
    }

    public void removeEpicById(int id) {
        Epic epic = epics.get(id);

        for (Integer subTaskId : epic.getSubTasksID()) {
            subTasks.remove(subTaskId);
        }
        epics.remove(id);
    }

    public ArrayList<SubTask> getAllSubTask() {
        return new ArrayList<>(subTasks.values());
    }

    public void removeAllSubTasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubTasksID().clear();
        }
    }

    public SubTask getSubTaskById(int id) {
        return subTasks.get(id);
    }

    public void createSubTask(SubTask subTask) {
        Epic epic = epics.get(subTask.getEpicId());
        subTask.setId(generateId());
        subTasks.put(subTask.getId(), subTask);
        epic.getSubTasksID().add(subTask.getId());
        calculateStatus(subTask);
    }

    public void updateSubTask(SubTask subTask) {
        if (subTasks.containsValue(subTask)) {
            subTasks.put(subTask.getId(), subTask);
            calculateStatus(subTask);
        }
    }

    public void removeSubTaskById(int id) {
        SubTask subTask = subTasks.get(id);
        Epic epic = epics.get(subTask.getEpicId());
        epic.getSubTasksID().remove(subTask.getId());
        subTasks.remove(id);
        calculateStatus(subTasks.get(id));
    }

    public ArrayList<SubTask> getSubTaskByEpic(int epicID) {
        Epic epic = epics.get(epicID);
        ArrayList<SubTask> tasks = new ArrayList<>();
        for (int id : epic.getSubTasksID()) {
            tasks.add(subTasks.get(id));
        }
        return tasks;
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
}
