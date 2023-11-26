package com.example.borys_mankowski_test_10.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class ExceptionDto {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final String message;

    // TODO: 26/11/2023 check all of the exceptions if they are needed and are used properly
}
