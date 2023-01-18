package yoon.community.dto.point;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PointRankingRedisResponseDto {
    private String username;
    private int point;

    public static PointRankingRedisResponseDto toDto(ZSetOperations.TypedTuple<String> tuple) {
        return new PointRankingRedisResponseDto(tuple.getValue(), tuple.getScore().intValue());
    }
}
