package com.pshiblo.videohosting.service;

import com.pshiblo.videohosting.dto.kafka.UserKafka;
import com.pshiblo.videohosting.models.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserCallbackService {

    public static final String KAFKA_TOPIC_USER_CONFIRM = "l4.user.confirm.callback";

    public static final String KAFKA_GROUP_ID_USER_CONFIRM = "user.confirm";

    @Bean
    public NewTopic confirmUser() {
        return TopicBuilder.name(KAFKA_TOPIC_USER_CONFIRM)
                .partitions(10)
                .build();
    }

    @KafkaListener(id = KAFKA_GROUP_ID_USER_CONFIRM, topics = KAFKA_TOPIC_USER_CONFIRM)
    public void listenerUserConfirm(UserKafka user) throws Exception {
        log.info(user.toString());
        if (user.getId() == 3) {
            throw new Exception("user not valid");
        }
    }
}
