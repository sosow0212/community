package yoon.community.dto.report;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiOperation(value = "유저 신고 처리 요청")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberReportRequestDto {

    @ApiModelProperty(value = "신고 당하는 사람 아이디", notes = "신고 당하는 사람 아이디를 입력해주세요.", required = true, example = "3")
    @NotNull(message = "신고할 유저의 아이디 입력해주세요.")
    private Long reportedUserId;

    @ApiModelProperty(value = "신고 사유", notes = "신고 사유를 입력해주세요.", required = true, example = "Insulting")
    @NotBlank(message = "신고 사유를 입력하세요.")
    private String content;

}
