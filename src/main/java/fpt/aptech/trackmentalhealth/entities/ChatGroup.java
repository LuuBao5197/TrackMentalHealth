package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "chat_group")
public class ChatGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String name;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String des;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String avt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @ManyToOne
    private Users createdBy;

    private int maxMember;

    @ManyToMany
    private List<Users> members;

    public ChatGroup() {
    }

    public ChatGroup(String name, String des, String avt, Date createdAt, Users createdBy, int maxMember, List<Users> members) {
        this.name = name;
        this.des = des;
        this.avt = avt;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.maxMember = maxMember;
        this.members = members;
    }
}
