package com.avvero.rss_collector.service;

import com.avvero.rss_collector.domain.Item;
import org.springframework.stereotype.Service;

/**
 * Created by fxdev-belyaev-ay on 03.11.16.
 */
@Service
public class RssEventProducer {

    public void publicate(Item item) {
        System.out.println("Item publication");
    }

}
