package org.vlasevsky.gym.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "trainees")
@Data
@PrimaryKeyJoinColumn(name = "id")
public class Trainee extends User {

    @Column(name = "date_of_birth")
    private Date dateOfBirth;
    @Column(name = "address")
    private String address;

    @ManyToMany(mappedBy = "trainees")
    @ToString.Exclude
    private List<Trainer> trainers;

    @OneToMany(mappedBy = "trainee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Training> trainings;
}