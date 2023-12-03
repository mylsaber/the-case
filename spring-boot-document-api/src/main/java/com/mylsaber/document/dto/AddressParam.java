package com.mylsaber.document.dto;

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

    private String province;

    private String city;

    private String district;

    private String street;
}
