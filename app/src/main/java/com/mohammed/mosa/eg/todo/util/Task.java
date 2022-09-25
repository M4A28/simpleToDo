package com.mohammed.mosa.eg.todo.util;

import java.io.Serializable;
import java.util.Date;

public class Task implements Comparable<Task>, Serializable{

    private int id;
    private String task;
    private boolean isDone;
    private int priority;
    private Date finishDate;

    public Task() {
    }
    // for edit and insert
    public Task(String task, boolean isDone, int priority, Date finishDate) {
        this.task = task;
        this.isDone = isDone;
        this.priority = priority;
        this.finishDate = finishDate;
    }

    // for geting
    public Task(int id, String task, boolean isDone, int priority, Date finishDate) {
        this.id = id;
        this.task = task;
        this.isDone = isDone;
        this.priority = priority;
        this.finishDate = finishDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public String toCSV(){
        return id +"," + task + ","+ Utility.toInt(isDone) + "," + priority + "," + Utility.toLong(finishDate);
    }

    @Override
    public int compareTo(Task task) {
        // sort by date if thay have same date sort by priprity
        int a = Long.compare(this.getFinishDate().getTime(), task.finishDate.getTime());
        if(a == 0)
            return Integer.compare(this.getPriority(), task.getPriority());
        return a;
    }

}
