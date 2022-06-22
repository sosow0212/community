package yoon.community.dto.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardReadCondition {
    @NotNull(message = "페이지 번호를 입력하세요.")
    @PositiveOrZero(message = "올바른 페이지 번호를 입력해주세요. (0 이상)")
    private int page;

    @NotNull(message = "페이지 크기를 입력하세요.")
    @Positive(message = "올바른 페이지 크기를 입력하세요. (1 이상)")
    private int size;

    private List<Integer> userId = new ArrayList<>();
}
