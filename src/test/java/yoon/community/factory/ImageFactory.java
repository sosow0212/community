package yoon.community.factory;

import org.springframework.test.util.ReflectionTestUtils;
import yoon.community.domain.board.Image;

public class ImageFactory {

    public static Image createImage() {
        return Image.from("origin_filename.jpg");
    }

    public static Image createImageWithOriginName(String originName) {
        return Image.from(originName);
    }

    public static Image createImageWithIdAndOriginName(Long id, String originName) {
        Image image = Image.from(originName);
        ReflectionTestUtils.setField(image, "id", id);
        return image;
    }
}
