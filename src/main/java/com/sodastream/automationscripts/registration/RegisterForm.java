package com.sodastream.automationscripts.registration;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterForm {
    private String firstName;
    private String lastName;
    private String email;
    private String sodaStreamModel;
    private String purchaseLocation;
    private String purchaseDate;
}
