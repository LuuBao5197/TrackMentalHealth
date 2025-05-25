package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "Users", uniqueConstraints = {
        @UniqueConstraint(name = "UQ__Users__F3DBC5727BBCF3A7", columnNames = {"username"})
})
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Email(message = "Email is invalid")
    @Column(name = "email")
    private String email;

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

    @Future(message = "OTP expiry must be in the future")
    @Column(name = "otpExpiry")
    private LocalDateTime otpExpiry;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_Id")
    private Role roleId;

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