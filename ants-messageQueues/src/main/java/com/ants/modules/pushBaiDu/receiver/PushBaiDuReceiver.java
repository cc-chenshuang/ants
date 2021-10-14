package com.ants.modules.pushBaiDu.receiver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * TODO
 * Author Chen
 * Date   2021/10/13 10:28
 */
@Component
@Slf4j
public class PushBaiDuReceiver {

    @RabbitListener(queues = "pushBaiDuQueue")
    public void process(String url) {
        log.info(url);
    }
}
