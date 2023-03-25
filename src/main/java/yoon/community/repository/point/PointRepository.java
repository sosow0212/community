package yoon.community.repository.point;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import yoon.community.domain.member.Member;
import yoon.community.domain.point.Point;

public interface PointRepository extends JpaRepository<Point, Long> {

    Page<Point> findAll(Pageable pageable);
}
