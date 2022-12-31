package yoon.community.dto.board;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NotBlank
public class BoardCreateResponse {
    private Long id;
    private String title;
    private String content;

}
