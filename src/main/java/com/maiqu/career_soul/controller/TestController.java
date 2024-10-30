package com.maiqu.career_soul.controller;

import com.maiqu.career_soul.pojo.Result;
import com.maiqu.career_soul.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("maiqu/test")
public class TestController {

    @Autowired
    private TestService testService;

    @PostMapping("/iq")
    public Result IQTest(@RequestParam Integer conversationsId, @RequestParam String userInput) {

        Result result = new Result();
        String response = testService.getIQResponse(conversationsId, userInput);

        if (!response.equals("400")) {
            result.setCode(200);
            result.setMsg("success");
            result.setData(response);
        } else {
            result.setCode(400);
            result.setMsg("error");
            result.setData("模型异常");
        }

        return result;
    }

    @PostMapping
    public Result addConversation(@RequestParam Integer userId, @RequestParam Integer agentId) {
        Integer conversationId = testService.addConversation(userId, agentId);
        return Result.success(conversationId);
    }

}
