package toyProject.springweb.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import toyProject.springweb.domain.Member;
import toyProject.springweb.repository.MemberRepository;

@Slf4j
@Controller
@RequestMapping("/member/")
public class MemberController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MemberRepository repository;

    @GetMapping("/signup")
    public void signUpPage(){
        log.info("Load MemberController.signUpPage()");
    }

    @Transactional
    @PostMapping("/signup")
    public String signUp(@ModelAttribute("member") Member member){
        log.info("Load MemberController.signUp(member)");

        String encryptPwd = passwordEncoder.encode(member.getUserPwd());

        log.info("encryption Result : " + encryptPwd);

        member.setUserPwd(encryptPwd);

        repository.save(member);

        return "redirect:/login";
    }
}
