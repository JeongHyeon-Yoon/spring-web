package toyProject.springweb.repository;

import org.springframework.data.repository.CrudRepository;
import toyProject.springweb.domain.Member;

public interface MemberRepository extends CrudRepository<Member, String> {

    public Member findByUserId(String userId);

}
