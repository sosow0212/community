package yoon.community.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import yoon.community.exception.*;
import yoon.community.exception.NotSelfReportException;
import yoon.community.response.Response;

import javax.management.relation.RoleNotFoundException;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {
    // 500 에러
    @ExceptionHandler(IllegalArgumentException.class) // 지정한 예외가 발생하면 해당 메소드 실행
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 각 예외마다 상태 코드 지정
    public Response illegalArgumentExceptionAdvice(IllegalArgumentException e) {
        log.info("e = {}", e.getMessage());
        return Response.failure(500, e.getMessage().toString());
    }

    // 500 에러
    // 컨버트 실패
    @ExceptionHandler(CannotConvertHelperException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response cannotConvertNestedStructureException(CannotConvertHelperException e) {
        log.error("e = {}", e.getMessage());
        return Response.failure(500, e.getMessage().toString());
    }

    // 400 에러
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response methodArgumentNotValidException(MethodArgumentNotValidException e) { // 2
        return Response.failure(400, e.getBindingResult().getFieldError().getDefaultMessage());
    }

    // 400 에러
    // 토큰 만료
    @ExceptionHandler(TokenExpiredException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response tokenExpiredException() {
        return Response.failure(400, "토큰이 만료되었습니다.");
    }

    // 400 에러
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response bindException(BindException e) {
        return Response.failure(400, e.getBindingResult().getFieldError().getDefaultMessage());
    }


    // 401 응답
    // 아이디 혹은 비밀번호 오류시 발생
    @ExceptionHandler(LoginFailureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response loginFailureException() {
        return Response.failure(401, "로그인에 실패하였습니다.");
    }

    // 401 응답
    // 요청자와 요청한 유저의 정보가 일치하지 않을시에 발생
    @ExceptionHandler(MemberNotEqualsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response memberNotEqualsException() {
        return Response.failure(401, "유저 정보가 일치하지 않습니다.");
    }


    // 404 응답
    // 요청한 유저를 찾을 수 없음
    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response memberNotFoundException() {
        return Response.failure(404, "요청한 회원을 찾을 수 없습니다.");
    }

    // 404 응답
    // 요청한 Favorite 찾을 수 없음
    @ExceptionHandler(FavoriteNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response favoriteNotFoundException() {
        return Response.failure(404, "요청한 즐겨찾기를 찾을 수 없습니다.");
    }


    // 404 응답
    // 요청한 자원을 찾을 수 없음
    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response roleNotFoundException() {
        return Response.failure(404, "요청한 권한 등급을 찾을 수 없습니다.");
    }

    // 404 응답
    // 요청한 Message를 찾을 수 없음
    @ExceptionHandler(MessageNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response messageNotFountException() {
        return Response.failure(404, "메시지를 찾을 수 없습니다.");
    }

    // 404 응답
    // 요청한 Comment를 찾을 수 없음
    @ExceptionHandler(CommentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response commentNotFoundException() {
        return Response.failure(404, "댓글 찾을 수 없습니다.");
    }


    // 404 응답
    // Image 형식 지원하지 않음
    @ExceptionHandler(UnsupportedImageFormatException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response unsupportedImageFormatException() {
        return Response.failure(404, "이미지 형식을 지원하지 않습니다.");
    }

    // 404 응답
    // 파일 업로드 실패
    @ExceptionHandler(FileUploadFailureException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response fileUploadFailureException(FileUploadFailureException e) {
        log.error("e = {}", e.getMessage());
        return Response.failure(404, "이미지 업로드 실패");
    }

    // 404 응답
    // 게시글 찾기 실패
    @ExceptionHandler(BoardNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response boardNotFoundException() {
        return Response.failure(404, "게시글을 찾을 수 없습니다.");
    }

    // 404 응답
    // 이미 신고 처리해서 신고 실패 실패
    @ExceptionHandler(AlreadyReportException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response alreadyReportException() {
        return Response.failure(404, "이미 신고처리를 했습니다.");
    }

    // 404 응답
    // 자기 자신을 신고할 수 없다.
    @ExceptionHandler(NotSelfReportException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response notSelfReportException() {
        return Response.failure(404, "자기 자신은 신고할 수 없습니다.");
    }

    // 404 응답
    // 신고되지 않은 게시물 혹은 유저
    @ExceptionHandler(NotReportedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response notReportedException() {
        return Response.failure(404, "신고되지 않은 유저 혹은 게시물입니다.");
    }


    // 404 응답
    // 카테고리 찾을 수 없음
    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response categoryNotFoundException() {
        return Response.failure(404, "카테고리를 찾을 수 없습니다.");
    }


    // 409 응답
    // username 중복
    @ExceptionHandler(MemberUsernameAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response memberEmailAlreadyExistsException(MemberUsernameAlreadyExistsException e) {
        return Response.failure(409, e.getMessage() + "은 중복된 아이디 입니다.");
    }

    // 409 응답
    // nickname 중복
    @ExceptionHandler(MemberNicknameAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response memberNicknameAlreadyExistsException(MemberNicknameAlreadyExistsException e) {
        return Response.failure(409, e.getMessage() + "은 중복된 닉네임 입니다.");
    }
}
