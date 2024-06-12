package com.vinsguru.aggregator.dto;

import com.vinsguru.aggregator.domain.Ticker;
import com.vinsguru.aggregator.domain.TradeAction;

public record TradeRequest(Ticker ticker,
                           TradeAction action,
                           Integer quantity) {
}
