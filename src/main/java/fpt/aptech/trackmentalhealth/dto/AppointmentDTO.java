package fpt.aptech.trackmentalhealth.dto;

public class AppointmentDTO {
    private String timeStart;
    private String status;
    private String note;
    private int userId;
    private int psychologistId;

    public AppointmentDTO() {
    }

    public AppointmentDTO(String timeStart, String status, String note, int userId, int psychologistId) {
        this.timeStart = timeStart;
        this.status = status;
        this.note = note;
        this.userId = userId;
        this.psychologistId = psychologistId;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPsychologistId() {
        return psychologistId;
    }

    public void setPsychologistId(int psychologistId) {
        this.psychologistId = psychologistId;
    }
}
