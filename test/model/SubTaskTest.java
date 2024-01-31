package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {
    SubTask subTask1 = new SubTask("Задача 1", "...", 1);
    SubTask subTask2 = new SubTask("Задача 2", "...", 1);

    @Test
    void shouldBeEqualsSubTask() {
        subTask1.setId(1);
        subTask2.setId(1);

        assertEquals(subTask1, subTask2, "Подзадачи должны быть равны");
    }

}