package toyProject.springweb.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import toyProject.springweb.domain.Board;
import toyProject.springweb.domain.Reply;
import toyProject.springweb.repository.BoardRepository;
import toyProject.springweb.repository.ReplyRepository;

import java.util.List;

@RestController
@RequestMapping("/reply/*")
@Slf4j

public class ReplyController {

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private BoardRepository boardRepository;


    @GetMapping("/{boardSeq}")
    public ResponseEntity<List<Reply>> getReplies(@PathVariable("boardSeq") Long boardSeq) {
        log.info("Load ReplyController.getReplies()");

        Board board = new Board();
        board.setBoardSeq(boardSeq);
        return new ResponseEntity<>(getListByBoard(board), HttpStatus.OK);
    }



    @Secured(value={"ROLE_USER","ROLE_MANAGER","ROLE_ADMIN"})
    @Transactional
    @PostMapping("/{boardSeq}")
    public ResponseEntity<List<Reply>> addReply(@PathVariable("boardSeq") Long boardSeq, @RequestBody Reply reply) {
        log.info("Load ReplyController.addReply()");
        log.info("boardSeq : " + boardSeq);
        log.info("Reply : " + reply);

        Board board = new Board();
        board.setBoardSeq(boardSeq);

        reply.setBoard(board);

        replyRepository.save(reply);

        return new ResponseEntity<>(getListByBoard(board), HttpStatus.CREATED);
    }

    @Secured(value={"ROLE_USER","ROLE_MANAGER","ROLE_ADMIN"})
    @Transactional
    @DeleteMapping("/{boardSeq}/{replySeq}")
    public ResponseEntity<List<Reply>> remove(@PathVariable("boardSeq") Long boardSeq, @PathVariable("replySeq") Long replySeq ) {
        log.info("Load ReplyController.remove()");
        log.info("boardSeq : " + boardSeq);
        log.info("replySeq : " + replySeq);

        replyRepository.deleteById(replySeq);

        Board board = new Board();
        board.setBoardSeq(boardSeq);

        return new ResponseEntity<>(getListByBoard(board), HttpStatus.OK);
    }

    @Secured(value={"ROLE_USER","ROLE_MANAGER","ROLE_ADMIN"})
    @Transactional
    @PutMapping("/{boardSeq}")
    public ResponseEntity<List<Reply>> edit(@PathVariable("boardSeq") Long boardSeq, @RequestBody Reply reply ) {
        log.info("Load ReplyController.edit()");
        log.info("boardSeq : " + boardSeq);

        replyRepository.findById(reply.getReplySeq()).ifPresent(origin -> {
            origin.setReplyContent(reply.getReplyContent());
            origin.setReplyWriter(reply.getReplyWriter());

            replyRepository.save(origin);
        });

        Board board = new Board();
        board.setBoardSeq(boardSeq);

        return new ResponseEntity<>(getListByBoard(board), HttpStatus.OK);
    }
    private List<Reply> getListByBoard( Board board ) throws RuntimeException{
        log.info("Load ReplyController.getListByBoard()");
        return replyRepository.getRepliesOfBoard(board);
    }

}

