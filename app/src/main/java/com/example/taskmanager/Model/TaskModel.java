package com.example.taskmanager.Model;

public class TaskModel {
    public int taskId;
    public String title;
    public String description;
    public String dueDate;
    public Boolean status;

    public TaskModel(String title, String description, String dueDate, Boolean status){
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
    }
    public TaskModel(int taskId, String title, String description, String dueDate, Boolean status){
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
