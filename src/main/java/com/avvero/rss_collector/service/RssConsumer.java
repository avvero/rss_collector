package com.avvero.rss_collector.service;

import com.avvero.rss_collector.domain.Rss;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;

/**
 * Created by fxdev-belyaev-ay on 03.11.16.
 */
@Service
public class RssConsumer {

    public Rss get(String url) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2XmlHttpMessageConverter());
        Rss rss = restTemplate.getForObject(url, Rss.class);
        return rss;
    }

}
