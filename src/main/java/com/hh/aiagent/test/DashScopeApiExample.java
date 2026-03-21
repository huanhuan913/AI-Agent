package com.hh.aiagent.test;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;



//http调用
public class DashScopeApiExample {

    public static void main(String[] args) {
        // API Key（请替换为您实际的API Key）
        String apiKey = TAPIKEY.APTKEY;

        // 请求URL
        String url = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";

        // 构建请求体
        JSONObject requestBody = JSONUtil.createObj()
                                         .set("model", "qwen-plus")
                                         .set("input", JSONUtil.createObj()
                                                               .set("messages", JSONUtil.createArray()
                                                                                        .put(JSONUtil.createObj()
                                                                                                     .set("role", "system")
                                                                                                     .set("content", "You are a helpful assistant."))
                                                                                        .put(JSONUtil.createObj()
                                                                                                     .set("role", "user")
                                                                                                     .set("content", "你是谁？"))
                                                               )
                                         )
                                         .set("parameters", JSONUtil.createObj()
                                                                    .set("result_format", "message"));

        // 发送POST请求
        HttpResponse response = HttpRequest.post(url)
                                           .header("Authorization", "Bearer " + apiKey)
                                           .header("Content-Type", "application/json")
                                           .body(requestBody.toString())
                                           .charset(CharsetUtil.CHARSET_UTF_8)
                                           .execute();

        // 获取响应内容
        String body = response.body();

        // 打印响应结果
        System.out.println("响应状态码：" + response.getStatus());
        System.out.println("响应内容：" + body);


    }
}
