package yoon.community.dto.comment;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentReadCondition {

    @NotNull(message = "게시글 번호를 입력해주세요.")
    @PositiveOrZero(message = "올바른 게시글 번호를 입력해주세요. (0 이상)")
    private Long boardId;
}
