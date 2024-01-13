package org.learnings.statemachines.domain;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public enum TripEvents {
    DRIVER_REQUESTS_TRIP("DRIVER_REQUESTS_TRIP"),
    DRIVER_ARRIVES_AT_PICKUP("DRIVER_ARRIVES_AT_PICKUP"),
    TRIP_STARTS("TRIP_STARTS"),
    ARRIVES_AT_DESTINATION("ARRIVES_AT_DESTINATION");

    private final String name;

    private static final Map<String, TripEvents> ENUM_MAP;

    TripEvents(String name) {
        this.name = name;
    }

    static {
        Map<String, TripEvents> map = new ConcurrentHashMap<String, TripEvents>();
        for (TripEvents instance : TripEvents.values()) {
            map.put(instance.name.toLowerCase(), instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    public static Optional<TripEvents> get(String name) {
        return Optional.ofNullable(ENUM_MAP.get(name.toLowerCase().trim()));
    }
}
