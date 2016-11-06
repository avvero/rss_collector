package com.avvero.rss_collector.service;

import com.avvero.rss_collector.entity.queue.Event;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by fxdev-belyaev-ay on 03.11.16.
 */
@Service
public class RssCache {

    private Map<String, LocalDateTime> store = new ConcurrentHashMap<>();

    public boolean store(Event event) {
        LocalDateTime lastPubDateTime = store.get(event.getSourceUrl());
        if (lastPubDateTime == null || lastPubDateTime.isBefore(event.getItem().getPubDate())) {
            store.put(event.getSourceUrl(), event.getItem().getPubDate());
            return true;
        } else {
            return false;
        }
    }
}
