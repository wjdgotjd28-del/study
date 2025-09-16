package com.catboardback.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto
{
    @NotNull(message = "이메일은 필수 입력 값입니다.")
    private  String email;

    @NotNull(message = "비밀번호는 필수 입력 값입니다.")
    private  String password;

    @NotNull(message = "닉네임은 필수 입력 값입니다.")
    private  String nickName;
}
