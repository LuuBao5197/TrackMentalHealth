package fpt.aptech.trackmentalhealth.dto;

public class GetUserNameDTO {

    private String username;
    private String fullname;

    public GetUserNameDTO(String username, String fullname) {
        this.username = username;
        this.fullname = fullname;
    }

    // Getters & Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
