package com.alessandro.astages.api.time;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;

@NotNullParamsAndMethodsReturn
public class AMutableTime extends ATime {
    private int currentTicks;

    private AMutableTime(String timeString, int currentTicks) {
        super(timeString);
        this.currentTicks = currentTicks;
    }

    public static AMutableTime fromFixed(String timeString) {
        return fromFixed(new ATime(timeString));
    }

    public static AMutableTime fromFixed(ATime time) {
        return new AMutableTime(time.getTimeString(), time.getTicks());
    }

    public static AMutableTime fromTicks(Integer ticks) {
        return new AMutableTime(ticks + "t", ticks);
    }

    public boolean subtractTicks(int ticks) {
        currentTicks -= ticks;
        return currentTicks <= 0;
    }

    public int getCurrentTicks() {
        return currentTicks;
    }

    public int getCurrentSeconds() {
        return currentTicks / 20;
    }
}
