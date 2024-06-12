package com.vinsguru.customerportfolio.dto;

import com.vinsguru.customerportfolio.domain.Ticker;
import com.vinsguru.customerportfolio.domain.TradeAction;

public record StockTradeResponse(Integer customerId,
                                 Ticker ticker,
                                 Integer price,
                                 Integer quantity,
                                 TradeAction action,
                                 Integer totalPrice,
                                 Integer balance) {
}
