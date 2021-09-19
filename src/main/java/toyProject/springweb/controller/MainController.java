package toyProject.springweb.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * MainController
 * 메인 페이지 컨트롤러
 */
@Slf4j
@Controller
public class MainController {

    @GetMapping("/")
    public String mainPage(){
        log.info("Load MainController.mainPage() ");

        return "mainPage";
    }
}
