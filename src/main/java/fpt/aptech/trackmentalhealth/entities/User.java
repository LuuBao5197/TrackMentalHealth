package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "Users", uniqueConstraints = {
        @UniqueConstraint(name = "UQ__Users__F3DBC5727BBCF3A7", columnNames = {"username"})
})
public class User {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @Nationalized
    @Column(name = "username")
    private String username;

    @Size(max = 255)
    @Nationalized
    @Column(name = "password")
    private String password;

    @Size(max = 255)
    @Nationalized
    @Column(name = "avatar")
    private String avatar;

    @Size(max = 255)
    @Nationalized
    @Column(name = "status")
    private String status;

    @Size(max = 255)
    @Nationalized
    @Column(name = "token")
    private String token;

    @Size(max = 255)
    @Nationalized
    @Column(name = "refreshtoken")
    private String refreshtoken;

    @Size(max = 255)
    @Nationalized
    @Column(name = "otp")
    private String otp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role")
    private Role role;

    @Size(max = 255)
    @Nationalized
    @Column(name = "fullname")
    private String fullname;

    @Size(max = 255)
    @Nationalized
    @Column(name = "address")
    private String address;

    @Column(name = "dob")
    private LocalDate dob;

    @Size(max = 255)
    @Nationalized
    @Column(name = "gender")
    private String gender;


}