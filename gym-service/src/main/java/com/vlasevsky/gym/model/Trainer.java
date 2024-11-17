package com.vlasevsky.gym.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

//@Entity
//@Table(name = "trainers")
@Data


public class Trainer extends User {

   // @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)

    private Set<TrainingType> specializations = new HashSet<>();

    //@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Trainee> trainees = new HashSet<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
   // @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Training> trainings = new HashSet<>();


}
