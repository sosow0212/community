package yoon.community.dto.comment;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yoon.community.dto.user.UserEditRequestDto;
import yoon.community.entity.comment.Comment;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private int id;
    private String content;
    private UserEditRequestDto userEditRequestDto;
    private LocalDateTime createdAt;

    public CommentDto toDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getContent(),
                UserEditRequestDto.toDto(comment.getUser()),
                comment.getCreatedAt()
        );
    }
}
