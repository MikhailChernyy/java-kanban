package model;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subTasksID = new ArrayList<>();

    public Epic(String title, String description) {
        super(title, description);
    }

    public ArrayList<Integer> getSubTasksID() {
        return subTasksID;
    }

    public void setSubTasksID(ArrayList<Integer> subTasksID) {
        this.subTasksID = subTasksID;
    }
}
