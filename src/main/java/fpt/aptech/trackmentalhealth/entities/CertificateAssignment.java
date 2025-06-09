package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "CertificateAssignment")
public class CertificateAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "certificate_id")
    private Certificate certificate;

    @Column(name = "user_type")
    private String userType; // "Psychologist", "ContentCreator", "TestDesigner"

    @Column(name = "user_id")
    private Integer userId;
}
