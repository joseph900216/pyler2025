package pyler.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
@RequiredArgsConstructor
public class FileFolderConfig {

    @Value("${file.uploadDir}")
    private String uploadDir;

    @Value("${file.thumbnailDir}")
    private String thumbnailDir;

    @PostConstruct
    public void init() {
        createDirectory(uploadDir);
        createDirectory(thumbnailDir);
    }

    private void createDirectory(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();  // 폴더가 없으면 생성
            if (created) {
                System.out.println("폴더 생성됨: " + path);
            } else {
                System.err.println("폴더 생성 실패: " + path);
            }
        }
    }
}
