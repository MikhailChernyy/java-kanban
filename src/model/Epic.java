package model;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subTasksId = new ArrayList<>();

    public Epic(String title, String description) {
        super(title, description);
        this.setType(TaskType.EPIC);
    }

    public Epic(ArrayList<Integer> subTasksId, int id, String title, String description, TaskStatus status) {
        super(id, title, description, status);
        this.subTasksId  = subTasksId;
    }

    public ArrayList<Integer> getSubTasksID() {
        return subTasksId;
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public String toStringFromFile() {
        return String.format("%s,%s,%s,%s,%s,%s,%s", subTasksId, getId(), getType(), getTitle(),
                getStatus(), getDescription(), "");
    }
}
