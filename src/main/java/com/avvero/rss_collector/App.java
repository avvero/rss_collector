package com.avvero.rss_collector;

import com.avvero.rss_collector.domain.Event;
import com.avvero.rss_collector.domain.EventChannel;
import com.avvero.rss_collector.domain.EventItem;
import com.avvero.rss_collector.entity.Channel;
import com.avvero.rss_collector.entity.Item;
import com.avvero.rss_collector.entity.Rss;
import com.avvero.rss_collector.event.ApplicationStart;
import com.avvero.rss_collector.service.RssCache;
import com.avvero.rss_collector.service.RssLoader;
import com.avvero.rss_collector.service.RssEventProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by fxdev-belyaev-ay on 03.11.16.
 */
@Slf4j
@ComponentScan
@EnableAsync
@EnableScheduling
@EnableAutoConfiguration
public class App {

    @Autowired
    RssLoader rssLoader;
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
        LocalDateTime startDate = getApplicationStartEvent().getDateTime();
        List<String> rssUrls = Arrays.asList("http://igromania.ru/rss/rss_all.xml");

        rssUrls.stream()
                .peek(url -> log.info("Load rss from {}", url))
                .map(rssLoader::load)
                .flatMap(this::parse)
                .filter(item -> item.getItem().getPubDate().isAfter(startDate))
                .filter(rssCache::store).forEach(rssEventProducer::publish);
    }

    private Stream<Event> parse(Rss rss) {
        return rss.getChannel().getItems().stream().map(item -> parse(rss, item));
    }

    private Event parse(Rss rss, Item item){
        Channel channel = rss.getChannel();
        return new Event(
                rss.getSourceUrl(),
                new EventChannel(channel.getTitle(), channel.getLink(), channel.getDescription()),
                new EventItem(item.getTitle(), item.getLink(), item.getDescription(), item.getPubDate())
        );
    }

}
