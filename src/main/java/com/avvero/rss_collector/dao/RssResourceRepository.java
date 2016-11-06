package com.avvero.rss_collector.dao;

import com.avvero.rss_collector.domain.RssResource;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author Avvero
 */
public interface RssResourceRepository extends CrudRepository<RssResource, String> {

    @Override
    List<RssResource> findAll();

}
