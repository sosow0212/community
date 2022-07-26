package yoon.community.service;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.context.request.RequestContextHolder;
import yoon.community.dto.board.BoardCreateRequest;
import yoon.community.entity.category.Category;
import yoon.community.entity.user.User;
import yoon.community.repository.board.BoardRepository;
import yoon.community.repository.board.FavoriteRepository;
import yoon.community.repository.board.LikeBoardRepository;
import yoon.community.repository.category.CategoryRepository;
import yoon.community.repository.user.UserRepository;
import yoon.community.service.board.BoardService;
import yoon.community.service.file.FileService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static yoon.community.factory.BoardFactory.createBoardWithImages;
import static yoon.community.factory.ImageFactory.createImage;
import static yoon.community.factory.UserFactory.createUserWithAdminRole;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {
    @InjectMocks
    BoardService boardService;

    @Mock
    UserRepository userRepository;
    @Mock
    BoardRepository boardRepository;
    @Mock
    FileService fileService;
    @Mock
    LikeBoardRepository likeBoardRepository;
    @Mock
    FavoriteRepository favoriteRepository;
    @Mock
    CategoryRepository categoryRepository;


//    @Test
//    @DisplayName("Board create 서비스 테스트")
//    void createTest() {
//        // given
//        BoardCreateRequest req = new BoardCreateRequest("title", "content", 1, List.of(
//                new MockMultipartFile("test1", "test1.PNG", MediaType.IMAGE_PNG_VALUE, "test1".getBytes()),
//                new MockMultipartFile("test2", "test2.PNG", MediaType.IMAGE_PNG_VALUE, "test2".getBytes()),
//                new MockMultipartFile("test3", "test3.PNG", MediaType.IMAGE_PNG_VALUE, "test3".getBytes())
//        ));
//
//        given(userRepository.findById(anyInt())).willReturn(Optional.of(createUserWithAdminRole()));
//        given(categoryRepository.findById(anyInt())).willReturn(Optional.of(new Category("name", null)));
//        given(boardRepository.save(any())).willReturn(createBoardWithImages(
//                IntStream.range(0, req.getImages().size()).mapToObj(i -> createImage()).collect(toList()))
//        );
//
//        // when
//        boardService.create(req);
//
//        // then
//        verify(boardRepository).save(any());
//        verify(fileService, times(req.getImages().size())).upload(any(), anyString());
//    }
}
