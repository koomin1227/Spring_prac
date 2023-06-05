package hello.hellospring.controller;

import hello.hellospring.domain.Member;
import hello.hellospring.dto.MessageDTO;
import hello.hellospring.service.GptService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class GptController {
    GptService gptService = new GptService();
    String test;
    String quest;
    @GetMapping("/gpt")
    public String gpt() {
        gptService.createGptService();
        return "gpt";
    }
    @RequestMapping(value = "/dataSend",method = RequestMethod.POST)
    public String dataSend(Model model, MessageDTO dto) throws Exception {
        GptService gptService = new GptService();
        gptService.createGptService();
        System.out.print(dto.getResult());
        String test = gptService.createChatCompletion(dto.getResult(),"user");
        System.out.print(test);
        model.addAttribute("answer",test);
        model.addAttribute("request",dto.getResult()+"/ this is the value sent by the server234");
//        quest = dto.getResult();
//        System.out.print(quest);
////        test = gptService.createChatCompletion(quest,"user");
////        System.out.print(test);
//        model.addAttribute("answer", "sibal");
////        model.addAttribute("request", "아이폰");
        return "gpt :: #resultDiv";
//        return "redirect:/gpt_answer";
    }

//    @PostMapping("/gpt/quest")
//    public String createQuestion(GptForm form, Model model) throws Exception {
//        quest = form.getQuest();
//        System.out.print(quest);
//        test = gptService.createChatCompletion(quest,"user");
//        System.out.print(test);
////        model.addAttribute("answer", test);
//        model.addAttribute("request", "아이폰");
//        //form.setanswer(gptService.chatGPT(quest));
//        //form.setanswer(gptService.sendQuestion(quest));
//        return "redirect:/gpt_answer";
//    }
    @GetMapping(value = "/gpt_answer")
    public String home_login(Model model) {
        model.addAttribute("answer", test);
        model.addAttribute("request", quest);
        return "/gpt";
    }
}
