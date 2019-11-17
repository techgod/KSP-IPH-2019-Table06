package com.ksp.donut.uca.tasks;

public class TaskDetails {

   private String taskName;
   private String taskDeadline;
   private String assignedToNo;
   private String assignedToName;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDeadline() {
        return taskDeadline;
    }

    public void setTaskDeadline(String taskDeadline) {
        this.taskDeadline = taskDeadline;
    }

    public String getAssignedToNo() {
        return assignedToNo;
    }

    public void setAssignedToNo(String assignedBy) {
        this.assignedToNo = assignedBy;
    }

    public String getAssignedToName() {
        return assignedToName;
    }

    public void setAssignedToName(String assignedToName) {
        this.assignedToName = assignedToName;
    }
}
