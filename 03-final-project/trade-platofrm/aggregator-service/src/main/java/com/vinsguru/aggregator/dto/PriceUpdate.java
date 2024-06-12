package com.vinsguru.aggregator.dto;

import com.vinsguru.aggregator.domain.Ticker;

import java.time.LocalDateTime;

public record PriceUpdate(Ticker ticker,
                          Integer price,
                          LocalDateTime time) {
}
