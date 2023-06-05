package hello.hellospring.service;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
public class GptService {
    private OpenAiService service;
    private ChatCompletionRequest chatCompletionRequest;
    List<ChatMessage> messages = new ArrayList<>();

    public void createGptService(){
        service = new OpenAiService("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",1800);
        chatCompletionRequest = ChatCompletionRequest.builder()
                .messages(messages)
                .model("gpt-3.5-turbo")
                .build();
    }

    public String createChatCompletion(String text, String role)
    {
        ChatMessage message = new ChatMessage();
        message.setRole(role);
        message.setContent(text);
        messages.add(message);
        return service.createChatCompletion(chatCompletionRequest).getChoices().get(0).getMessage().getContent();
    }

    public void initiateChatCompletion()
    {
        messages.clear();
    }

    public String generateSummary(String[] text){
        initiateChatCompletion();
        if (text.length == 1)
            return createChatCompletion("summarize this:".concat(text[0]), "user");
        else {
            int len  = text.length;
            String summary_result = "";
            for (int i = 0;i<len;i++)
            {
                initiateChatCompletion();
                summary_result = summary_result.concat(createChatCompletion("summarize this:".concat(text[i]), "user"));
                System.out.println(i);
                System.out.println(summary_result);
            }
            return createChatCompletion("summarize this :".concat(summary_result),"user");
        }
    }
    public String generateExercise(String[] text, int quiz_cnt, String difficulty, String quiz_type){
        initiateChatCompletion();//전에 내용을 초기화 시키고 새로운 세션으로 시작할지?
        //Please make 5 questions about this article so that is multiple choice
        String quiz_prompt = "make " + Integer.toString(quiz_cnt) + " questions about this article so that is " + quiz_type + ". and make its difficulty level is " + difficulty + ". : ";
        if (text.length == 1)
            return createChatCompletion(quiz_prompt.concat(text[0]), "user");
        else {
            int len  = text.length;
            String summary_result = "";
            for (int i = 0;i<len;i++)
            {
                initiateChatCompletion();
                summary_result = summary_result.concat(createChatCompletion("summarize this:".concat(text[i]), "user"));
                System.out.println(i);
                //System.out.println(summary_result);
            }
            return createChatCompletion(quiz_prompt.concat(text[0]), "user");
        }
    }

    public String generateTranslation(String[] text, String from_lan, String to_lan)
    {
        initiateChatCompletion();//전에 내용을 초기화 시키고 새로운 세션으로 시작할지?
        String translation_prompt = "translate this article that is written by " + from_lan + "to" + to_lan + ":";
        if (text.length == 1)
            return createChatCompletion(translation_prompt.concat(text[0]), "user");
        else{
            int len  = text.length;
            String summary_result = "";
            for (int i = 0;i<len;i++)
            {
                initiateChatCompletion();
                summary_result = summary_result.concat(createChatCompletion(translation_prompt.concat(text[i]), "user"));
                System.out.println(i);
                //System.out.println(summary_result);
            }
            return summary_result;
        }
    }

    public String pdfToText(String file_url) throws IOException {
        File file = new File(file_url);
        PDDocument document;
        document = PDDocument.load(file);
        PDFTextStripper s = new PDFTextStripper();
        String content = s.getText(document);
        document.close();
        return content;
    }

    public String wordToText(String file_url) throws IOException {
        String content = "";
        File file = new File(file_url);
        InputStream is = new FileInputStream(file);
        XWPFDocument document = new XWPFDocument(is);
        //List<XWPFParagraph> content = document.getParagraphs();
        for(XWPFParagraph paragraph: document.getParagraphs())
        {
            content = content + paragraph.getText();
        }
        document.close();
        return content;
    }

    public String pptToText(String file_url) throws IOException {
        String content = "";
        XMLSlideShow document = new XMLSlideShow(new FileInputStream(file_url));
        for(XSLFSlide slide: document.getSlides()){
            XSLFTextShape[] shapes = slide.getPlaceholders();
            for (XSLFTextShape textShape : shapes){
                content = content + textShape.getText();
            }
        }
        return content;
    }
    public String[] longTextSplit(String content, int divCnt)
    {
        int div = divCnt;
        String[] split;
        String[] word = content.split(" |\n|\t");
        int token = word.length;
        System.out.println(token);
        if (token < div)
        {
            String[] result = new String[1];
            result[0] = content;
            split = result;
        }
        else
        {
            int cnt = token / div;
            if (cnt % div != 0)
                cnt = cnt+1;
            String[] result = new String[cnt];
            if (cnt % div != 0)
                cnt = cnt-1;
            int i;
            for (i = 0;i<cnt;i++)
            {
                String temp[] = new String[div];
                for(int j  = i*div;j<(i+1)*div;j++)
                {
                    temp[j - (i*div)] = word[j];
                }
                result[i] = String.join(" ", temp);
            }
            if (cnt % div != 0)
            {
                String temp[] = new String[word.length - i*div];
                for(int j  = i*div;j< word.length;j++)
                {
                    temp[j - (i*div)] = word[j];
                }
                result[i] = String.join(" ", temp);
            }
            split = result;
        }
        //System.out.print(word.length);
        return split;
    }


//

//    public String gptTest(String text, String role)
//    {
//        List<ChatMessage> messages = new ArrayList<>();
//        ChatMessage message = new ChatMessage();
//        message.setRole(role);
//        message.setContent(text);
//        messages.add(message);
//        OpenAiService service = new OpenAiService("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",1800);
//        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
//                .messages(messages)
//                .model("gpt-3.5-turbo")
//                .build();
//        List<ChatCompletionChoice> answer = service.createChatCompletion(chatCompletionRequest).getChoices();
//        return answer.get(0).getMessage().getContent();
//        //service.createChatCompletion(chatCompletionRequest).getChoices().forEach(System.out::println);
//    }




//    public String generate_summary2(String[] text) throws Exception {
//        if (text.length == 1)
//            return chatGPT("summarize this :".concat(text[0]));
//        else
//        {
//            int len  = text.length;
//            String summary_result = "";
//            for (int i = 0;i<len;i++)
//            {
//                summary_result = summary_result.concat(chatGPT("summarize this in 100 words in korean:".concat(text[i])));
//                System.out.println(i);
//                System.out.println(summary_result);
//            }
//            return chatGPT("summarize this :".concat(summary_result));
//        }
//    }







   // public String sendQuestion(String text) {
//        OpenAiService service = new OpenAiService("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",1800);
//        CompletionRequest completionRequest = CompletionRequest.builder()
//                .prompt(text)
//                .model("text-davinci-003")
//                .echo(false)
//                .maxTokens(2000)
//                .temperature(0.9)
//                .topP(1.0)
//                .frequencyPenalty(0.0)
//                .presencePenalty(0.0)
//                .build();
//        //service.createCompletion(completionRequest).getChoices().forEach(System.out::println);
//        List answer;
//        answer = service.createCompletion(completionRequest).getChoices();
//        String response = answer.toString();
//        System.out.print(response);
//        return response;
//        //return service.createCompletion(completionRequest).getChoices().toString();
//        //getChoices().toString();
//    }
//    public String generate_summary(String text) {
//        OpenAiService service = new OpenAiService("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",1800);
//        String request = "summarize this in korea : ";
//        request.concat(text);
//        CompletionRequest completionRequest = CompletionRequest.builder()
//                .prompt(text)
//                .model("text-davinci-003")
//                .echo(false)
//                .maxTokens(2000)
//                .temperature(0.9)
//                .topP(1.0)
//                .frequencyPenalty(0.0)
//                .presencePenalty(0.0)
//                .build();
//        List answer;
//        answer = service.createCompletion(completionRequest).getChoices();
//        String response = answer.toString();
//        //System.out.print(response);
//        return response;
//    }
//    public String generate_exercise(String text) {
//        OpenAiService service = new OpenAiService("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",1800);
//        String request = "Make 5 exercises about the text:";
//        request.concat(text);
//        CompletionRequest completionRequest = CompletionRequest.builder()
//                .prompt(text)
//                .model("text-davinci-003")
//                .echo(false)
//                .maxTokens(2000)
//                .temperature(0.9)
//                .topP(1.0)
//                .frequencyPenalty(0.0)
//                .presencePenalty(0.0)
//                .build();
//        List answer;
//        answer = service.createCompletion(completionRequest).getChoices();
//        String response = answer.toString();
//        //System.out.print(response);
//        return response;
//    }
//
//    public String generate_translation(String text) {
//        OpenAiService service = new OpenAiService("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",1800);
//        String request = "Translate this in Korean:";
//        request.concat(text);
//        CompletionRequest completionRequest = CompletionRequest.builder()
//                .prompt("user")
//                .model("text-davinci-003")
//                .echo(false)
//                .maxTokens(2000)
//                .temperature(0.9)
//                .topP(1.0)
//                .frequencyPenalty(0.0)
//                .presencePenalty(0.0)
//                .build();
//        List answer;
//        answer = service.createCompletion(completionRequest).getChoices();
//        String response = answer.toString();
//        //System.out.print(response);
//        return response;
//    }
//    public String chatGPT(String text) throws Exception {
//        String url = "https://api.openai.com/v1/completions";
//        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
//
//        con.setRequestMethod("POST");
//        con.setRequestProperty("Content-Type", "application/json");
//        con.setRequestProperty("Authorization", "Bearer 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");
//
//        JSONObject data = new JSONObject();
//        data.put("model", "text-davinci-003");
//        data.put("prompt", text);
//        data.put("max_tokens", 2000);
//        data.put("temperature", 1.0);
//
//        con.setDoOutput(true);
//        con.getOutputStream().write(data.toString().getBytes());
//
//        String output = new BufferedReader(new InputStreamReader(con.getInputStream())).lines()
//                .reduce((a, b) -> a + b).get();
//        //System.out.println(output);
//        String response = new JSONObject(output).getJSONArray("choices").getJSONObject(0).getString("text");
//        return response;
//        //System.out.println(new JSONObject(output).getJSONArray("choices").getJSONObject(0).getString("text"));
//    }

}
