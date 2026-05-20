package com.mageddo.ws.rs;

import com.mageddo.ws.rs.ApiClientException;
import jakarta.ws.rs.core.Response;

public class ResponseValidator {

    public static void success(Response res) {
        int status = res.getStatus();
        if (status >= 200 && status <= 299) return;

        String body = res.hasEntity() ? res.readEntity(String.class) : null;
        throw new ApiClientException(status, body);
    }
}
