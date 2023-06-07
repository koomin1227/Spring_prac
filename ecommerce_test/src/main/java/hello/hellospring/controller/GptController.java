package hello.hellospring.controller;

import hello.hellospring.domain.Member;
import hello.hellospring.dto.MessageDTO;
import hello.hellospring.service.GptService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Controller
public class GptController {
    GptService gptService = new GptService();
    String summary;
    String quest;
    @GetMapping("/gpt")
    public String gpt() {
        gptService.createGptService();
        return "gpt";
    }
    @ResponseBody
    @RequestMapping(value = "/summary",method = RequestMethod.POST)
    public HashMap<String, Object> summaryDoc(@RequestBody HashMap<String, Object> map) throws Exception{
        gptService.initiateChatCompletion();
        String text = gptService.pdfToText(map.get("file_path").toString());
        String[] origin_text = gptService.longTextSplit(text,2000);
        summary = gptService.generateSummary(origin_text);
        System.out.println(summary);
        map.put("summary", summary);
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/quest",method = RequestMethod.POST)
    public HashMap<String, Object> chatQna(@RequestBody HashMap<String, Object> map){
        System.out.println(map.get("quest"));
        String reponse = gptService.createChatCompletion(map.get("quest").toString(),"user");
        System.out.print(reponse);
        map.put("response",reponse);
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/exercise",method = RequestMethod.POST)
    public HashMap<String, Object> makeQuiz(@RequestBody HashMap<String, Object> map){
        System.out.println(map);
        String[] text = new String[1];
        text[0] = summary;
        String[][] quiz = gptService.makeQuiz(text, Integer.parseInt(map.get("quiz_cnt").toString()),map.get("difficulty").toString(),Integer.parseInt(map.get("quiz_type").toString()));
        System.out.println(quiz);
        map = gptService.listToJson(map, quiz, Integer.parseInt(map.get("quiz_cnt").toString()), Integer.parseInt(map.get("quiz_type").toString()));
        return map;
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
//    @GetMapping(value = "/gpt_answer")
//    public String home_login(Model model) {
//        model.addAttribute("answer", test);
//        model.addAttribute("request", quest);
//        return "/gpt";
//    }
}
