package service;

import model.Task;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
HistoryManager historyManager = Managers.getDefaultHistory();
    Task task = new Task("Переезд", "Нужно собрать вещи");
    Task task2 = new Task("Спорт", "Записаться в зал");
    @Test
    void add_shouldAddTasksToHistoryList() {
        historyManager.add(task);
        Task thisTask = task;

        assertEquals(List.of(thisTask), historyManager.getHistory());
    }

    @Test
    void remove_shouldRemoveATaskFromHistory() {
        historyManager.add(task);
        historyManager.add(task2);
        Task thisTask1 = task2;
        historyManager.remove(1);

        assertEquals(List.of(thisTask1), historyManager.getHistory());
    }

}