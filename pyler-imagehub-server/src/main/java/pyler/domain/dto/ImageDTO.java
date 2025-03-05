package pyler.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.pl.NIP;
import pyler.domain.entity.CategoryEntity;
import pyler.enums.CategoryEnum;

import java.time.LocalDateTime;
import java.util.Set;

public class ImageDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String description;
        private Set<CategoryEnum> category;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String fileName;
        private String originFileName;
        private String filePath;
        private String thumbnailPath;
        private Long fileSize;
        private String fileExt;
        private String contentType;
        private String description;
        private Set<CategoryEnum> categoryEnums;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateReq {
        private String description;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class delRes {
        private Long imageId;
    }



}
