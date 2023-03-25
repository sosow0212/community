package yoon.community.domain.board;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.web.multipart.MultipartFile;
import yoon.community.domain.category.Category;
import yoon.community.domain.common.EntityDate;
import yoon.community.domain.member.Member;
import yoon.community.dto.board.BoardUpdateRequest;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Board extends EntityDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category category;

    @OneToMany(mappedBy = "board", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Image> images;

    @Column(nullable = true)
    private int liked; // 추천 수

    @Column(nullable = true)
    private int favorited; // 즐겨찾기 수

    @Column(nullable = false)
    private boolean reported;

    public Board(String title, String content, Member member, Category category, List<Image> images) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.liked = 0;
        this.favorited = 0;
        this.reported = false;
        this.category = category;
        this.images = new ArrayList<>();
        addImages(images);
    }

    public ImageUpdatedResult update(BoardUpdateRequest req) {
        this.title = req.getTitle();
        this.content = req.getContent();

        ImageUpdatedResult result = findImageUpdatedResult(req.getAddedImages(), req.getDeletedImages());
        addImages(result.getAddedImages());
        deleteImages(result.getDeletedImages());
        return result;
    }

    private void addImages(List<Image> addedImages) {
        addedImages.forEach(addedImage -> {
            images.add(addedImage);
            addedImage.initBoard(this);
        });
    }

    private void deleteImages(List<Image> deletedImages) {
        deletedImages.forEach(deletedImage -> this.images.remove(deletedImage));
    }

    private ImageUpdatedResult findImageUpdatedResult(List<MultipartFile> addedImageFiles,
                                                      List<Integer> deletedImageIds) {
        List<Image> addedImages = convertImageFilesToImages(addedImageFiles);
        List<Image> deletedImages = convertImageIdsToImages(deletedImageIds);
        return new ImageUpdatedResult(addedImageFiles, addedImages, deletedImages);
    }

    private List<Image> convertImageIdsToImages(List<Integer> imageIds) {
        return imageIds.stream()
                .map(this::convertImageIdToImage)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
    }

    private Optional<Image> convertImageIdToImage(int id) {
        return this.images.stream()
                .filter(image -> image.isSameImageId(id))
                .findAny();
    }

    private List<Image> convertImageFilesToImages(List<MultipartFile> imageFiles) {
        return imageFiles.stream()
                .map(imageFile -> new Image(imageFile.getOriginalFilename()))
                .collect(toList());
    }

    public boolean isReported() {
        return this.reported;
    }

    public void increaseLikeCount() {
        this.liked += 1;
    }

    public void decreaseLikeCount() {
        this.liked -= 1;
    }

    public void increaseFavoriteCount() {
        this.favorited += 1;
    }

    public void decreaseFavoriteCount() {
        this.favorited -= 1;
    }

    public void setStatusIsBeingReported() {
        this.reported = true;
    }

    public void unReportedBoard() {
        this.reported = false;
    }

    @Getter
    @AllArgsConstructor
    public static class ImageUpdatedResult {
        private List<MultipartFile> addedImageFiles;
        private List<Image> addedImages;
        private List<Image> deletedImages;
    }
}
