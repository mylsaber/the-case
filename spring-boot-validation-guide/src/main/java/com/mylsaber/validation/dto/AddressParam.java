package com.mylsaber.validation.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author mylsaber
 */
@Data
public class AddressParam implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "could not be empty")
    private String province;

    @NotEmpty(message = "could not be empty")
    private String city;

    @NotEmpty(message = "could not be empty")
    private String district;

    @NotEmpty(message = "could not be empty")
    private String street;
}
