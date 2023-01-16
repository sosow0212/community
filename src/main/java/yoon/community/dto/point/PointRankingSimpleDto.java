package yoon.community.dto.point;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yoon.community.domain.point.Point;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointRankingSimpleDto {
    private Long point_id;
    private String username;
    private int point;

    public PointRankingSimpleDto toDto(Point point) {
        return new PointRankingSimpleDto(point.getId(), point.getMember().getUsername(), point.getPoint());
    }
}
