package com.avvero.rss_collector;

import com.avvero.rss_collector.domain.Rss;
import com.avvero.rss_collector.event.ApplicationStart;
import com.avvero.rss_collector.service.RssCache;
import com.avvero.rss_collector.service.RssConsumer;
import com.avvero.rss_collector.service.RssEventProducer;
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
    @Autowired
    RssCache rssCache;
    @Autowired
    RssEventProducer rssEventProducer;

    @Bean
    ApplicationStart getApplicationStartEvent() {
        return new ApplicationStart(LocalDateTime.now());
    }

    public static void main(String args[]) throws Throwable {
        SpringApplication.run(App.class, args);
    }

    @Scheduled(fixedDelay=5000)
    private void collectRss(){
        Rss rss = rssConsumer.get("http://igromania.ru/rss/rss_all.xml");
        LocalDateTime startDate = getApplicationStartEvent().getDateTime();
        rss.getChannel().getItems().stream()
                .filter(item -> item.getPubDate().isAfter(startDate))
                .filter(rssCache::store).forEach(rssEventProducer::publicate);
    }

}
