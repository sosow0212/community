package yoon.community.dto.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardSimpleDto {
    // 메인 페이지에서 보여지는 게시글 정보
    private int id;
    private String title;
    private String nickname;

}
