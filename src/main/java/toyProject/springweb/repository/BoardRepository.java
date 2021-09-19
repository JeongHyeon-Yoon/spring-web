package toyProject.springweb.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import toyProject.springweb.domain.Board;
import toyProject.springweb.domain.QBoard;

public interface BoardRepository extends CrudRepository<Board, Long>, QuerydslPredicateExecutor<Board> {

    public default Predicate makeQuery(String type, String keyword) {
        BooleanBuilder builder = new BooleanBuilder();

        QBoard board = QBoard.board;
        builder.and(board.boardSeq.gt(0));

        if (type == null) {
            return builder;
        }

        switch (type) {
            case "title":
                builder.and(board.title.like("%" + keyword));
                break;
            case "content":
                builder.and(board.content.like("%" + keyword + "%"));
                break;
            case "writer":
                builder.and(board.writer.like("%" + keyword + "%"));
                break;
        }
        return builder;
    }

    @Query("SELECT b.boardSeq, b.title, b.writer, b.regDate, count(r) FROM Board b " +
            " LEFT OUTER JOIN b.replies r WHERE b.boardSeq > 0 GROUP BY b")
    public Page<Object[]> getListWithAll(Pageable page);

    @Query("SELECT b.boardSeq, b.title, b.writer, b.regDate, count(r) FROM Board b " +
            " LEFT OUTER JOIN b.replies r WHERE b.title like %?1%  AND b.boardSeq > 0 GROUP BY b")
    public Page<Object[]> getListWithTitle(String keyword, Pageable page);

    @Query("SELECT b.boardSeq, b.title, b.writer, b.regDate, count(r) FROM Board b " +
            " LEFT OUTER JOIN b.replies r WHERE b.content like %?1% AND b.boardSeq > 0 GROUP BY b")
    public Page<Object[]> getListWithContent(String keyword, Pageable page);

    @Query("SELECT b.boardSeq, b.title, b.writer, b.regDate, count(r) FROM Board b " +
            " LEFT OUTER JOIN b.replies r WHERE b.writer like %?1% AND b.boardSeq > 0 GROUP BY b")
    public Page<Object[]> getListWithWriter(String keyword, Pageable page);

}
