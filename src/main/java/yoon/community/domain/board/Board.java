package yoon.community.domain.board;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.web.multipart.MultipartFile;
import yoon.community.domain.category.Category;
import yoon.community.domain.common.EntityDate;
import yoon.community.domain.member.Member;
import yoon.community.dto.board.BoardUpdateRequest;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

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

    public Board(final String title,
                 final String content,
                 final Member member,
                 final Category category,
                 final List<Image> images) {
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

    public ImageUpdatedResult update(final BoardUpdateRequest req) {
        this.title = req.getTitle();
        this.content = req.getContent();

        ImageUpdatedResult result = findImageUpdatedResult(req.getAddedImages(), req.getDeletedImages());
        addImages(result.getAddedImages());
        deleteImages(result.getDeletedImages());
        return result;
    }

    private void addImages(final List<Image> addedImages) {
        addedImages.forEach(addedImage -> {
            images.add(addedImage);
            addedImage.initBoard(this);
        });
    }

    private void deleteImages(final List<Image> deletedImages) {
        deletedImages.forEach(deletedImage -> this.images.remove(deletedImage));
    }

    private ImageUpdatedResult findImageUpdatedResult(final List<MultipartFile> addedImageFiles,
                                                      final List<Integer> deletedImageIds) {
        List<Image> addedImages = convertImageFilesToImages(addedImageFiles);
        List<Image> deletedImages = convertImageIdsToImages(deletedImageIds);
        return new ImageUpdatedResult(addedImageFiles, addedImages, deletedImages);
    }

    private List<Image> convertImageIdsToImages(final List<Integer> imageIds) {
        return imageIds.stream()
                .map(this::convertImageIdToImage)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
    }

    private Optional<Image> convertImageIdToImage(final int id) {
        return this.images.stream()
                .filter(image -> image.isSameImageId(id))
                .findAny();
    }

    private List<Image> convertImageFilesToImages(final List<MultipartFile> imageFiles) {
        return imageFiles.stream()
                .map(imageFile -> Image.from(imageFile.getOriginalFilename()))
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

    public void makeStatusReported() {
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
