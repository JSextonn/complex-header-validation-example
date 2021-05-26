package com.sexton.example.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class RequestUtils {
    private RequestUtils() {
        // private constructor to stop instantiation
    }

    public static Optional<String> getHeader(String headerName, HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(headerName));
    }
}
