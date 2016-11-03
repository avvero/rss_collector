package com.avvero.rss_collector.service;

import com.avvero.rss_collector.domain.Item;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by fxdev-belyaev-ay on 03.11.16.
 */
@Service
public class RssCache {

    private Map store = new ConcurrentHashMap<>();

    public boolean store(Item item) {
        return store.put(item, BigDecimal.ZERO) == null;
    }

}
