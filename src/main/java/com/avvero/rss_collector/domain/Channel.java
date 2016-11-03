package com.avvero.rss_collector.domain;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by fxdev-belyaev-ay on 03.11.16.
 */
@Data
public class Channel {

    private String title;
    private String link;
    private String description;
    @JacksonXmlProperty(localName = "item")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Item> items;

}
