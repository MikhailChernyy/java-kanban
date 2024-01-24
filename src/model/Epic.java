package model;

import java.util.ArrayList;

public class Epic extends Task {

    private final ArrayList<Integer> subTasksID = new ArrayList<>();

    public Epic(String title, String description) {
        super(title, description);
    }

    public ArrayList<Integer> getSubTasksID() {
        return subTasksID;
    }
}
