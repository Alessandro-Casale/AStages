package com.alessandro.astages.api.develop;

public @interface ChangeVisibilityTo {
    Visibility value();

    enum Visibility {
        PUBLIC, PRIVATE, PROTECTED
    }
}
