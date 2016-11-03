package com.avvero.rss_collector;

import com.avvero.rss_collector.service.RssConsumer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    RssConsumer rssConsumer;

    public static void main(String args[]) throws Throwable {
        SpringApplication.run(App.class, args);
    }

//    @Bean
//    public Module customModule() {
//        SimpleModule module = new SimpleModule();
//        module.addDeserializer(LocalDateTime.class, new Dateconverter.Deserialize());
//        return module;
//    }

    @Scheduled(fixedDelay=5000)
    private void collectRss(){
        System.out.println(rssConsumer.get("http://igromania.ru/rss/rss_all.xml"));
    }

}
