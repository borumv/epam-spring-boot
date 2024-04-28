package org.vlasevsky.gym.dto;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
