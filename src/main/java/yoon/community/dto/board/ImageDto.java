package yoon.community.dto.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import yoon.community.domain.board.Image;

@Data
@AllArgsConstructor
public class ImageDto {
    private int id;
    private String originName;
    private String uniqueName;

    public static ImageDto toDto(Image image) {
        return new ImageDto(image.getId(), image.getOriginName(), image.getUniqueName());
    }
}
