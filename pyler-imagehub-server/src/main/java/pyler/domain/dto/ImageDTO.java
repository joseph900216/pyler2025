package pyler.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageDTO {

    private Long id;
    private String fileName;
    private String originFileName;
    private String filePath;
    private String thumbnailPath;
    private Long fileSize;
    private String contentType;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
