package yoon.community.dto.point;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PointRankingWithPagingResponseDto {
    private List<PointRankingSimpleDto> ranking;
    private RankingPageInfoDto rankingPageInfoDto;

    public static PointRankingWithPagingResponseDto toDto(List<PointRankingSimpleDto> ranking, RankingPageInfoDto rankingPageInfoDto) {
        return new PointRankingWithPagingResponseDto(ranking, rankingPageInfoDto);
    }
}
