package yoon.community.dto.comment;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yoon.community.dto.member.MemberEditRequestDto;
import yoon.community.domain.comment.Comment;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private String content;
    private MemberEditRequestDto memberEditRequestDto;
    private LocalDateTime createdAt;

    public CommentDto toDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getContent(),
                MemberEditRequestDto.toDto(comment.getMember()),
                comment.getCreatedAt()
        );
    }
}
