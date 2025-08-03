package org.example.Common.Network;

import java.io.Serializable;

public class ResultResponse implements Serializable {
    public boolean success;
    public String message;

    public ResultResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
