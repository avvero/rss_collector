package com.avvero.rss_collector.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Created by fxdev-belyaev-ay on 03.11.16.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationStart {

    private LocalDateTime dateTime;

}
