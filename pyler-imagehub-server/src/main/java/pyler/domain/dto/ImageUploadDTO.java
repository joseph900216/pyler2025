package pyler.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageUploadDTO {

    private MultipartFile file;
    private String imageName;
    private String imageDescription;
    private String imageExt;
    private BigInteger imageSize;
}
