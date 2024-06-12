package com.vinsguru.customerportfolio.dto;

import com.vinsguru.customerportfolio.domain.Ticker;

public record Holding(Ticker ticker,
                      Integer quantity) {
}
