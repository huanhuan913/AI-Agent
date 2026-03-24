package com.hh.aiagent;

import com.hh.aiagent.App.TraditionalCNMedicalSciApp;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class AiAgentApplicationTests {

    @Resource
    TraditionalCNMedicalSciApp traditionalCNMedicalSciApp;

    @Test
    void contextLoads() {
    }

    /**
     * 测试中医咨询  会话记忆功能
     */
    @Test
    public void test1(){

        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "我的肠胃不好";
        String answer = traditionalCNMedicalSciApp.doChat(message, chatId);
        // 第二轮
        message = "我对陈皮过敏";
        answer = traditionalCNMedicalSciApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // 第三轮
        message = "我对什么过敏来着";
        answer = traditionalCNMedicalSciApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
    }

    /**
     * 测试中医咨询  结构化输出
     */
    @Test
    public void test2(){

        String chatId = UUID.randomUUID().toString();
       String message="我肠胃不好";

        Assertions.assertNotNull(traditionalCNMedicalSciApp.doChatWithReport(message,
                chatId));
    }
}
