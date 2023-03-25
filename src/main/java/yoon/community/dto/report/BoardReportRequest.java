package yoon.community.dto.report;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiOperation(value = "게시글 신고 처리 요청")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardReportRequest {

    @ApiModelProperty(value = "신고 당하는 게시글 아이디", notes = "신고 당할 게시글 아이디를 입력해주세요.", required = true, example = "3")
    @NotNull(message = "신고할 게시글의 아이디를 입력해주세요.")
    private Long reportedBoardId;

    @ApiModelProperty(value = "신고 사유", notes = "신고 사유를 입력해주세요.", required = true, example = "Insulting")
    @NotBlank(message = "신고 사유를 입력하세요. ")
    private String content;
}
