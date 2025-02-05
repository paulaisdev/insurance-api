package com.insurance.api.utils;

import java.util.List;

public interface CsvParser<T> {
    List<T> parse(String[] dados);
}
