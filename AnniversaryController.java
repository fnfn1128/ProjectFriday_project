package com.example.ProjectFriday.controller;

import com.example.ProjectFriday.dto.AnniversaryForm;
import com.example.ProjectFriday.entity.Anniversary;
import com.example.ProjectFriday.repository.AnniversaryRepository;
import com.example.ProjectFriday.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
public class AnniversaryController {

    @Autowired
    private AnniversaryRepository anniversaryRepository;

    @Autowired
    private MailService mailService;

    @GetMapping("/PF/new")
    public String newAnniversaryForm(){
        return "view/new";
    }

    @PostMapping("/PF/create")
    public String createAnniversary(AnniversaryForm form){
        Anniversary anniversary = form.toEntity();
         log.info(anniversary.toString());
         Anniversary saved = anniversaryRepository.save(anniversary);
         return "redirect:/anniversaries/" + saved.getId();
    }
    @GetMapping("/anniversaries/{id}")
    public String showAnniversafies(@PathVariable Long id, Model model){
        Anniversary anniversary = anniversaryRepository.findById(id).orElse(null);
        model.addAttribute("anniversary", anniversary);
        return "view/show";
    }
    @GetMapping("/anniversaries")
    public String listAnniversaries(Model model){
        Iterable<Anniversary> anniversaryList = anniversaryRepository.findAll();
        model.addAttribute("anniversaries", anniversaryList);
        return "view/list";
    }
    @GetMapping("/anniversaries/{id}/edit")
    public String editAnniversaryForm(@PathVariable Long id, Model model){
        Anniversary anniversary = anniversaryRepository.findById(id).orElse(null);
        model.addAttribute("anniversary", anniversary);
        return "view/edit";
    }
    @PostMapping("/anniversaries/update")
    public String updateAnniversary(AnniversaryForm form){
        Anniversary anniversary = form.toEntity();
        Anniversary target = anniversaryRepository.findById(anniversary.getId()).orElse(null);
        if (target != null){
            anniversaryRepository.save(anniversary);
        }
        return "redirect:/anniversaries/" + anniversary.getId();
    }
    @GetMapping("/anniversaries/{id}/delete")
    public String deleteAnniversary(@PathVariable Long id){
        Anniversary target = anniversaryRepository.findById(id).orElse(null);
        if (target != null){
            anniversaryRepository.delete(target);
        }
        return "redirect:/anniversaries";
    }

    @PostMapping("/anniversaries/sendEmailBatch")
    public String sendBatchEmail(@RequestParam String email,
                                 @RequestParam(name = "ids", required = false) List<Long> ids,
                                 Model model) {
        if (ids != null && !ids.isEmpty()) {
            List<Anniversary> selected = (List<Anniversary>) anniversaryRepository.findAllById(ids);
            StringBuilder text = new StringBuilder();
            text.append("[ProjectFriday] 기념일 알림\n\n");
            for (Anniversary ann : selected) {
                text.append("- 제목: ").append(ann.getTitle()).append("\n")
                        .append("  날짜: ").append(ann.getDate()).append("\n")
                        .append("  D-day: ").append(ann.getDayString()).append("\n");
                if (ann.getMemo() != null && !ann.getMemo().isEmpty()) {
                    text.append("  메모: ").append(ann.getMemo()).append("\n");
                }
                text.append("\n");
            }
            mailService.sendAnniversaryMail(email, "[ProjectFriday] 선택 기념일 모음", text.toString());
            model.addAttribute("message", "이메일이 성공적으로 전송되었습니다!");
        } else {
            model.addAttribute("message", "전송할 기념일을 선택하세요.");
        }
        // 메일 전송 후 다시 목록 보여주기
        Iterable<Anniversary> anniversaryList = anniversaryRepository.findAll();
        model.addAttribute("anniversaries", anniversaryList);
        return "view/list";

    }

}
