package yoon.community.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import yoon.community.config.constant.Constant;
import yoon.community.dto.point.PointRankingRedisResponseDto;
import yoon.community.repository.point.PointRepository;
import yoon.community.service.point.PointService;

@ExtendWith(MockitoExtension.class)
public class PointServiceTest {
    private final static String RANKING_KEY = Constant.REDIS_RANKING_KEY;

    @InjectMocks
    PointService pointService;

    @Mock
    RedisTemplate redisTemplate;

    @Mock
    PointRepository pointRepository;

    @Test
    public void findPointsRankingWithRedisTest() {
        // given
        redisTemplate.opsForZSet().add(RANKING_KEY, "test1", 10);
        redisTemplate.opsForZSet().add(RANKING_KEY, "test2", 20);
        redisTemplate.opsForZSet().add(RANKING_KEY, "test3", 30);

        // when
        List<PointRankingRedisResponseDto> result = pointService.findPointsRankingWithRedis();

        // then
        assertThat(result.size()).isEqualTo(3);
    }
}
