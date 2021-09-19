package toyProject.springweb;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import toyProject.springweb.domain.Board;
import toyProject.springweb.domain.Reply;
import toyProject.springweb.repository.BoardRepository;
import toyProject.springweb.repository.ReplyRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Commit
public class ReplyTest {
    Logger logger = (Logger) LoggerFactory.getLogger(getClass());

    @Autowired
    ReplyRepository repository;

    @Autowired
    BoardRepository boardRepository;

    /**
     * insertReplyDummies()
     * 10개의 댓글을 데이터 베이스에 입력
     */
    @Test
    public void insertReplyDummies(){

        Long[] arr = {303L, 300L, 296L};

        Arrays.stream(arr).forEach(num ->{

            Board board = new Board();
            board.setBoardSeq(num);

            IntStream.range(0, 10).forEach(i -> {

                Reply reply = new Reply();
                reply.setReplyContent("REPLY" + i);
                reply.setReplyWriter("Writer2" + i);
                reply.setBoard(board);

                repository.save(reply);
            });
        });
    }

    /**
     * insertReplyTest()
     * 댓글을 데이터 베이스에 입력 후 DB에 데이터가 입력한 데이터와 일치하는지 확인
     */
    @Transactional
    @Test
    public void  insertReplyTest(){
        Board board = new Board();
        board.setTitle("test Title");
        board.setWriter("test Writer");
        board.setContent("test Content");

        Reply reply = new Reply();
        reply.setReplyContent("test Reply");
        reply.setReplyWriter("test Writer2");

        List<Reply> replies = new ArrayList<Reply>();
        replies.add(reply);

        board.setReplies(replies);

        Board boardObj = boardRepository.save(board);

        Optional<Board> result = boardRepository.findById(boardObj.getBoardSeq());
        List<Reply> resultRepliesList = result.get().getReplies();
        Reply resultReply = resultRepliesList.get(0);

        Assertions.assertThat(reply)
                .usingRecursiveComparison()
                .ignoringFields(
                        "replySeq"
                        ,"regDate"
                        ,"updateDate"
                        ,"roleName"
                        ,"board"
                ) .isEqualTo(resultReply);
    }



}
