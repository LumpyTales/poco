package io.github.lumpytales.poco.testclasses;

/**
 * Price test class
 *
 * @param currency Price currency
 * @param value    Price value
 * @param tax      Price tax
 */
public record Price(String currency, Double value, Price tax) {}
