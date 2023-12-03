package com.mylsaber.document.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author mylsaber
 */
@Data
public class UserParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String userId;

    private String telephone;

    private String email;

    private String cardNo;

    private String nickName;

    private int sex;

    private int age;

}
