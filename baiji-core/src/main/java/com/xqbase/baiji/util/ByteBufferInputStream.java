package com.xqbase.baiji.util;

import java.io.IOException;
import java.io.InputStream;

/**
 * Utility to present {@link java.nio.ByteBuffer} data as an {@link java.io.InputStream}
 */
public class ByteBufferInputStream extends InputStream {

    @Override
    public int read() throws IOException {
        return 0;
    }
}
