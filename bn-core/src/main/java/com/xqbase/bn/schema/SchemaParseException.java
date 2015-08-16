package com.xqbase.bn.schema;

import com.xqbase.bn.exceptions.BaijiRuntimeException;

/**
 * Throws when parsing schema failed.
 *
 * @author Tony He
 */
public class SchemaParseException extends BaijiRuntimeException {

    public SchemaParseException(Throwable cause) {
        super(cause);
    }

    public SchemaParseException(String message) {
        super(message);
    }
}
