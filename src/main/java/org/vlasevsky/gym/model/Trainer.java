package org.vlasevsky.gym.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "trainers")
@Data
@PrimaryKeyJoinColumn(name = "id")

public class Trainer extends User {

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "trainer_training_type",
            joinColumns = @JoinColumn(name = "trainer_id"),
            inverseJoinColumns = @JoinColumn(name = "training_type_id")
    )
    private List<TrainingType> specializations;

    @ManyToMany
    @JoinTable(
            name = "trainee_trainer",
            joinColumns = @JoinColumn(name = "trainer_id"),
            inverseJoinColumns = @JoinColumn(name = "trainee_id")
    )
    @ToString.Exclude
    private Set<Trainee> trainees;

    @ToString.Exclude
    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Training> trainings;


}
