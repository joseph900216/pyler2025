package pyler.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import pyler.domain.entity.BaseEntity;
import pyler.enums.CategoryEnum;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "CATEGORY")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "CATEGORY_NAME")
    private CategoryEnum categoryName;

    public CategoryEnum getName() {
        return categoryName;
    }

}
