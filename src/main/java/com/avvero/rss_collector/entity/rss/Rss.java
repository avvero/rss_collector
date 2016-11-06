package com.avvero.rss_collector.entity.rss;

import lombok.Data;

/**
 * Created by fxdev-belyaev-ay on 03.11.16.
 */
@Data
public class Rss {

    private String sourceUrl;
    private Channel channel;

}
