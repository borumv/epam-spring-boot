package com.vlasevsky.gym.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;

public record TraineeRegistrationDto(@NotNull
                                     @Size(min = 1, max = 100)
                                     String firstName,
                                     @NotNull
                                     @Size(min = 1, max = 100)
                                     String lastName,

                                     Date dateOfBirth,
                                     @Size(max = 200)
                                     String address
) {

}
