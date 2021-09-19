package toyProject.springweb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.assertj.core.api.Assertions;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import toyProject.springweb.domain.*;
import toyProject.springweb.repository.BoardRepository;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Commit
public class BoardTests {
    @Autowired
    BoardRepository repository;


    /**
     * insertBoardDummies()
     * 300개의 게시물을 데이터를 데이터 베이스에 입력
     */
    @Test
    public void insertBoardDummies(){
        IntStream.range(0, 300).forEach(i->{
            Board board = new Board();

            board.setTitle("Board Title" + i);
            board.setContent("Content" + i);
            board.setWriter("user"+ (i % 10));

            repository.save(board);
        });
    }


    /**
     * insertBoardTest()
     * 게시물을 데이터 베이스에 입력 후 DB에 데이터가 입력한 데이터와 일치하는지 확인
     */
    @Transactional
    @Test
    public void  insertBoardTest(){
        Board board = new Board();
        board.setTitle("test Title");
        board.setWriter("test Writer");
        board.setContent("test Content");

        Board boardObj = repository.save(board);

        Optional<Board> result = repository.findById(boardObj.getBoardSeq());
        Board resultBoard = result.get();


        Assertions.assertThat(board)
                .usingRecursiveComparison()
                .ignoringFields(
                        "boardSeq"
                        ,"regDate"
                        ,"updateDate"
                        ,"replies"
                        ,"files"
                ).isEqualTo(resultBoard);
    }

}
