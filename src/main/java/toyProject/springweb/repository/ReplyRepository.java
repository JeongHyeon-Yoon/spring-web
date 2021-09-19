package toyProject.springweb.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import toyProject.springweb.domain.Board;
import toyProject.springweb.domain.Reply;

import java.util.List;

public interface ReplyRepository extends CrudRepository<Reply, Long> {

    @Query("SELECT r FROM Reply r WHERE r.board = ?1 AND r.replySeq > 0 ORDER BY r.replySeq ASC")
    public List<Reply> getRepliesOfBoard(Board board);
}
