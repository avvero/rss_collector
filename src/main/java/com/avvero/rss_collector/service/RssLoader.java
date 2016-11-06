package com.avvero.rss_collector.service;

import com.avvero.rss_collector.entity.rss.Rss;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;

/**
 * Created by fxdev-belyaev-ay on 03.11.16.
 */
@Service
public class RssLoader {

    public Rss load(String url) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2XmlHttpMessageConverter());
        Rss rss = restTemplate.getForObject(url, Rss.class);
        rss.setSourceUrl(url); //TODO bad smell
        return rss;
    }

}
