import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {
        final TaskManager taskManager = new TaskManager();
        Task task1 = new Task("Переезд", "Нужно собрать вещи", TaskStatus.NEW);
        Task task2 = new Task("Починить машину", "Нужно заехать в автосервис", TaskStatus.NEW);
        Epic epic1 = new Epic("Эпик 1", "///", TaskStatus.NEW);
        Epic epic2 = new Epic("Эпик 2", "***", TaskStatus.NEW);
        SubTask subTask1 = new SubTask("Задача 1", "...", TaskStatus.NEW);
        SubTask subTask2 = new SubTask("Задача 2", ",,,", TaskStatus.NEW);
        SubTask subTask3 = new SubTask("Задача 1", "Нужно ...", TaskStatus.NEW);

        Epic newEpic1 = new Epic("Переезд 2", "...", TaskStatus.IN_PROGRESS);
        SubTask newSubTask2 = new SubTask("Задача 2", ",,,", TaskStatus.IN_PROGRESS);
        SubTask newSubTask3 = new SubTask("Задача 1", "Нужно ...", TaskStatus.DONE);

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubTask(epic1, subTask1);
        taskManager.createSubTask(epic1, subTask2);
        taskManager.createSubTask(epic2, subTask3);

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubTask());

        taskManager.updateSubTask(subTask2, newSubTask2);
        taskManager.updateSubTask(subTask3, newSubTask3);
        taskManager.updateEpic(epic1, newEpic1);

        System.out.println("\n" + taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubTask());

        taskManager.removeTaskAnID(1);
        taskManager.removeEpicAnID(4);

        System.out.println("\n" + taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
    }
}
