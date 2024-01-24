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
    int seq = 0;


    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public Task getTaskByID(int id) {
        return tasks.get(id);
    }

    public void createTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
    }

    public void updateTask(Task task) {
        if(tasks.containsValue(task)) {
            tasks.put(task.getId(), task);
        }
    }

    public void removeTaskByID(int id) {
        tasks.remove(id);
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public void removeAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    public Epic getEpicIDByID(int id) {
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

    public void removeEpicByID(int id) {
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
        ArrayList<Epic> allEpics = (ArrayList<Epic>) epics.values();
        for (Epic epic : allEpics) {
            epic.getSubTasksID().clear();
        }
    }

    public SubTask getSubTaskByID(int id) {
        return subTasks.get(id);
    }

    public void createSubTask(SubTask subTask) {
        Epic epic = epics.get(subTask.getEpicID());
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

    public void removeSubTaskByID(int id) {
        SubTask subTask = subTasks.get(id);
        Epic epic = epics.get(subTask.getEpicID());
        epic.getSubTasksID().remove(id);
        subTasks.remove(id);
        if (epic.getSubTasksID() != null) {
            calculateStatus(subTasks.get(id));
        }
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
        Epic epic = epics.get(subTask.getEpicID());
        ArrayList<SubTask> tasks = new ArrayList<>();
        boolean flagStatusNew = false;
        boolean flagStatusDone = false;
        for (Integer id : epic.getSubTasksID()) {
            tasks.add(subTasks.get(id));
        }

        if (epic.getSubTasksID() != null) {
            for (SubTask task : tasks) {
                if (task.getStatus() == TaskStatus.NEW) {
                    flagStatusNew = true;
                } else {
                    flagStatusNew = false;
                    break;
                }
            }

            for (SubTask task : tasks) {
                if (task.getStatus() == TaskStatus.DONE) {
                    flagStatusDone = true;
                } else {
                    flagStatusDone = false;
                    break;
                }
            }

            if (flagStatusNew) {
                epic.setStatus(TaskStatus.NEW);
            } else if (flagStatusDone) {
                epic.setStatus(TaskStatus.DONE);
            } else epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

}
