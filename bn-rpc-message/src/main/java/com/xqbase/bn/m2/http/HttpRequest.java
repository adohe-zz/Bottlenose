package com.xqbase.bn.m2.http;

import com.xqbase.bn.m2.Request;

import java.net.URI;
import java.nio.ByteBuffer;

/**
 * Created by tonyhe on 15-5-26.
 */
public class HttpRequest implements Request {
    @Override
    public URI getUri() {
        return null;
    }

    @Override
    public ByteBuffer getEntity() {
        return null;
    }
}
