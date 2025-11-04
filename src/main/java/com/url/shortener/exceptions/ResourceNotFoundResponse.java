package com.url.shortener.exceptions;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResourceNotFoundResponse {
    public String type;
    public String message;
    public HttpStatus httpStatus;
}
