package com.example.borys_mankowski_test_10.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class ExceptionDto {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final String message;

}
