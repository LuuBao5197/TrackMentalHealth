package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class ChatMessageGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private ChatGroup group;

    @ManyToOne
    private Users sender;

    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    private Date sendAt;

    public ChatMessageGroup() {
    }

    public ChatMessageGroup(ChatGroup group, Users sender, String content, Date sendAt) {
        this.group = group;
        this.sender = sender;
        this.content = content;
        this.sendAt = sendAt;
    }
}
