package com.hh.aiagent.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO: 这里当前类描述
 *
 * @author zhh
 * @date 2026/3/21 14:06
 */
@RestController("测试文档显示")
public class TestController {
    @GetMapping("/t1")
    @Operation(description = "测试文档显示")
    public void test(){}
}
