package yoon.community.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import yoon.community.response.Response;
import yoon.community.service.admin.AdminService;

@Api(value = "Admin Controller", tags = "Admin")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AdminController {

    private final AdminService adminService;

    @ApiOperation(value = "정지 유저 관리", notes = "정지된 유저를 관리합니다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/admin/manages/users")
    public Response manageReportedUser() {
        return Response.success(adminService.manageReportedUser());
    }

    @ApiOperation(value = "신고된 유저 정지 해제", notes = "신고된 유저를 정지 해제시킵니다.")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/admin/manages/users/{id}")
    public Response unlockUser(@PathVariable int id) {
        return Response.success(adminService.unlockUser(id));
    }

    @ApiOperation(value = "게시판 관리", notes = "게시판을 관리합니다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/admin/manages/boards")
    public Response manageReportedBoards() {
        return Response.success(adminService.manageReportedBoards());
    }


    @ApiOperation(value = "신고된 게시물 관리", notes = "신고된 게시물을 관리합니다.")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/admin/manages/boards/{id}")
    public Response unlockBoard(@PathVariable int id) {
        return Response.success(adminService.unlockBoard(id));
    }
}
