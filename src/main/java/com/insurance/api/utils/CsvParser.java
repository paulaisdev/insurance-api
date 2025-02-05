package com.insurance.api.service.csv;

import java.util.List;

public interface CsvParser<T> {
    List<T> parse(String[] dados);
}
