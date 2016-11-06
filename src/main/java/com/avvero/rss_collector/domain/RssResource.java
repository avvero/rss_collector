package com.avvero.rss_collector.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Avvero
 */
@Data
@Entity
public class RssResource {

    @Id
    private String url;

}
