package com.boolck.tasks.eventbus;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

class ConsumerWithFilter<T> {

    private final Consumer<T> consumer;
    private final Predicate<T> filter;

    public ConsumerWithFilter(Consumer<T> consumer, Predicate<T> filter) {
        this.consumer = consumer;
        this.filter = filter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConsumerWithFilter<T> that = (ConsumerWithFilter<T>) o;
        return getConsumer().equals(that.getConsumer()) && getFilter().equals(that.getFilter());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getConsumer(), getFilter());
    }

    public Predicate<T> getFilter() {
        return filter;
    }

    public Consumer<T> getConsumer() {
        return consumer;
    }
}
