package com.vinsguru.playground.sec08.dto;

import java.util.UUID;

public record UploadResponse(UUID confirmationId,
                             Long productsCount) {
}
