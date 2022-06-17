package yoon.community.dto.message;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel(value = "쪽지 생성 요청")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageCreateRequest {

    @ApiModelProperty(value = "메시지 제목", notes = "메시지 제목을 입력해주세요.", required = true, example = "message title")
    @NotBlank(message = "메시지 제목을 입력해주세요.")
    private String title;

    @ApiModelProperty(value = "메시지 내용", notes = "메시지 내용 입력해주세요.", required = true, example = "message content")
    @NotBlank(message = "메시지 내용을 입력해주세요.")
    private String content;

    @ApiModelProperty(value = "수신자 닉네임", notes = "수신자 닉네임을 입력하세요.", required = true, example = "user30")
    @NotNull(message = "받는 사람 닉네임을 입력해주세요")
    private String receiverNickname;
}
