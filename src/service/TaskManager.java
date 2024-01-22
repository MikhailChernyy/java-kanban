package service;

import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, SubTask> subTasks = new HashMap<>();
    int seq = 0;

    private int generateId() {
        return ++seq;
    }
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public Task getTaskAnID(int id) {
        return tasks.get(id);
    }

    public void createTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
    }

    public void updateTask(Task task, Task newTask) {
        newTask.setId(task.getId());
        tasks.put(newTask.getId(), newTask);
    }

    public void removeTaskAnID(int id) {
        tasks.remove(id);
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public void removeAllEpics() {
        epics.clear();
    }

    public Epic getEpicAnID(int id) {
        return epics.get(id);
    }

    public void createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
    }

    public void updateEpic(Epic epic, Epic newEpic) {
        Epic saved = epics.get(epic.getId());
        saved.setTitle(newEpic.getTitle());
        saved.setDescription(newEpic.getDescription());
        saved.setSubTasks(epic.getSubTasks());
        epics.put(epic.getId(), saved);
    }

    public void removeEpicAnID(int id) {
        epics.remove(id);
    }

    public ArrayList<SubTask> getAllSubTask() {
        return new ArrayList<>(subTasks.values());
    }

    public void removeAllSubTasks() {
        subTasks.clear();
    }

    public SubTask getSubTaskAnID(int id) {
        return subTasks.get(id);
    }

    public void createSubTask(Epic epic, SubTask subTask) {
        subTask.setId(generateId());
        epic.getSubTasks().add(subTask);
        subTask.setEpic(epic);
        subTasks.put(subTask.getId(), subTask);
    }

    public void updateSubTask(SubTask subTask, SubTask newSubTask) {
        newSubTask.setId(subTask.getId());
        newSubTask.setEpic(subTask.getEpic());
        subTasks.put(newSubTask.getId(), newSubTask);

        if (newSubTask.getStatus() == TaskStatus.NEW) {
            newSubTask.getEpic().setStatus(TaskStatus.NEW);
        } else if (newSubTask.getStatus() == TaskStatus.DONE) {
            newSubTask.getEpic().setStatus(TaskStatus.DONE);
        } else newSubTask.getEpic().setStatus(TaskStatus.IN_PROGRESS);
    }

    public void removeSubTaskAnID(int id) {
        subTasks.remove(id);
    }

    public ArrayList<SubTask> getSubTaskAnEpic(Epic epic) {
        return new ArrayList<>(epic.getSubTasks());
    }
}
