package com.hh.aiagent.App;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hh.aiagent.Advisor.MyLoggerAdvisor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * 中医咨询AI
 *
 * @author zhh
 * @date 2026/3/22 11:20
 */

@Component
@Slf4j
public class TraditionalCNMedicalSciApp {

    private ChatClient chatClient;

    final  String SYSTEM_PROMPT="你是专业、严谨、合规的中医智能咨询助手，基于中医基础理论（阴阳五行、脏腑经络、气血津液）" +
            "与经典诊疗思维提供健康参考，严格恪守医疗边界，不做诊断、不开处方、不替代执业医师。" +
            "、回答规范" +"回复结果控制在300字以内，条理清晰"+
            "先亮明边界：每次首句 / 结尾明确「仅供参考，需医师面诊确认」。\n" +
            "辨证清晰：用通俗语言解释病机，不晦涩、不玄虚。\n" +
            "建议温和：以「可尝试」「建议」「适合」等委婉表述，不绝对化。\n" +
            "语气亲和：耐心、严谨、易懂，符合中医人文关怀";


    /**
     * 初始化chatClient
     * @param dashscopeChatModel
     */
    public TraditionalCNMedicalSciApp(ChatModel dashscopeChatModel) {
//        // 初始化基于文件的对话记忆
//        String fileDir = System.getProperty("user.dir") + "/tmp/chat-memory";
//        ChatMemory chatMemory = new FileBasedChatMemory(fileDir);
        // 初始化基于内存的对话记忆
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder()
                                                                    .chatMemoryRepository(new InMemoryChatMemoryRepository())
                                                                    .maxMessages(20)
                                                                    .build();
        chatClient = ChatClient.builder(dashscopeChatModel)
                               .defaultSystem(SYSTEM_PROMPT)
                               .defaultAdvisors(
                                       MessageChatMemoryAdvisor.builder(chatMemory).build(),
                                new MyLoggerAdvisor()

//                        // 自定义推理增强 Advisor，可按需开启
//                       ,new ReReadingAdvisor()
                               )
                               .build();
    }

    /**
     * AI 基础对话（支持多轮对话记忆）
     *
     * @param message
     * @param chatId
     * @return
     */
    public String doChat(String message, String chatId) {
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

    /**
     * AI 基础对话（支持多轮对话记忆，SSE 流式传输）
     *
     * @param message
     * @param chatId
     * @return
     */
    public Flux<String> doChatByStream(String message, String chatId) {
        return chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                .stream()
                .content();
    }


     record QueryReport(
                          @JsonProperty("title") String title,
                          @JsonProperty("suggestions") List<String> suggestions) {

    }

    /**
     * AI 恋爱报告功能（实战结构化输出）
     *
     * @param message
     * @param chatId
     * @return
     */
    public QueryReport doChatWithReport(String message, String chatId) {
        QueryReport queryReport = chatClient
                .prompt()
                .system(SYSTEM_PROMPT + "每次对话后都要生成恋爱结果，标题为{用户名}的恋爱报告，内容为建议列表")
                .user(message)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                .call()
                .entity(QueryReport.class);
//
        return queryReport;
    }
}
