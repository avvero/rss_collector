package com.avvero.rss_collector;

import com.avvero.rss_collector.event.ApplicationStart;
import com.avvero.rss_collector.service.RssCollector;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.component.jms.JmsConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

/**
 * Created by fxdev-belyaev-ay on 03.11.16.
 */
@ComponentScan
@EnableAsync
@EnableScheduling
@EnableAutoConfiguration
public class App {

    @Value("${rss_collector.jms_broker_url}")
    public String jmsBrokerUrl;
    @Value("${rss_collector.jms_broker_user_name}")
    public String jmsBrokerUserName;
    @Value("${rss_collector.jms_broker_password}")
    public String jmsBrokerPassword;

    @Autowired
    RssCollector collector;

    @Bean
    ApplicationStart getApplicationStartEvent() {
        return new ApplicationStart(LocalDateTime.now());
    }

    public static void main(String args[]) throws Throwable {
        SpringApplication.run(App.class, args);
    }

    @Scheduled(fixedDelayString="${rss_collector.collect_delay}")
    private void collectRss(){
        collector.collect();
    }

    @Bean
    public ActiveMQConnectionFactory pooledConnectionFactory() {
        return new ActiveMQConnectionFactory(jmsBrokerUserName, jmsBrokerPassword, jmsBrokerUrl);
    }

    @Bean
    public JmsConfiguration jmsConfig() {
        JmsConfiguration configuration = new JmsConfiguration(pooledConnectionFactory());
        configuration.setConcurrentConsumers(10);
        return configuration;
    }

    @Bean
    public ActiveMQComponent activemq() {
        ActiveMQComponent activeMQComponent = new ActiveMQComponent();
        activeMQComponent.setConfiguration(jmsConfig());
        return activeMQComponent;
    }

    @Bean
    public JacksonDataFormat jacksonDataFormat() {
        JacksonDataFormat jacksonDataFormat =  new JacksonDataFormat();
        jacksonDataFormat.addModule(new JSR310Module());
        return jacksonDataFormat;
    }
}
