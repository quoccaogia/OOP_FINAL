package vn.edu.crud.model;

import java.time.LocalDateTime;

public class CareSchedule {
    private String careId;
    private String flowerId;
    private String employeeId;
    private LocalDateTime checkDate;
    private String freshnessLevel;
    private String waterStatus;
    private String actionTaken;
    private String note;

    public CareSchedule() {}

    public CareSchedule(String careId, String flowerId, String employeeId,
                        LocalDateTime checkDate, String freshnessLevel,
                        String waterStatus, String actionTaken, String note) {
        this.careId = careId;
        this.flowerId = flowerId;
        this.employeeId = employeeId;
        this.checkDate = checkDate;
        this.freshnessLevel = freshnessLevel;
        this.waterStatus = waterStatus;
        this.actionTaken = actionTaken;
        this.note = note;
    }

    public String getCareId() { return careId; }
    public void setCareId(String careId) { this.careId = careId; }

    public String getFlowerId() { return flowerId; }
    public void setFlowerId(String flowerId) { this.flowerId = flowerId; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public LocalDateTime getCheckDate() { return checkDate; }
    public void setCheckDate(LocalDateTime checkDate) { this.checkDate = checkDate; }

    public String getFreshnessLevel() { return freshnessLevel; }
    public void setFreshnessLevel(String freshnessLevel) { this.freshnessLevel = freshnessLevel; }

    public String getWaterStatus() { return waterStatus; }
    public void setWaterStatus(String waterStatus) { this.waterStatus = waterStatus; }

    public String getActionTaken() { return actionTaken; }
    public void setActionTaken(String actionTaken) { this.actionTaken = actionTaken; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    @Override
    public String toString() {
        return "CareSchedule{" + careId + ", " + flowerId + ", " + freshnessLevel + "}";
    }
}