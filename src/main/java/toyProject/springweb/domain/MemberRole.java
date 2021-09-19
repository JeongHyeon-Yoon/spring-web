package toyProject.springweb.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import toyProject.springweb.domain.Roles;

import javax.persistence.*;

/**
 * MemberRole Class
 * 회원 권한 관련 VO
 */

@Getter
@Setter
@ToString
@Entity
@Table(name = "tbl_member_roles")
@EqualsAndHashCode(of = "roleSeq")
public class MemberRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleSeq;

    @Enumerated(EnumType.STRING)
    private Roles roleName;


}
