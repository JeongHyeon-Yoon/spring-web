package toyProject.springweb.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;
import toyProject.springweb.domain.Board;
import toyProject.springweb.domain.UploadFile;
import toyProject.springweb.file.FileStore;
import toyProject.springweb.repository.CustomCrudRepository;
import toyProject.springweb.repository.UploadFileRepository;
import toyProject.springweb.vo.PageFactory;
import toyProject.springweb.vo.PageVO;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board/")
public class BoardController {

    @Value("${file.dir}")
    private String fileDir;

    private final FileStore fileStore;

    private final CustomCrudRepository repository;
    private final UploadFileRepository uploadFileRepository;

    @GetMapping("/list")
    public void  list(PageVO pageVO, Model model){
        log.info("Load BoardController.list()");

        Pageable pageable = pageVO.makePageable(0, "boardSeq");
        log.info("pageVO : " + pageVO);

        Page<Object[]> result = repository.getCustomPage(pageVO.getType(), pageVO.getKeyword(), pageable);
        log.info("result : "+result);

        log.info("TOTAL PAGE NUMBER: " + result.getTotalPages());

        model.addAttribute("result", new PageFactory<>(result));
    }

    @GetMapping("/write")
    public void writePage(@ModelAttribute("board") Board board ){
        log.info("Load BoardController.writePage()");

    }

    @PostMapping("/write")
    public String write(@ModelAttribute BoardForm boardForm ,RedirectAttributes redirectAttributes) throws IOException {

        log.info("Load BoardController.write()");


        List<UploadFile> storeFiles = fileStore.storeFiles(boardForm.getFiles());

        Board board = new Board();
        board.setTitle(boardForm.getTitle());
        board.setContent(boardForm.getContent());
        board.setWriter(boardForm.getWriter());
        board.setFiles(storeFiles);

        repository.save(board);
        redirectAttributes.addFlashAttribute("msg", "success");

        return "redirect:/board/list";
    }


    @GetMapping("/view")
    public void view(Long boardSeq, @ModelAttribute("pageVO") PageVO pageVO, Model model){

        log.info("Load BoardController.view()");
        log.info("BoardSeq: "+ boardSeq);

        repository.findById(boardSeq).ifPresent(board ->{

            model.addAttribute("board", board);
        } );

    }

    @GetMapping("/files/{fileSeq}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileSeq) throws MalformedURLException {
        log.info("Load BoardController.downloadFile()");
        log.info("fileSeq: "+ fileSeq);
        Optional<UploadFile> uploadFile = uploadFileRepository.findById(fileSeq);

        String storeFileName = uploadFile.get().getStoreFileName();
        String originFileName = uploadFile.get().getOriginFileName();

        UrlResource resource = new UrlResource("file:" + fileStore.getFullPath(storeFileName));

        log.info("downloadFileName={}", originFileName);

        String encodedUploadFileName = UriUtils.encode(originFileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }


    @Secured(value={"ROLE_USER","ROLE_MANAGER","ROLE_ADMIN"})
    @GetMapping("/edit")
    public void editPage(Long boardSeq, @ModelAttribute("pageVO") PageVO pageVO, Model model){

        log.info("Load BoardController.editPage()");
        log.info("edit boardSeq: "+ boardSeq);

        repository.findById(boardSeq).ifPresent(board -> model.addAttribute("board", board));
    }

    @Secured(value={"ROLE_USER","ROLE_MANAGER","ROLE_ADMIN"})
    @PostMapping("/edit")
    public String edit(@ModelAttribute BoardForm boardForm, Long boardSeq, PageVO pageVO, RedirectAttributes redirectAttributes ) throws IOException {

        log.info("BoardController.edit()");
        log.info("boardSeq: " + boardSeq);
        log.info("boardForm: " + boardForm);


        repository.findById(boardSeq).ifPresent( origin ->{

            origin.setTitle(boardForm.getTitle());
            origin.setContent(boardForm.getContent());

            if(boardForm.getFiles() != null){
                List<UploadFile> storeFiles = null;
                try {
                    storeFiles = fileStore.storeFiles(boardForm.getFiles());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                origin.setFiles(storeFiles);
            }


            repository.save(origin);
            redirectAttributes.addFlashAttribute("msg", "success");
            redirectAttributes.addAttribute("boardSeq", origin.getBoardSeq());
        });

        //페이징과 검색했던 결과로 이동하는 경우
        redirectAttributes.addAttribute("page", pageVO.getPage());
        redirectAttributes.addAttribute("size", pageVO.getSize());
        redirectAttributes.addAttribute("type", pageVO.getType());
        redirectAttributes.addAttribute("keyword", pageVO.getKeyword());

        return "redirect:/board/view";
    }


    @Secured(value={"ROLE_USER","ROLE_MANAGER","ROLE_ADMIN"})
    @PostMapping("/delete")
    public String delete(Long boardSeq, PageVO pageVO, RedirectAttributes redirectAttributes ){

        log.info("Load BoardController.delete()");
        log.info("DELETE BoardSeq: " + boardSeq);

        repository.deleteById(boardSeq);

        redirectAttributes.addFlashAttribute("msg", "success");

        //페이징과 검색했던 결과로 이동하는 경우
        redirectAttributes.addAttribute("page", pageVO.getPage());
        redirectAttributes.addAttribute("size", pageVO.getSize());
        redirectAttributes.addAttribute("type", pageVO.getType());
        redirectAttributes.addAttribute("keyword", pageVO.getKeyword());

        return "redirect:/board/list";
    }

}
