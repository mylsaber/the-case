package com.mylsaber.validation.dto;

import com.mylsaber.validation.annotation.TelephoneNumber;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author mylsaber
 */
@Data
public class UserParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "could not be empty", groups = {UpdateGroup.class})
    private String userId;

    @TelephoneNumber(message = "invalid telephone number") // 这里
    private String telephone;

    @NotEmpty(message = "could not be empty")
    @Email(message = "invalid email")
    private String email;

    @NotEmpty(message = "could not be empty")
    @Pattern(regexp = "^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|X)$", message = "invalid ID")
    private String cardNo;

    @NotEmpty(message = "could not be empty")
    @Length(min = 1, max = 10, message = "nick name should be 1-10")
    private String nickName;

    @NotNull(message = "could not be empty")
    @Range(min = 0, max = 1, message = "sex should be 0-1")
    private int sex;

    @Max(value = 100, message = "Please input valid age")
    private int age;

    @Valid
    private AddressParam address;

    public interface AddGroup {
    }

    public interface UpdateGroup {
    }
}
