package com.url.shortener.payload;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ApiResponse {
    private String message;
    private boolean success;
    private String type;
}
