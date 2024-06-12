package com.vinsguru.aggregator.dto;


import com.vinsguru.aggregator.domain.Ticker;
import com.vinsguru.aggregator.domain.TradeAction;

public record StockTradeRequest(Ticker ticker,
                                Integer price,
                                Integer quantity,
                                TradeAction action) {

}
