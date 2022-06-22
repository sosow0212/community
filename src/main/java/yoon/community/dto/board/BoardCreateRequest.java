package yoon.community.dto.board;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;

@ApiOperation(value = "게시글 생성 요청")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardCreateRequest {
    @ApiModelProperty(value = "게시글 제목", notes = "게시글 제목을 입력해주세요.", required = true, example = "게시글 제목")
    @NotBlank(message = "게시글 제목을 입력해주세요.")
    private String title;

    @ApiModelProperty(value = "게시글 본문", notes = "게시글 본문을 입력해주세요.", required = true, example = "게시글 본문")
    @NotBlank(message = "게시글 본문을 입력해주세요.")
    private String content;

//    @ApiModelProperty(hidden = true)
//    @Null
//    private int userId;

    @ApiModelProperty(value = "이미지", notes = "이미지를 첨부해주세요.")
    private List<MultipartFile> images = new ArrayList<>();
}
