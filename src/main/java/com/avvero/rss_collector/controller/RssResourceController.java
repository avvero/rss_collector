package com.avvero.rss_collector.controller;

import com.avvero.rss_collector.dao.RssResourceRepository;
import com.avvero.rss_collector.domain.RssResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Avvero
 */
@RestController
@RequestMapping("/rssResource")
public class RssResourceController {

    @Autowired
    RssResourceRepository repository;

    @RequestMapping(method = RequestMethod.GET)
    public Iterable<RssResource> findAll() {
        return repository.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public void add(@RequestBody RssResource rssResource) {
        repository.save(rssResource);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public void delete(@RequestBody RssResource rssResource) {
        repository.delete(rssResource);
    }

}
