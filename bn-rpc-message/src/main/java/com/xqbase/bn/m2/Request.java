package com.xqbase.bn.m2;

import java.net.URI;

/**
 * This class represents the request message.
 *
 * @author Tony He
 */
public interface Request extends Message {

    URI getUri();
}
