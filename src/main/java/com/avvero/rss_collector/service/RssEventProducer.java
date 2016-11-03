package com.avvero.rss_collector.service;

import com.avvero.rss_collector.domain.Event;
import com.avvero.rss_collector.entity.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created by fxdev-belyaev-ay on 03.11.16.
 */
@Slf4j
@Service
public class RssEventProducer {

    public void publish(Event event) {
        log.info("New event: \n\r {}", event);
    }

}
