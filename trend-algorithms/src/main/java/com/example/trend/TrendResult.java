package com.example.trend;

public record TrendResult(
    String metric,
    double value,
    TrendStatus status,
    String details
) {
}
