package service;


import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

abstract class ManagersTest<T extends TaskManager> {
    T manager;
    Duration duration = Duration.ofMinutes(30);
    LocalDateTime startTime = LocalDateTime.now();
    Task task = new Task(1, "Переезд", "Нужно собрать вещи", duration, startTime);
    SubTask subTask = new SubTask(3, "Подзадача","///", duration, startTime, 2);
    ArrayList<Integer> subTasksID = (ArrayList<Integer>) List.of(subTask.getId());
    Epic epic = new Epic(subTasksID,1, "Эпик 1", "///", duration, startTime );
    @Test
    void addTask_shouldCreateATask() {
        manager.createTask(task);
        List<Task> listOfTasks = new ArrayList<>(manager.getAllTasks());

        assertNotNull(task.getStatus());
        assertEquals(TaskStatus.NEW, task.getStatus());
        assertEquals(List.of(task), listOfTasks);
    }

    @Test
    void addTask_shouldNotCreateATaskIfItsEmpty() {
        Task task = manager.createTask(null);;
        assertNull(task);
    }

    @Test
    void getTaskById_shouldReturnCreatedTaskById() {
        manager.createTask(task);
        Task task = manager.getTaskById(1);
        List<Task> listOfTasks = new ArrayList<>(manager.getAllTasks());

        assertNotNull(task.getStatus());
        assertEquals(1, task.getId());
        assertEquals(List.of(task), listOfTasks);
    }

    @Test
    void deleteTasks_shouldDeleteTasks() {
        manager.createTask(task);
        manager.removeAllTasks();

        assertEquals(Collections.EMPTY_MAP, manager.getAllTasks());
        assertEquals(Collections.EMPTY_LIST, manager.getPrioritizedTasks());
    }

    @Test
    void deleteTasks_shouldNotDeleteTasksIfListIsEmpty() {
        manager.createTask(null);
        manager.removeAllTasks();
        List<Task> listOfTask = manager.getAllTasks();
        List<Task> listOfTasks = manager.getPrioritizedTasks();

        assertEquals(0, listOfTask.size());
        assertEquals(0, listOfTasks.size());
    }

    @Test
    void deleteTaskById_shouldDeleteTaskById() {
        Task thisTask = manager.createTask(task);
        manager.removeTaskById(thisTask.getId());
        List<Task> mapOfTasks = manager.getAllTasks();
        List<Task> listOfTasks = manager.getPrioritizedTasks();

        assertEquals(0, mapOfTasks.size());
        assertEquals(0, listOfTasks.size());
    }

    @Test
    void deleteTaskById_shouldNotDeleteTaskIfPassedIdIsIncorrect() {
        manager.createTask(task);
        Task thisTask = task;
        manager.removeTaskById(99);
        List<Task> listOfTasks = new ArrayList<>(manager.getAllTasks());

        assertEquals(List.of(thisTask), listOfTasks);
    }

    @Test
    void getTasks_shouldReturnMapOfTasks() {
        manager.createTask(task);
        List<Task> mapOfTasks = manager.getAllTasks();

        assertNotNull(mapOfTasks);
        assertEquals(1, mapOfTasks.size());
    }

    @Test
    void updateTask_shouldUpdateTaskToStatusIN_PROGRESS() {
        Task thisTask = manager.createTask(task);
        task.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateTask(thisTask);

        assertEquals(TaskStatus.IN_PROGRESS, manager.getTaskById(thisTask.getId()).getStatus());
    }

    @Test
    void updateTask_shouldUpdateTaskToStatusDONE() {
        Task thisTask = manager.createTask(task);
        task.setStatus(TaskStatus.DONE);
        manager.updateTask(thisTask);

        assertEquals(TaskStatus.DONE, manager.getTaskById(thisTask.getId()).getStatus());
    }

    @Test
    void updateTask_shouldNotUpdateTaskIfItsEmpty() {
        Task thisTask = manager.createTask(task);
        manager.updateTask(null);

        assertEquals(task, manager.getTaskById(thisTask.getId()));
    }

    // тесты для Подзадач

    @Test
    void addSubtask_shouldCreateASubtask() {
        Epic thisEpic = manager.createEpic(epic);
        SubTask thisSubtask = manager.createSubTask(subTask);
        List<Task> listOfSubtasks = new ArrayList<>(manager.getAllSubTask());

        assertNotNull(thisSubtask.getStatus());
        assertEquals(TaskStatus.NEW, thisSubtask.getStatus());
        assertEquals(thisEpic.getId(), thisSubtask.getEpicId());
        assertEquals(List.of(thisSubtask), listOfSubtasks);
        assertEquals(List.of(thisSubtask.getId()), thisEpic.getSubTasksID());
    }

    @Test
    void addSubtask_shouldNotCreateASubtaskIfItsEmpty() {
        manager.createEpic(null);
        SubTask subtask = manager.createSubTask(null);
        List<SubTask> listOfSubtasks = new ArrayList<>(manager.getAllSubTask());

        assertNull(subtask);
        assertTrue(listOfSubtasks.isEmpty());
    }

    @Test
    void getSubtaskById_shouldReturnCreatedSubtaskById() {
        manager.createEpic(epic);
        SubTask thisSubtask = manager.createSubTask(subTask);
        List<SubTask> listOfSubtasks = new ArrayList<>(manager.getAllSubTask());

        assertNotNull(thisSubtask.getStatus());
        assertEquals(12, thisSubtask.getId());
        assertEquals(List.of(thisSubtask), listOfSubtasks);
    }

    @Test
    void getSubtaskById_shouldReturnNullIfCreatedSubtaskIsEmpty() {
        manager.createSubTask(null);
        SubTask subtask = manager.getSubTaskById(0);

        assertNull(subtask);
    }

    @Test
    void deleteSubtasks_shouldDeleteSubtasks() {
        manager.createSubTask(subTask);
        manager.removeAllSubTasks();

        assertEquals(Collections.EMPTY_MAP, manager.getAllSubTask());
        assertEquals(Collections.EMPTY_LIST, manager.getPrioritizedTasks());
    }

    @Test
    void deleteSubtasks_shouldNotDeleteSubtasksIfListIsEmpty() {
        manager.createSubTask(subTask);
        manager.removeAllSubTasks();
        List<SubTask> listOfSubtasks = manager.getAllSubTask();
        List<Task> listOfTasks = manager.getPrioritizedTasks();

        assertEquals(0, listOfSubtasks.size());
        assertEquals(0, listOfTasks.size());
    }

    @Test
    void deleteSubtaskById_shouldDeleteSubtaskById() {
        manager.createSubTask(subTask);
        List<SubTask> listOfSubtasks = manager.getAllSubTask();
        List<Task> listOfTasks = manager.getPrioritizedTasks();

        assertEquals(0, listOfSubtasks.size());
        assertEquals(0, listOfTasks.size());
    }

    @Test
    void deleteSubtaskById_shouldNotDeleteSubtaskIfPassedIdIsIncorrect() {
        manager.createEpic(epic);
        SubTask thisSubtask = manager.createSubTask(subTask);
        manager.removeSubTaskById(99);
        List<SubTask> listOfSubtasks = manager.getAllSubTask();

        assertEquals(List.of(thisSubtask), listOfSubtasks);
    }

    @Test
    void getSubtasks_shouldReturnMapOfSubtasks() {
        Epic thisEpic = manager.createEpic(epic);
        SubTask thisSubtask = manager.createSubTask(subTask);
        List<SubTask> listOfSubtasks = manager.getAllSubTask();
        List<Epic> listOfEpics = manager.getAllEpics();
        assertNotNull(listOfSubtasks);
        assertTrue(listOfSubtasks.contains(thisSubtask));
        assertTrue(listOfEpics.contains(thisEpic));
        assertEquals(1, listOfSubtasks.size());
    }

    @Test
    void getSubtasks_shouldReturnAnEmptyMapOfSubtasks() {
        assertTrue(manager.getAllSubTask().isEmpty());
    }

    @Test
    void updateSubtask_shouldUpdateSubtaskToStatusIN_PROGRESS() {
        manager.createEpic(epic);
        SubTask thisSubtask = manager.createSubTask(subTask);
        subTask.setStatus(TaskStatus.IN_PROGRESS);

        assertEquals(TaskStatus.IN_PROGRESS, manager.getSubTaskById(thisSubtask.getId()).getStatus());
    }

    @Test
    void updateSubtask_shouldUpdateSubtaskToStatusDONE() {
        manager.createEpic(epic);
        SubTask thisSubtask = manager.createSubTask(subTask);
        thisSubtask.setStatus(TaskStatus.DONE);

        assertEquals(TaskStatus.DONE, manager.getSubTaskById(thisSubtask.getId()).getStatus());
    }

    @Test
    void updateSubtask_shouldNotUpdateSubtaskIfItsEmpty() {
        manager.createEpic(epic);
        manager.createSubTask(subTask);
        manager.updateSubTask(null);

        assertEquals(subTask, manager.getSubTaskById(subTask.getId()));
    }

    // тесты для Эпиков

    @Test
    void addEpic_shouldCreateAnEpic() {
        manager.createEpic(epic);
        List<Task> listOfEpics = new ArrayList<>(manager.getAllEpics());

        assertNotNull(epic.getStatus());
        assertEquals(TaskStatus.NEW, epic.getStatus());
        assertEquals(List.of(epic), listOfEpics);
        assertEquals(Collections.EMPTY_LIST, epic.getSubTasksID());
    }

    @Test
    void addEpic_shouldNotCreateAnEpicIfItsEmpty() {
        Epic epic = manager.createEpic(null);

        assertNull(epic);
    }

    @Test
    void getEpicById_shouldReturnCreatedEpicById() {
        manager.createEpic(epic);
        Epic epic = manager.getEpicById(3);
        List<Epic> listOfEpics = new ArrayList<>(manager.getAllEpics());

        assertNotNull(epic.getStatus());
        assertEquals(3, epic.getId());
        assertEquals(List.of(epic), listOfEpics);
    }

    @Test
    void getEpicById_shouldReturnNullIfCreatedEpicIsEmpty() {
        manager.createEpic(null);
        Epic epic = manager.getEpicById(0);

        assertNull(epic);
    }

    @Test
    void deleteEpics_shouldDeleteEpics() {
        manager.createEpic(epic);
        manager.removeAllEpics();

        assertEquals(Collections.EMPTY_LIST, manager.getAllEpics());
        assertEquals(Collections.EMPTY_LIST, manager.getPrioritizedTasks());
    }

    @Test
    void deleteEpics_shouldNotDeleteEpicIfListIsEmpty() {
        manager.createEpic(epic);
        manager.removeAllEpics();
        List<Epic> listOfEpics = manager.getAllEpics();
        List<Task> listOfTasks = manager.getPrioritizedTasks();

        assertEquals(0, listOfEpics.size());
        assertEquals(0, listOfTasks.size());
    }

    @Test
    void deleteEpicById_shouldDeleteEpicById() {
        manager.createEpic(epic);
        manager.removeEpicById(3);
        List<Epic> listOfEpics = manager.getAllEpics();
        List<Task> listOfTasks = manager.getPrioritizedTasks();

        assertEquals(0, listOfEpics.size());
        assertEquals(0, listOfTasks.size());
    }

    @Test
    void deleteEpicById_shouldNotDeleteEpicIfPassedIdIsIncorrect() {
        Epic thisEpic = manager.createEpic(epic);
        manager.removeEpicById(0);

        List<Epic> listOfEpics = manager.getAllEpics();

        assertEquals(List.of(thisEpic), listOfEpics);
    }

    @Test
    void getEpics_shouldReturnMapOfEpics() {
        manager.createEpic(epic);
        List<Epic> listOfEpics = manager.getAllEpics();

        assertNotNull(listOfEpics);
        assertEquals(1, listOfEpics.size());
    }

    @Test
    void getEpics_shouldReturnAnEmptyMapOfEpics() {
        assertTrue(manager.getAllEpics().isEmpty());
    }

    @Test
    void updateEpic_shouldUpdateEpicToStatusIN_PROGRESS() {
        Epic thisEpic = manager.createEpic(epic);
        epic.setStatus(TaskStatus.IN_PROGRESS);

        assertEquals(TaskStatus.IN_PROGRESS, manager.getEpicById(thisEpic.getId()).getStatus());
    }

    @Test
    void updateEpic_shouldUpdateEpicToStatusDONE() {
        Epic thisEpic = manager.createEpic(epic);
        epic.setStatus(TaskStatus.DONE);

        assertEquals(TaskStatus.DONE, manager.getEpicById(thisEpic.getId()).getStatus());
    }

    @Test
    void updateEpic_shouldNotUpdateEpicIfItsEmpty() {
        Epic thisEpic = manager.createEpic(epic);
        manager.updateEpic(null);

        assertEquals(thisEpic, manager.getEpicById(thisEpic.getId()));
    }

    // остальные публичные методы

    @Test
    void getPrioritizedTasks_shouldReturnListOfPrioritizedTasks() {
        Task thisTask = manager.createTask(task);
        manager.getTaskById(thisTask.getId());
        List<Task> list = List.of(thisTask);
        List<Task> listOfPrioritizedTasks = manager.getPrioritizedTasks();

        assertEquals(list, listOfPrioritizedTasks);
        assertFalse(listOfPrioritizedTasks.isEmpty());
    }

    @Test
    void getPrioritizedTasks_shouldReturnAnEmptyListOfPrioritizedTasks() {
        assertTrue(manager.getPrioritizedTasks().isEmpty());
    }

    @Test
    void getSubtasksOfEpic_shouldReturnListOfParticularEpicSubtasks() {
        Epic thisEpic = manager.createEpic(epic);
        manager.createSubTask(subTask);
        List<Integer> listOfSubtasksIds = thisEpic.getSubTasksID();

        assertFalse(listOfSubtasksIds.isEmpty());
    }

    @Test
    void getSubtasksOfEpic_shouldReturnAnEmptyListOfParticularEpicSubtasks() {
        Epic thisEpic = manager.createEpic(epic);
        List<Integer> listOfSubtasksIds = thisEpic.getSubTasksID();

        assertTrue(listOfSubtasksIds.isEmpty());
    }

}