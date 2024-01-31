import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import service.InMemoryHistoryManager;
import service.InMemoryTaskManager;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {
        final TaskManager taskManager = new InMemoryTaskManager(new InMemoryHistoryManager());
        Task task1 = new Task("Переезд", "Нужно собрать вещи");
        Task task2 = new Task("Починить машину", "Нужно заехать в автосервис");
        Epic epic1 = new Epic("Эпик 1", "///");
        Epic epic2 = new Epic("Эпик 2", "***");
        SubTask subTask1 = new SubTask("Задача 1", "...", 3);
        SubTask subTask2 = new SubTask("Задача 2", ",,,", 3);
        SubTask subTask3 = new SubTask("Задача 1", "Нужно ...", 4);


        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);
        taskManager.createSubTask(subTask3);

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubTask());

        subTask2.setStatus(TaskStatus.IN_PROGRESS);
        subTask3.setStatus(TaskStatus.DONE);

        taskManager.updateSubTask(subTask2);
        taskManager.updateSubTask(subTask3);
        taskManager.updateEpic(epic1);

        System.out.println("\n" + taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubTask());

        taskManager.removeTaskById(1);
        taskManager.removeEpicById(4);
        System.out.println("\n" + taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
    }
}
