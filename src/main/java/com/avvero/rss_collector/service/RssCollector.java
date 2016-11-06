package com.avvero.rss_collector.service;

import com.avvero.rss_collector.dao.RssResourceRepository;
import com.avvero.rss_collector.domain.RssResource;
import com.avvero.rss_collector.entity.queue.Event;
import com.avvero.rss_collector.entity.queue.EventChannel;
import com.avvero.rss_collector.entity.queue.EventItem;
import com.avvero.rss_collector.entity.rss.Channel;
import com.avvero.rss_collector.entity.rss.Item;
import com.avvero.rss_collector.entity.rss.Rss;
import com.avvero.rss_collector.event.ApplicationStart;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    @Autowired
    RssResourceRepository repository;
    @Value("${rss_collector.collect_old}")
    private boolean collectOld;

    @Autowired
    ApplicationStart applicationStart;

    public void collect() {
        LocalDateTime startDate = applicationStart.getDateTime();

        repository.findAll().stream()
                .peek(url -> log.info("Load rss from {}", url))
                .map(RssResource::getUrl)
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
