package yoon.community.entity.board;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;
import yoon.community.entity.common.EntityDate;
import yoon.community.exception.UnsupportedImageFormatException;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

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

    private final static String supportedExtension[] = {"jpg", "jpeg", "gif", "bmp", "png"};

    public Image(String originName) {
        // 세팅
        this.originName = originName;
        this.uniqueName = generateUniqueName(extractExtension(originName));
    }

    public void initBoard(Board board) {
        if(this.board == null) {
            this.board = board;
        }
    }

    private String generateUniqueName(String extension) {
        return UUID.randomUUID().toString() + "." + extension;
    }

    private String extractExtension(String originName) {
        try {
            String ext = originName.substring(originName.lastIndexOf(".") + 1);
            if(isSupportedFormat(ext)) return ext;
        } catch (StringIndexOutOfBoundsException e) { }
        throw new UnsupportedImageFormatException();
    }

    private boolean isSupportedFormat(String ext) {
        return Arrays.stream(supportedExtension).anyMatch(e -> e.equalsIgnoreCase(ext));
    }
}
