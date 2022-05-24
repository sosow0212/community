package yoon.community.dto.sign;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "로그인 요청")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {
    @ApiModelProperty(value = "아이디", notes = "아이디를 입력해주세요", required = true, example = "sosow0212")
    @NotBlank(message = "{LoginRequestDto.username.notBlank}")
    private String username;

    @ApiModelProperty(value = "비밀번호", required = true, example = "123456")
    @NotBlank(message = "{LoginRequestDto.password.notBlank}")
    private String password;
}