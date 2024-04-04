package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.List;

public interface TaskManager {
    List<Task> getAllTasks();

    void removeAllTasks();

    Task getTaskById(int id);

    Task createTask(Task task);

    void updateTask(Task task);

    void removeTaskById(int id);

    List<Epic> getAllEpics();

    void removeAllEpics();

    Epic getEpicById(int id);

    Epic createEpic(Epic epic);

    void updateEpic(Epic epic);

    void removeEpicById(int id);

    List<SubTask> getAllSubTask();

    void removeAllSubTasks();

    SubTask getSubTaskById(int id);

    SubTask createSubTask(SubTask subTask);

    void updateSubTask(SubTask subTask);

    void removeSubTaskById(int id);

    List<SubTask> getSubTaskByEpic(int epicID);

    List<Task> getPrioritizedTasks();
}
