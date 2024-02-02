package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> history = new ArrayList<>();

    @Override
    public void addTaskInHistory(Task task) {
        Task newVersionTask = new Task(task.getTitle(), task.getDescription());
        newVersionTask.setId(task.getId());
        if (history.size() > 10) {
            history.remove(0);
            for (int i = 0; i < history.size() - 1; i++) {
                Task newTask = history.get(i + 1);
                history.set(i + 1, newTask);
            }
            history.add(newVersionTask);
        } else {
            history.add(newVersionTask);
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }
}
