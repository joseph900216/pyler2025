package pyler.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "USER_USER")
public class UserUserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "USER_EMAIL")
    private String userEmail;

    @Column(name = "USER_PASSWORD")
    private String passWord;

    @Column(name = "IS_MASTER")
    private Boolean isMaster;

    @Column(name = "IS_ACTIVE")
    private Boolean isActive;

    @Column(name = "IS_DEL")
    private Boolean isDel;

}
