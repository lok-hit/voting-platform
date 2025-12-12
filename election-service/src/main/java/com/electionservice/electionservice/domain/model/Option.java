package com.electionservice.electionservice.domain.model;

import java.util.Objects;

public final class Option {
    private final OptionId id;
    private final String name;

    public Option(OptionId id, String name) {
        this.id = Objects.requireNonNull(id, "Option ID cannot be null");
        this.name = validateName(name);
    }

    private static String validateName(String name) {
        if (name == null) {
            throw new NullPointerException("Name cannot be null");
        }
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty or blank");
        }
        return name;
    }

    public static Option create(String name) {
        Objects.requireNonNull(name, "Name cannot be null");
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty or blank");
        }
        return new Option(OptionId.newId(), name);
    }

    public OptionId id() { return id; }
    public String name() { return name; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Option)) return false;
        Option option = (Option) o;
        return Objects.equals(id, option.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}

