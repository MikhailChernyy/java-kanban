package service;

import exeptions.ManagerSaveException;
import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;
    private final InMemoryTaskManager manager = new InMemoryTaskManager();

    public FileBackedTaskManager(String path) {
        this.file = new File(path);
    }

    private static Task fromString(String value) {
        Task task = new Task();
        List<Integer> subtasksIds = new ArrayList<>();
        int id = 0;
        TaskType type = null;
        String title = null;
        TaskStatus status = null;
        String description = null;
        LocalDateTime startTime = null;
        Duration duration = null;
        int epicId = 0;
        String[] elements = value.split(",");
        if (elements[2].equals("EPIC")) {
            subtasksIds = List.of(Integer.parseInt(elements[0]));
        } else {
            id = Integer.parseInt(elements[0]);
            type = TaskType.valueOf(elements[1]);
            title = String.valueOf(elements[2]);
            status = TaskStatus.valueOf(elements[3]);
            description = elements[4];
            duration = Duration.parse(elements[5]);
            startTime = LocalDateTime.parse(elements[6]);

            if (elements.length == 8) {
                epicId = Integer.parseInt(elements[7]);
            }
        }

        if (type == TaskType.TASK) {
            return new Task(id, title, description, duration, startTime, status);
        } else if (type == TaskType.SUBTASK) {
            return new SubTask(epicId, id, title, description, duration, startTime, status);
        } else if (type == TaskType.EPIC) {
            return new Epic((ArrayList<Integer>) subtasksIds, id, title, description, duration, startTime, status);
        }
        return task;
    }

    private static List<Integer> historyFromString(String value) {
        List<Integer> tasksId = new ArrayList<>();
        if (value != null) {
            String[] idsString = value.split(",");
            for (String idString : idsString) {
                tasksId.add(Integer.valueOf(idString));
            }
        }
        return tasksId;
    }

    @Override
    public Task getTaskById(int id) {
        save();
        return super.getTaskById(id);
    }

    @Override
    public Task createTask(Task task) {
        super.createTask(task);
        save();
        return task;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public Epic getEpicById(int id) {
        save();
        return super.getEpicById(id);
    }

    @Override
    public Epic createEpic(Epic epic) {
        super.createEpic(epic);
        save();
        return epic;
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public SubTask getSubTaskById(int id) {
        save();
        return super.getSubTaskById(id);
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        super.createSubTask(subTask);
        save();
        return subTask;
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void removeSubTaskById(int id) {
        super.removeSubTaskById(id);
        save();
    }

    public void save() {
        try (Writer writer = Files.newBufferedWriter(Paths.get(file.toURI()), StandardCharsets.UTF_8)) {
            writer.write("id,type,title,status,description,startTime,duration,epic");
            HashMap<Integer, String> allTasks = new HashMap<>();

            HashMap<Integer, Task> tasks = super.getTasks();
            for (Integer id : tasks.keySet()) {
                allTasks.put(id, tasks.get(id).toStringFromFile());
            }

            HashMap<Integer, Epic> epics = super.getEpics();
            for (Integer id : epics.keySet()) {
                allTasks.put(id, epics.get(id).toStringFromFile());
            }

            HashMap<Integer, SubTask> subtasks = super.getSubTasks();
            for (Integer id : subtasks.keySet()) {
                allTasks.put(id, subtasks.get(id).toStringFromFile());
            }

            for (String value : allTasks.values()) {
                writer.write(String.format("%s\n", value));
            }
            writer.write("\n");

            for (Task task : super.getHistory()) {
                writer.write(task.getId() + ",");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла");
        }
    }

    public  void loadFromFile(File file) {
        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(file.toURI()), StandardCharsets.UTF_8)) {
            String line;
            while (bufferedReader.ready()) {
                line = bufferedReader.readLine();
                if (line.isEmpty()) {
                    break;
                }
                if (line.contains("id")) {
                    continue;
                }

                Task task = fromString(line);

                if (task.getType().equals(TaskType.EPIC)) {
                    super.createEpic((Epic) task);
                } else if (task.getType().equals(TaskType.SUBTASK)) {
                    super.createSubTask((SubTask) task);
                } else {
                    createTask(task);
                }
            }

            String lineWithHistory = bufferedReader.readLine();
            for (int id : historyFromString(lineWithHistory)) {
                if (manager.getTasks().containsKey(id)) {
                    getHistoryManager().add(getTasks().get(id));
                } else if (manager.getSubTasks().containsKey(id)) {
                    getHistoryManager().add(getSubTasks().get(id));
                } else {
                    getHistoryManager().add(getEpics().get(id));
                }
            }
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка чтения файла");
        }
    }
}
