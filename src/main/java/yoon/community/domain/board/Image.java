package yoon.community.domain.board;

import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import yoon.community.domain.common.EntityDate;
import yoon.community.exception.UnsupportedImageFormatException;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
public class Image extends EntityDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String uniqueName;

    @Column(nullable = false)
    private String originName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Board board;

    private final static List<String> supportedExtensions = List.of("jpg", "jpeg", "gif", "bmp", "png");

    public Image(String originName) {
        this.originName = originName;
        this.uniqueName = generateUniqueName(extractExtension(originName));
    }

    public void initBoard(Board board) {
        if (this.board == null) {
            this.board = board;
        }
    }

    private String generateUniqueName(String extension) {
        return UUID.randomUUID() + "." + extension;
    }

    private String extractExtension(String originName) {
        String extension = originName.substring(originName.lastIndexOf(".") + 1);

        if (isSupportedFormat(extension)) {
            return extension;
        }

        throw new UnsupportedImageFormatException();
    }

    private boolean isSupportedFormat(String extension) {
        return supportedExtensions.stream()
                .anyMatch(supportedExtension -> supportedExtension.equalsIgnoreCase(extension));
    }

    public boolean isSameImageId(int id) {
        return this.getId() == id;
    }
}
