package bank.utils;

import org.jetbrains.annotations.NotNull;

public record Date(int day, int month, int year) implements Comparable<Date> {

    public static Date parse(String str) {
        String[] split = str.split("\\.");
        return new Date(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
    }
    @Override
    public @NotNull String toString() {
        return day + "." + month + "." + year;
    }

    @Override
    public int compareTo(@NotNull Date other) {
        if (year - other.year != 0) return year - other.year();
        if (month - other.month != 0) return month - other.month();
        return day - other.day();
    }
}
