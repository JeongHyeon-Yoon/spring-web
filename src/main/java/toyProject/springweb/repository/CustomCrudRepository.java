package toyProject.springweb.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.data.repository.CrudRepository;
import toyProject.springweb.domain.Board;
import toyProject.springweb.domain.QBoard;

public interface CustomCrudRepository extends CrudRepository<Board, Long>, CustomBoard {
    public default Predicate makePredicate(String type, String keyword){

        BooleanBuilder builder = new BooleanBuilder();

        QBoard board = QBoard.board;

        builder.and(board.boardSeq.gt(0));

        if(type == null){
            return builder;
        }

        switch(type){
            case "title":
                builder.and(board.title.like("%" + keyword +"%"));
                break;
            case "content":
                builder.and(board.content.like("%" + keyword +"%"));
                break;
            case "writer":
                builder.and(board.writer.like("%" + keyword +"%"));
                break;
        }

        return builder;
    }
}
