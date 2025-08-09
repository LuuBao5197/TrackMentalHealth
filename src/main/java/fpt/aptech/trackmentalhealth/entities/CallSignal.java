package fpt.aptech.trackmentalhealth.entities;

public class CallSignal {
    private String type;       // CALL_REQUEST, CALL_ACCEPTED, CALL_REJECTED, CALL_ENDED
    private int callerId;
    private String callerName;
    private int sessionId;

    // Getters và setters
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public int getCallerId() {
        return callerId;
    }
    public void setCallerId(int callerId) {
        this.callerId = callerId;
    }
    public String getCallerName() {
        return callerName;
    }
    public void setCallerName(String callerName) {
        this.callerName = callerName;
    }
    public int getSessionId() {
        return sessionId;
    }
    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }
}
