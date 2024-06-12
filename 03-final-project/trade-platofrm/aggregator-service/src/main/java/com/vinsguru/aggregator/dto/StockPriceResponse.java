package com.vinsguru.aggregator.dto;

import com.vinsguru.aggregator.domain.Ticker;

public record StockPriceResponse(Ticker ticker,
                                 Integer price) {
}
