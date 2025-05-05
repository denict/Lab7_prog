package org.example.network;

import java.io.Serializable;

public class Response implements Serializable {
    private static final long serialVersionUID = 4L;
    private final boolean success;
    private final String message;

    public Response(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
}
