package pyler.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import pyler.converter.UserEnumConverter;
import pyler.enums.UserEnum;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "USER_ROLE")
public class UserRoleEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_ID")
    private long userId;

    @Convert(converter = UserEnumConverter.class)
    @Column(name = "USER_ROLE_TYPE")
    private UserEnum userRoleType;

    @Column(name = "CREATOR_ID")
    private long creatorId;

    @Column(name = "UPDATOR_ID")
    private long updatorId;

}
