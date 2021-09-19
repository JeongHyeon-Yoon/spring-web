package toyProject.springweb.controller;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class BoardForm {
    private String title;
    private String content;
    private String writer;
    private List<MultipartFile> files;
}
