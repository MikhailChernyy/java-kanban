package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private final int epicId;

    public SubTask(int id, String title, String description, Duration duration, LocalDateTime startTime, int epicId) {
        super(id, title, description, duration, startTime);
        this.setType(TaskType.SUBTASK);
        this.epicId = epicId;
    }

    public SubTask(int epicId, int id, String title, String description, Duration duration, LocalDateTime startTime, TaskStatus status) {
        super(id, title, description, duration, startTime);
        this.setStatus(status);
        this.epicId = epicId;
    }

    public SubTask(String title, String description, int epicId) {
        super(title, description);
        this.setType(TaskType.SUBTASK);
        this.epicId = epicId;
    }

    public SubTask(int epicId, int id, String title, String description, TaskStatus status) {
        super(id, title, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toStringFromFile() {
        return String.format("%s,%s,%s,%s,%s,%s,%s", epicId, getId(), getType(), getTitle(),
                getStatus(), getDescription(), "");
    }
}
