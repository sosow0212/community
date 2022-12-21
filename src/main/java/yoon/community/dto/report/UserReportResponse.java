package yoon.community.dto.report;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yoon.community.dto.user.UserEditRequestDto;
import yoon.community.entity.report.UserReportHistory;
import yoon.community.entity.user.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserReportResponse {
    private int id;
    private UserEditRequestDto reportedUser;
    private String content;
    private LocalDateTime createdAt;


    public UserReportResponse toDto(UserReportHistory userReportHistory, User reportedUser) {
        return new UserReportResponse(
                userReportHistory.getId(),
                UserEditRequestDto.toDto(reportedUser),
                userReportHistory.getContent(),
                userReportHistory.getCreatedAt()
        );
    }
}
