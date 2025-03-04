package pyler.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageUploadService{

    @Value("${file.uploadDir}")
    private String uploadDir;

    @Value("${file.thumbnailDir}")
    private String thumbnailDir;

    /**
     * Image File upload
     * @param multipartFile
     * @return
     * @throws IOException
     */
    public String imageUpload(MultipartFile multipartFile) throws IOException {

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String uniqueFileName = UUID.randomUUID() + "_" + fileName;

        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path targetPath = uploadPath.resolve(uniqueFileName);
        Files.copy(multipartFile.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return uniqueFileName;
    }

    /**
     * Image File Thumbnail
     * @param originFileName
     * @return
     * @throws IOException
     */
    public String createThumbnail(String originFileName) throws IOException {

        Path thumbnailPath= Paths.get(thumbnailDir);
        if(!Files.exists(thumbnailPath)) {
            Files.createDirectories(thumbnailPath);
        }

        String thumbnailName = "thumb_" + originFileName;
        File originFile = new File(uploadDir + File.separator + originFileName);
        File thumbnailFile = new File(thumbnailDir + File.separator + thumbnailName);

        Thumbnails.of(originFile)
                .size(100,100)
                .toFile(thumbnailFile);

        return thumbnailName;
    }

    /**
     * Image File Delete
     * @param fileName
     * @throws IOException
     */
    public void imageDelete(String fileName) throws IOException {
        Path filePath = Paths.get(uploadDir).resolve(fileName);
        Files.deleteIfExists(filePath);

        Path thumbnalPath = Paths.get(thumbnailDir).resolve("thumb_" + fileName);
        Files.deleteIfExists(thumbnalPath);
    }
}
