package toyProject.springweb;

import org.assertj.core.api.Assertions;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import toyProject.springweb.domain.Member;
import toyProject.springweb.domain.Roles;
import toyProject.springweb.repository.MemberRepository;
import toyProject.springweb.domain.MemberRole;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest

public class MemberTest {
    Logger logger = (Logger) LoggerFactory.getLogger(getClass());

    @Autowired
    private MemberRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * insertMemberDummies()
     * 100개의 사용자 데이터를 데이터 베이스에 입력
     */

    @Test
    public void insertMemberDummies(){

        for(int i = 0; i < 100; i++){
            Member member = new Member();
            member.setUserId("user" + i);
            member.setUserPwd(passwordEncoder.encode("pw" + i));
            member.setUserNm("유저" + i);

            MemberRole role = new MemberRole();
            if( i < 70 ){
                role.setRoleName(Roles.USER);
            }else if( i < 90 ){
                role.setRoleName(Roles.MANAGER);
            }else{
                role.setRoleName(Roles.ADMIN);
            }
            member.setRoles(Arrays.asList(role));

            repository.save(member);
        }
    }

    /**
     * saveMemberTest()
     * 사용자 저장 후 기존 값과 DB에서 가져온 값이 같은지 확인
     */
    @Transactional
    @Test
    public void saveMemberTest() {
        //given
        Member originVal = new Member();
        originVal.setUserId("testUser87");
        originVal.setUserPwd(passwordEncoder.encode("pw87"));
        originVal.setUserNm("유저87");

        MemberRole role = new MemberRole();
        role.setRoleName(Roles.MANAGER);
        originVal.setRoles(Arrays.asList(role));

        //when
        repository.save(originVal);

        //then
        Member result = repository.findByUserId("testUser87");

        Assertions.assertThat(originVal)
                .usingRecursiveComparison()
                .ignoringFields(
                        "userSeq"
                        ,"userPwd"
                        ,"regDate"
                        ,"roleName"
                        ,"updateDate"
                ) .isEqualTo(result);

        Assertions.assertThat(originVal.getRoles()).isEqualTo(result.getRoles());
        Assertions.assertThat(passwordEncoder.matches( "pw87", result.getUserPwd() )).isTrue();
    }


}
