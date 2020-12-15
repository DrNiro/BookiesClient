package com.dts.bookies.util.memento;

import java.util.Objects;

public class Memento {

    private String state;

    public Memento(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Memento memento = (Memento) o;
        return Objects.equals(state, memento.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state);
    }

    @Override
    public String toString() {
        return "Memento{" +
                "state='" + state + '\'' +
                '}';
    }
}
