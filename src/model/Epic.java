package model;

import java.util.ArrayList;

public class Epic extends Task {

    private final ArrayList<Integer> subTasksId = new ArrayList<>();

    public Epic(String title, String description) {
        super(title, description);
    }

    public ArrayList<Integer> getSubTasksID() {
        return subTasksId;
    }
}
