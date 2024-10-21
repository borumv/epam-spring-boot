package com.vlasevsky.gym.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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

    @ManyToMany(mappedBy = "trainees", fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Trainer> trainers;

    @OneToMany(mappedBy = "trainee", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Training> trainings;
}