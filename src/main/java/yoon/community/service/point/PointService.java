package yoon.community.service.point;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yoon.community.domain.point.Point;
import yoon.community.dto.point.PointRankingSimpleDto;
import yoon.community.dto.point.PointRankingWithPagingResponseDto;
import yoon.community.dto.point.RankingPageInfoDto;
import yoon.community.repository.member.MemberRepository;
import yoon.community.repository.point.PointRepository;

@RequiredArgsConstructor
@Service
public class PointService {
    private final MemberRepository memberRepository;
    private final PointRepository pointRepository;
    private final RedisTemplate<String, Integer> redisTemplate;

    @Transactional(readOnly = true)
    public PointRankingWithPagingResponseDto findPointsRanking(Integer page) {
        Page<Point> points = makePointRankingPages(page);
        return responseRankingPaging(points);
    }

    private PointRankingWithPagingResponseDto responseRankingPaging(Page<Point> points) {
        List<PointRankingSimpleDto> pointRanking = points.stream()
                .map(point -> new PointRankingSimpleDto().toDto(point))
                .collect(Collectors.toList());
        return PointRankingWithPagingResponseDto.toDto(pointRanking, new RankingPageInfoDto(points));
    }

    private Page<Point> makePointRankingPages(Integer page) {
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("point").descending());
        return pointRepository.findAll(pageRequest);
    }
}
