package com.seantcanavan.backoff;

@FunctionalInterface
public interface BackOffExecute<T> {
    T execute();
}
