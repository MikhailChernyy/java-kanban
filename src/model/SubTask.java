package model;

public class SubTask extends Task {
    private int epicID;

    public SubTask(String title, String description, int epicID) {
        super(title, description);
        this.epicID = epicID;
    }

    public int getEpicID() {
        return epicID;
    }

    public void setEpicID(int epicID) {
        this.epicID = epicID;
    }
}
