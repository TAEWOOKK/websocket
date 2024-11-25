package com.back.websocket.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDTO {

    @NotBlank(message = "이메일이 공백입니다.")
    private String email;
    @NotBlank(message = "패스워드가 공백입니다.")
    private String password;
}
