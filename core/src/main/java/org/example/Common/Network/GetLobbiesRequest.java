package org.example.Common.Network;

import java.io.Serializable;
public class GetLobbiesRequest implements Serializable {
    public SimplePlayer requester;

    public GetLobbiesRequest() {}

    public GetLobbiesRequest(SimplePlayer requester) {
        this.requester = requester;
    }
}
