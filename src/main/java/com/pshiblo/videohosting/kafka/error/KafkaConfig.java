package com.pshiblo.videohosting.kafka.error;

import com.vltgroup.powercasino.kafka.header.DelayHeader;
import com.vltgroup.powercasino.kafka.kafkatemplate.DefaultKafkaTemplate;
import com.vltgroup.powercasino.kafka.kafkatemplate.RetryingKafkaTemplate;
import com.vltgroup.powercasino.manager.ModelSerializationManager;
import com.vltgroup.powercasino.manager.SettingManager;
import com.vltgroup.powercasino.model.Setting;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.messaging.MessageHeaders;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@EnableKafka
@EnableRetry
@Configuration
public class KafkaConfig {

  private static final Logger log = LoggerFactory.getLogger(KafkaConfig.class);

  @Value("${kafka.bootstrap}")
  private String kafkaBootstrapServers;

  private @Autowired SettingManager settingManager;
  private @Autowired ModelSerializationManager modelSerializationManager;
  
  @Bean
  public String kafkaBootstrapServers() {
    log.info("Using kafka brokers: {}", kafkaBootstrapServers);
    return kafkaBootstrapServers;
  }

  @Bean
  public KafkaAdmin kafkaAdmin() {
    Map<String, Object> configs = new HashMap<>();
    configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers());
    final KafkaAdmin admin = new KafkaAdmin(configs);
    admin.setFatalIfBrokerNotAvailable(true); // Entire app. context would fail if not broker connection is available
    return admin;
  }

  @Bean
  public ProducerFactory<String, String> producerFactory(@Autowired String kafkaBootstrapServers) {
    Map<String, Object> configProps = new HashMap<>();
    configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
    configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    return new DefaultKafkaProducerFactory<>(configProps);
  }

  @Bean
  public DefaultKafkaTemplate kafkaTemplate(ProducerFactory<String, String> producerFactory) {
    return new DefaultKafkaTemplate(new RetryingKafkaTemplate<>(producerFactory), modelSerializationManager.getObjectMapper());
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(ConsumerFactory<String, String> consumerFactory,
                                                                                               DefaultKafkaTemplate kafkaTemplate) {
    ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory);
    factory.setMessageConverter(new JsonMessageConverter(modelSerializationManager.getObjectMapper()));
    factory.setRetryTemplate(retryTemplate());
    factory.setRecoveryCallback(
      new RetryRecoveryCallback<>(
        kafkaTemplate.getRetryingKafkaTemplate(),
        settingManager.getLong(Setting.default_kafka_init_millis_recovery_sleep),
        settingManager.getLong(Setting.default_kafka_max_minutes_recovery_sleep)
      )
    );
    factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.BATCH);
    return factory;
  }

  @Bean
  public RetryTemplate retryTemplate() {
    RetryTemplate retryTemplate = new RetryTemplate();

    SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy();
    simpleRetryPolicy.setMaxAttempts(1);
    retryTemplate.setRetryPolicy(simpleRetryPolicy);
    return retryTemplate;
  }

  @Bean
  public ConsumerFactory<String, String> consumerFactory(@Autowired String kafkaBootstrapServers) {
    Map<String, Object> configProps = new HashMap<>();
    configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
    configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    return new DefaultKafkaConsumerFactory<>(configProps);
  }

  @Bean
  public KafkaListenerErrorHandler kafkaListenerErrorHandler() {
    return (message, exception) -> {
      MessageHeaders headers = message.getHeaders();
      if (headers.containsKey(DelayHeader.KEY)) {
        Duration timeSleep = DelayHeader.getDelay((byte[]) headers.get(DelayHeader.KEY));
        if (!timeSleep.isZero()) {
          try {
            Thread.sleep(timeSleep.toMillis());
          }catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
      return Optional.empty();
    };
  }
}
