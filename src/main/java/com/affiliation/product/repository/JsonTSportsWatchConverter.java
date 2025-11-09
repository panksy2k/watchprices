package com.affiliation.product.repository;

import io.vertx.core.json.JsonObject;

public interface JsonTSportsWatchConverter<T> {
    T convert(JsonObject document);
}
