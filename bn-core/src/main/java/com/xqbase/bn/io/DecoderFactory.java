package com.xqbase.bn.io;

import com.xqbase.bn.schema.Schema;

import java.io.IOException;

/**
 * A factory for creating and configuring {@link Decoder}s.
 * <p/>
 * Factories are thread-safe, and are generally cached by applications for
 * performance reasons. Multiple instances are only required if multiple
 * concurrent configurations are needed.
 *
 * @see Decoder
 */
public class DecoderFactory {

    private static final DecoderFactory DECODER_FACTORY = new DefaultDecoderFactory();
    private static final int DEFAULT_BUFFER_SIZE = 8192;

    private int binaryDecoderBufferSize = DEFAULT_BUFFER_SIZE;

    public static DecoderFactory get() {
        return DECODER_FACTORY;
    }

    /**
     * Configures this factory to use the specified buffer size when creating
     * Decoder instances that buffer their input. The default buffer size is
     * 8192 bytes.
     *
     * @param size The preferred buffer size. Valid values are in the range [32,
     *          16*1024*1024]. Values outside this range are rounded to the nearest
     *          value in the range. Values less than 512 or greater than 1024*1024
     *          are not recommended.
     * @return This factory, to enable method chaining:
     * <pre>
     * DecoderFactory myFactory = new DecoderFactory().useBinaryDecoderBufferSize(4096);
     * </pre>
     */
    public DecoderFactory configureDecoderBufferSize(int size) {
        if (size < 32)
            size = 32;
        if (size > 16 * 1024 * 1024)
            size = 16 * 1024 * 1024;
        this.binaryDecoderBufferSize = size;
        return this;
    }

    /**
     * Creates a {@link ResolvingDecoder} wrapping the Decoder provided. This
     * ResolvingDecoder will resolve input conforming to the <i>writer</i> schema
     * from the wrapped Decoder, and present it as the <i>reader</i> schema.
     *
     * @param schema
     *          The Schema that the source data is in. Cannot be null.
     * @param wrapped
     *          The Decoder to wrap.
     * @return A ResolvingDecoder configured to resolve <i>writer</i> to
     *         <i>reader</i> from <i>in</i>
     * @throws IOException
     */
    public ResolvingDecoder resolvingDecoder(Schema schema, Decoder wrapped) throws IOException {
        return new ResolvingDecoder(schema, wrapped);
    }

    private static class DefaultDecoderFactory extends DecoderFactory {
        @Override
        public DecoderFactory configureDecoderBufferSize(int size) {
            throw new IllegalStateException("This factory instance is immutable");
        }
    }
}
