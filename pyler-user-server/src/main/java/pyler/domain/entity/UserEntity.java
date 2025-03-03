package pyler.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "USER")
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "USER_NAME")
    private String userEmail;

    @Column(name = "PASSWORD")
    private String passWord;

    @Column(name = "USER_ROLE")
    private int userRole;

    @Column(name = "IS_DEL")
    private boolean isDel;

}
