package yoon.community.service.point;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yoon.community.config.constant.Constant;
import yoon.community.domain.point.Point;
import yoon.community.dto.point.PointRankingRedisResponseDto;
import yoon.community.dto.point.PointRankingSimpleDto;
import yoon.community.dto.point.PointRankingWithPagingResponseDto;
import yoon.community.dto.point.RankingPageInfoDto;
import yoon.community.repository.member.MemberRepository;
import yoon.community.repository.point.PointRepository;

@RequiredArgsConstructor
@Service
public class PointService {
    private final static String RANKING_KEY = Constant.REDIS_RANKING_KEY;
    private final RedisTemplate redisTemplate;
    private final PointRepository pointRepository;

    @Transactional(readOnly = true)
    public PointRankingWithPagingResponseDto findPointsRankingWithMySQL(Integer page) {
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

    @Transactional(readOnly = true)
    public List<PointRankingRedisResponseDto> findPointsRankingWithRedis() {
        ZSetOperations<String, String> stringStringZSetOperations = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<String>> typedTuples = stringStringZSetOperations.reverseRangeWithScores(RANKING_KEY, 0, 10);
        List<PointRankingRedisResponseDto> result = typedTuples.stream()
                .map(i -> new PointRankingRedisResponseDto().toDto(i))
                .collect(Collectors.toList());
        return result;
    }

    @Transactional
    public void updatePoint(String username) {
        ZSetOperations<String, String> zSetOperation = redisTemplate.opsForZSet();
        zSetOperation.incrementScore(RANKING_KEY, username, 5);
    }

    // todo : 주기적으로 mysql 백업하는 로직 작성하기
}
