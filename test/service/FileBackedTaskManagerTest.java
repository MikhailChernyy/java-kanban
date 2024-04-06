package service;

import model.Epic;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {
    FileBackedTaskManager manager = new FileBackedTaskManager("resources/backUp.csv");
    Task task1 = new Task(1, "Переезд", "Нужно собрать вещи", TaskStatus.NEW);
    Task task2 = new Task(2, "Починить машину", "Нужно заехать в автосервис", TaskStatus.NEW);
    Epic epic1 = new Epic("Эпик 1", "///");

    @Test
    void save_loadFromFile_shouldSaveAndLoadEmptyKindOfTasks() {
        manager.save();
        manager.loadFromFile(new File("resources/backUp.csv"));

        assertEquals(Collections.EMPTY_MAP, manager.getTasks());
        assertEquals(Collections.EMPTY_MAP, manager.getEpics());
        assertEquals(Collections.EMPTY_MAP, manager.getSubTasks());
    }

    @Test
    void save_loadFromFile_shouldSaveAndLoadEmptyHistory() {
        manager.save();
        manager.loadFromFile(new File("resources/backUp.csv"));

        assertEquals(Collections.EMPTY_LIST, manager.getHistory());
    }
}