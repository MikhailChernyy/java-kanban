package service;

import model.Task;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
HistoryManager historyManager = Managers.getDefaultHistory();
    Task task = new Task("Переезд", "Нужно собрать вещи");
    @Test
    void shouldBeSaveThePreviousTaskVersion() {
        historyManager.addTaskInHistory(task);
        task.setTitle("Обновление");
        historyManager.addTaskInHistory(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(history.get(0).getTitle(), "Переезд", "Предидущая версия не сохраняется.");
        assertEquals(history.get(1).getTitle(), "Обновление");
    }

}