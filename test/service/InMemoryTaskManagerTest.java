package service;

import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    TaskManager taskManager = Managers.getDefaultTaskManager();
    Task task1 = new Task("Переезд", "Нужно собрать вещи");
    Task task2 = new Task("Починить машину", "Нужно заехать в автосервис");
    Epic epic1 = new Epic("Эпик 1", "///");
    Epic epic2 = new Epic("Эпик 2", "***");
    SubTask subTask1 = new SubTask("Задача 1", "...", 3);
    SubTask subTask2 = new SubTask("Задача 2", ",,,", 3);
    SubTask subTask3 = new SubTask("Задача 1", "Нужно ...", 4);

    @Test
    void shouldBeAddTaskAndGet() {
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);
        taskManager.createSubTask(subTask3);

        assertEquals(taskManager.getTaskById(1), task1);
        assertEquals(taskManager.getEpicById(3), epic1);
        assertEquals(taskManager.getSubTaskById(6), subTask2);
    }

    @Test
    void shouldBeTaskIdNotConflicted() {
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        task2.setId(1);
        assertEquals(task2, task1);

    }
}