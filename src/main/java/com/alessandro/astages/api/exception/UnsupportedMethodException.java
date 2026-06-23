package com.alessandro.astages.api.exception;

import com.alessandro.astages.api.nullability.NotNullMethodsReturn;

@NotNullMethodsReturn
public class UnsupportedMethodException extends UnsupportedOperationException {
    public UnsupportedMethodException(String suggestedCall) {
        super(buildMessage(suggestedCall));
    }

    public UnsupportedMethodException() {
        super(buildMessage());
    }

    public static UnsupportedMethodException useInstead(String suggestedCall) {
        return new UnsupportedMethodException(suggestedCall);
    }

    private static String buildMessage(String suggestedCall) {
        StackTraceElement caller = findCaller();

        String className = caller.getClassName();
        String simpleClass = className.substring(className.lastIndexOf('.') + 1);
        String method = caller.getMethodName();

        return "Method " + simpleClass + "#" + method + "() is not supported.\n" +
            "Use " + suggestedCall + " instead.";
    }

    private static String buildMessage() {
        StackTraceElement caller = findCaller();

        String className = caller.getClassName();
        String simpleClass = className.substring(className.lastIndexOf('.') + 1);
        String method = caller.getMethodName();

        return "Method " + simpleClass + "#" + method + "() is not supported.";
    }

    private static StackTraceElement findCaller() {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();

        for (StackTraceElement e : stack) {
            if (!e.getClassName().equals(Thread.class.getName())
                && !e.getClassName().equals(UnsupportedMethodException.class.getName())) {
                return e;
            }
        }

        return stack[stack.length - 1];
    }
}