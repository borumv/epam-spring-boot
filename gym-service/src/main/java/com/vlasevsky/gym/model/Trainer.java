package com.vlasevsky.gym.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "trainers")
@Data
@PrimaryKeyJoinColumn(name = "id")

public class Trainer extends User {

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "trainer_training_type",
            joinColumns = @JoinColumn(name = "trainer_id"),
            inverseJoinColumns = @JoinColumn(name = "training_type_id")
    )
    private Set<TrainingType> specializations = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "trainee_trainer",
            joinColumns = @JoinColumn(name = "trainer_id"),
            inverseJoinColumns = @JoinColumn(name = "trainee_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Trainee> trainees = new HashSet<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Training> trainings = new HashSet<>();


}
