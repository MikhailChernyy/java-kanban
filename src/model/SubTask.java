package model;

public class SubTask extends Task {
    private Epic epic;

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }

    public SubTask(String title, String description, TaskStatus status) {
        super(title, description, status);
    }


}
