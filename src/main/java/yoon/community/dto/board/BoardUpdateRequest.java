package yoon.community.dto.board;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@ApiModel(value = "게시글 수정")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardUpdateRequest {
    @ApiModelProperty(value = "게시글 제목", notes = "게시글 제목을 입력해주세요.")
    @NotBlank(message = "게시글 제목을 입력해주세요.")
    private String title;

    @ApiModelProperty(value = "게시글 내용", notes = "게시글 내용을 입력해주세요.")
    @NotBlank(message = "게시글 내용을 입력해주세요.")
    private String content;

    @ApiModelProperty(value = "추가된 이미지", notes = "추가된 이미지를 첨부해주세요.")
    private List<MultipartFile> addedImages = new ArrayList<>();

    @ApiModelProperty(value = "제거된 이미지 아이디", notes = "제거된 이미지 아이디를 입력해주세요.")
    private List<Integer> deletedImages = new ArrayList<>();
}
