package hello.hellospring.service;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class GptServiceTest {
    GptService gptService;
    public String text;


//    @Test
//    public void pdfTest() throws Exception {
//        gptService = new GptService();
//        text = gptService.pdfToText("/Users/koomin/Library/Mobile Documents/com~apple~CloudDocs/지능기전/3학년 1학기/영상처리/11_Gamma.pdf");
//        System.out.print(text);
//        System.out.print(gptService.gptTest("이 글 좀 한글로 설명해줘 :".concat(text),"user"));
//        //System.out.print(gptService.generate_summary(text));
//        //System.out.print(gptService.generate_exercise(text));
//    }
//    @Test
//    public void gptTest() throws Exception {
//        gptService = new GptService();
//        System.out.print(gptService.gptTest("내 이름은 구민이야","user"));
//        //System.out.print(gptService.chatGPT("아이폰의 역사에 대해 알려줘"));
//
//    }

//    @Test
//    public void longTextTest() throws Exception {
//        gptService = new GptService();
//        String text = gptService.pdfToText("/Users/koomin/Library/Mobile Documents/com~apple~CloudDocs/지능기전/2학년 2학기/확률통계 및 프로그래밍/강의자료/4주차 전체 수업.pdf");
//        //System.out.println(text);
//        String[] split;
//        split = gptService.longTextSplit(text,1500);
//        System.out.println(split[0]);
//        System.out.print(gptService.gptTest("이 글에 대해 한글로 설명해줘:".concat(split[0]),"user"));
//        //System.out.print(gptService.chatGPT("summarize this :".concat(split[0])));
//
//        //System.out.println(gptService.generate_summary2(split));
//    }
    @Test
    public void gpt_initiate_test(){
        gptService = new GptService();
        gptService.createGptService();
        System.out.println(gptService.createChatCompletion("영어로 번역해줘 : 나는 오늘 학교에 간다.","user"));
        System.out.println(gptService.createChatCompletion("위 글 내용이 뭐였지?","user"));
        gptService.initiateChatCompletion();
        System.out.println(gptService.createChatCompletion("위 글 내용이 뭐였지?","user"));
    }

    @Test
    public void summary_test() throws IOException {
        gptService = new GptService();
        gptService.createGptService();
        //String text = gptService.pdfToText("/Users/koomin/Library/Mobile Documents/com~apple~CloudDocs/지능기전/2학년 2학기/확률통계 및 프로그래밍/강의자료/4주차 전체 수업.pdf");
        String text = gptService.pdfToText("/Users/koomin/Library/Mobile Documents/com~apple~CloudDocs/지능기전/3학년 1학기/컴퓨터 네트워크/Chapter_3_v8.02.pdf");

        //System.out.println(text);
        String[] split;
        split = gptService.longTextSplit(text,2000);
        System.out.println(split[0]);
        System.out.println(gptService.generateSummary(split));
        //System.out.println(gptService.createChatCompletion("summarize this".concat(split[0]),"user"));

    }
    @Test
    public void exercise_test() throws IOException {
        gptService = new GptService();
        gptService.createGptService();
        String text = gptService.pdfToText("/Users/koomin/Library/Mobile Documents/com~apple~CloudDocs/지능기전/2학년 2학기/확률통계 및 프로그래밍/강의자료/4주차 전체 수업.pdf");
        //String text = gptService.pdfToText("/Users/koomin/Library/Mobile Documents/com~apple~CloudDocs/지능기전/3학년 1학기/컴퓨터 네트워크/Chapter_3_v8.02.pdf");
        String[] split;
        split = gptService.longTextSplit(text, 2000);
        System.out.println(gptService.generateExercise(split, 3, "hard", "calculation"));
    }

    @Test
    public void translation_test() throws IOException{
        gptService = new GptService();
        gptService.createGptService();
        String text = gptService.pdfToText("/Users/koomin/Library/Mobile Documents/com~apple~CloudDocs/지능기전/2학년 2학기/확률통계 및 프로그래밍/강의자료/4주차 전체 수업.pdf");
        //String text = gptService.pdfToText("/Users/koomin/Library/Mobile Documents/com~apple~CloudDocs/지능기전/3학년 1학기/컴퓨터 네트워크/Chapter_3_v8.02.pdf");
        String[] split;
        split = gptService.longTextSplit(text, 2000);
        System.out.println(gptService.generateTranslation(split, "English", "korean"));
    }
    @Test
    public void word_test() throws IOException {
        gptService = new GptService();
        System.out.println(gptService.wordToText("/Users/koomin/Library/Mobile Documents/com~apple~CloudDocs/지능기전/3학년 1학기/컴퓨터 구조 및 운영체제/19011799_구민_과제1.docx"));
    }

    @Test
    public void ppt_test() throws IOException {
        gptService = new GptService();
        System.out.println(gptService.pptToText("/Users/koomin/Library/Mobile Documents/com~apple~CloudDocs/지능기전/3학년 1학기/컴퓨터 네트워크/Chapter_3_v8.02.pptx"));
    }

}