package yoon.community.controller.point;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import yoon.community.response.Response;
import yoon.community.service.point.PointService;

/**
 * 글쓰면 포인트 10점, 댓글 쓰면 포인트 5점 반대로 지우면 -처리
 * <p>
 * Q. Dto에서 Getter를 쓰지 않고 주는 방법은 없을까?
 */

@Api(value = "Comment Controller", tags = "Comment ")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class PointController {
    private final PointService pointService;

    @ApiOperation(value = "포인트 랭킹 조회", notes = "전체 유저의 포인트 랭킹을 조회합니다.")
    @GetMapping("/points")
    @ResponseStatus(HttpStatus.OK)
    public Response findPointsRanking() {
        return Response.success(pointService.findPointsRankingWithRedis());
    }

    @ApiOperation(value = "포인트 업데이트", notes = "유저의 포인트를 업데이트 합니다.")
    @PutMapping("/points/{username}")
    @ResponseStatus(HttpStatus.OK)
    public Response updatePoint(@PathVariable("username") String username) {
        pointService.updatePoint(username);
        return Response.success();
    }
}
