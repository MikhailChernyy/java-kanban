package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subTasksId = new ArrayList<>();

    private LocalDateTime endTime;

    public Epic(int id, String title, String description, Duration duration, LocalDateTime startTime) {
        super(id, title, description, duration, startTime);
        this.setType(TaskType.EPIC);

    }

    public Epic(ArrayList<Integer> subTasksIds,
                int id, String title,
                String description, Duration duration,
                LocalDateTime startTime) {
        super(id, title, description, duration, startTime);
        this.subTasksId = subTasksIds;
        this.endTime = super.getEndTime();
        this.setType(TaskType.EPIC);
    }

    public Epic(ArrayList<Integer> subTasksIds,
                int id, String title,
                String description, Duration duration,
                LocalDateTime startTime, TaskStatus status) {
        super(id, title, description, duration, startTime);
        this.subTasksId = subTasksIds;
        this.endTime = super.getEndTime();
        this.setType(TaskType.EPIC);
        this.setStatus(status);
    }

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

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
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
