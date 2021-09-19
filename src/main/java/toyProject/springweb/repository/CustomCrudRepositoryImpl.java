package toyProject.springweb.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import toyProject.springweb.domain.Board;
import toyProject.springweb.domain.QBoard;
import toyProject.springweb.domain.QReply;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CustomCrudRepositoryImpl extends QuerydslRepositorySupport implements CustomBoard {

    public CustomCrudRepositoryImpl() {
        super(Board.class);
    }


    @Override
    public Page<Object[]> getCustomPage(String type, String keyword, Pageable page) {
        log.info("====================================");
        log.info("TYPE: " + type);
        log.info("KEYWORD: " + keyword);
        log.info("PAGE: " + page);
        log.info("====================================");

        QBoard qBoard = QBoard.board;
     //   QReply qReply = QReply.reply;

        JPQLQuery<Board> query = from(qBoard);

        JPQLQuery<Tuple> tuple = query.select(qBoard.boardSeq, qBoard.title, qBoard.count(), qBoard.writer, qBoard.regDate);
       // tuple.leftJoin(qReply);
      //  tuple.on( qBoard.boardSeq.eq(qReply.board.boardSeq) );
        tuple.where(qBoard.boardSeq.gt(0L));

        if(type != null){

            switch (type.toLowerCase()) {
                case "title":
                    tuple.where(qBoard.title.like("%" + keyword +"%"));
                    break;
                case "content":
                    tuple.where(qBoard.content.like("%" + keyword +"%"));
                    break;
                case "writer":
                    tuple.where(qBoard.writer.like("%" + keyword +"%"));
                    break;
            }
        }


        tuple.groupBy(qBoard.boardSeq);
        tuple.orderBy(qBoard.boardSeq.desc());

        tuple.offset(page.getOffset());
        tuple.limit(page.getPageSize());

        List<Tuple> list = tuple.fetch();

        List<Object[]> resultList = new ArrayList<>();

        list.forEach(t -> {
            resultList.add(t.toArray());
        });

        long total = tuple.fetchCount();

        return new PageImpl<>(resultList, page, total);
    }
}
