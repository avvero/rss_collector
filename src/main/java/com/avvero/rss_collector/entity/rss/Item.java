package com.avvero.rss_collector.entity.rss;

import com.avvero.rss_collector.utils.LocalDateTimeStringDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import javafx.util.converter.LocalDateTimeStringConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Created by fxdev-belyaev-ay on 03.11.16.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    private String title;
    private String link;
    private String description;
    @JsonDeserialize(using = LocalDateTimeStringDeserialize.class)
    private LocalDateTime pubDate;

}
