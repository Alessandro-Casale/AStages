package com.alessandro.astages.api.time;

import com.alessandro.astages.api.nullability.Nullable;

import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ATime {
    private final String timeString;

    public ATime(String timeString) {
        this.timeString = timeString;
    }

    public String getTimeString() {
        return timeString;
    }

    public int getTicks() {
        /*
            \\d+ captures one or more digits.
            \\s* allows optional spaces between the number and the unit.
            (h|m|s) captures the time unit
         */

        Pattern pattern = Pattern.compile("(\\d+)\\s*([hmst])");
        Matcher matcher = pattern.matcher(timeString);
        int totalTicks = 0;

        while (matcher.find()) {
            int value = Integer.parseInt(matcher.group(1));
            String unit = matcher.group(2);

            switch (unit) {
                case "t":
                    totalTicks += value;
                    break;
                case "s":
                    totalTicks += value * 20;
                    break;
                case "m":
                    totalTicks += value * 1200;
                    break; // 1 minute -> 16.666666 ticks
                case "h":
                    totalTicks += value * 72000;
                    break; // 1 hour -> 1000 ticks;
            }
        }

        return totalTicks;
    }

    public static @Nullable ATime of(@Nullable Object object) {
        if (object instanceof CharSequence sequence) {
            var string = sequence.toString().trim();
            return new ATime(string);
        } else if (object instanceof Number number) {
            var ticks = number.intValue();
            return new ATime(ticks + "t");
        } else if (object instanceof ATime time) {
            return time;
        } else if (object instanceof Temporal temporal) {
            var ticks = temporal.get(ChronoField.MILLI_OF_SECOND) / 50; // 1 tick = 50 milliseconds
            var seconds = temporal.get(ChronoField.SECOND_OF_MINUTE);
            var minutes = temporal.get(ChronoField.MINUTE_OF_HOUR);
            var hours = temporal.get(ChronoField.HOUR_OF_DAY);
            return new ATime(hours + "h " + minutes + "m " + seconds + "s " + ticks + "t");
        }

        return null;
    }
}
