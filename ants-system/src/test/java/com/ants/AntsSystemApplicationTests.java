package com.ants;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AntsSystemApplicationTests {
    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Test
    public void test () {
        rabbitTemplate.convertAndSend("pushBaiDuQueue", "http://wxmin.cn/articleDetails/");
    }

}
