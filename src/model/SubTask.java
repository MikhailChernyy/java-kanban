package model;

public class SubTask extends Task {
    private final int epicID;

    public SubTask(String title, String description, int epicID) {
        super(title, description);
        this.epicID = epicID;
    }

    public int getEpicID() {
        return epicID;
    }
}
