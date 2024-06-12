package com.vinsguru.aggregator.dto;


import com.vinsguru.aggregator.domain.Ticker;

public record Holding(Ticker ticker,
                      Integer quantity) {
}
