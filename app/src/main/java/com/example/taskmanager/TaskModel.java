package com.example.taskmanager;

public class TaskModel {
    int taskId;
    String title, description, dueDate;
    Boolean status;

    TaskModel(String title, String description, String dueDate, Boolean status){
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
    }
    TaskModel(int taskId, String title, String description, String dueDate, Boolean status){
        this.taskId=taskId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
    }

    TaskModel(String title, String dueDate){
        this.title = title;
        this.dueDate = dueDate;
    }

    public TaskModel() {

    }
}
