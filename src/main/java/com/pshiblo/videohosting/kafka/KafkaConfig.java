package com.pshiblo.videohosting.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pshiblo.videohosting.kafka.error.ResendKafkaProducerInterceptor;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import static com.pshiblo.videohosting.kafka.error.ErrorListener.TOPIC_ERROR;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${kafka.url}")
    private String kafkaUrlServer;

    @Bean
    public String kafkaUrlServer() {
        return kafkaUrlServer;
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrlServer);
        final KafkaAdmin admin = new KafkaAdmin(configs);
        admin.setFatalIfBrokerNotAvailable(true);
        return admin;
    }

    @Bean
    public ProducerFactory<String, Object> producerFactory(@Autowired ObjectMapper objectMapper) throws UnknownHostException {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrlServer());
        configProps.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, InetAddress.getLocalHost().getHostName());
        configProps.put(ProducerConfig.TRANSACTION_TIMEOUT_CONFIG, String.valueOf(30000));
        configProps.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, ResendKafkaProducerInterceptor.class.getName());
        return new DefaultKafkaProducerFactory<>(configProps, new StringSerializer(),
                new JsonSerializer<>(objectMapper));
    }

    @Primary
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> producerFactory) {
        KafkaTemplate<String, Object> ret = new KafkaTemplate<>(producerFactory);
        ret.setAllowNonTransactional(true);
        return ret;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
            ConsumerFactory<String, String> consumerFactory, ObjectMapper objectMapper,
            KafkaTemplate<String, Object> kafkaTemplate,
            @Value(TOPIC_ERROR) String errorTopic) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setMessageConverter(new JsonMessageConverter(objectMapper));
        factory.setConcurrency(4);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD); // Commit record-by-record
        ResendKafkaErrorHandler resendErrorHandler = new ResendKafkaErrorHandler(kafkaTemplate, errorTopic, objectMapper);
        factory.setErrorHandler(new DefaultKafkaErrorHandler(resendErrorHandler, kafkaTemplate));
        factory.getContainerProperties().setEosMode(ContainerProperties.EOSMode.BETA);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrlServer());
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        configProps.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(configProps);
    }
}
