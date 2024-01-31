package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    Task task1 = new Task("Задача 1", "...");
    Task task2 = new Task("Задача 2", "...");

    @Test
    void shouldBeEqualsTask() {
        task1.setId(1);
        task2.setId(1);
        assertEquals(task1, task2, "Задачи должны быть равны");
    }
}