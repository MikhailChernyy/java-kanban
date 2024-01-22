package model;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<SubTask> subTasks = new ArrayList<>();

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(ArrayList<SubTask> subTasks) {
        this.subTasks = subTasks;
    }

    public Epic(String title, String description, TaskStatus status) {
        super(title, description, status);
    }


}
