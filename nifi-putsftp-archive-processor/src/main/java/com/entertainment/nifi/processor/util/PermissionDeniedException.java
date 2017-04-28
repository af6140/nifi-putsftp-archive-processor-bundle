package com.entertainment.nifi.processor.util;

/**
 * Created by dwang on 4/28/17.
 */
import java.io.IOException;

public class PermissionDeniedException extends IOException {
    private static final long serialVersionUID = -6215434916883053982L;

    public PermissionDeniedException(final String message) {
        super(message);
    }

    public PermissionDeniedException(final String message, final Throwable t) {
        super(message, t);
    }
}