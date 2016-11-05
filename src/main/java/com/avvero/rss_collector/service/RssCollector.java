package com.avvero.rss_collector.service;

import com.avvero.rss_collector.domain.Event;
import com.avvero.rss_collector.domain.EventChannel;
import com.avvero.rss_collector.domain.EventItem;
import com.avvero.rss_collector.entity.Channel;
import com.avvero.rss_collector.entity.Item;
import com.avvero.rss_collector.entity.Rss;
import com.avvero.rss_collector.event.ApplicationStart;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Avvero
 */
@Slf4j
@Service
public class RssCollector {

    @Autowired
    RssLoader rssLoader;
    @Autowired
    RssCache rssCache;
    @Autowired
    RssEventProducer rssEventProducer;
    @Autowired
    ProducerTemplate producerTemplate;

    @Value("#{'${rss_collector.urls}'.split(',')}")
    private List<String> urls;
    @Value("${rss_collector.collect_old}")
    private boolean collectOld;

    @Autowired
    ApplicationStart applicationStart;

    public void collect() {
        LocalDateTime startDate = applicationStart.getDateTime();

        urls.stream()
                .peek(url -> log.info("Load rss from {}", url))
                .map(rssLoader::load)
                .flatMap(this::parse)
                .filter(item -> collectOld || item.getItem().getPubDate().isAfter(startDate))
                .filter(rssCache::store)
                .peek(event -> log.info("New event: \n\r {}", event))
                .forEach(event -> producerTemplate.sendBody("direct:push-event", event));
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
