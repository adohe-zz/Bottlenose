package com.xqbase.baiji.io;

import java.io.IOException;

/** Read data of a schema.
 * <p>Determines the in-memory data representation.
 *
 * @author Tony He
 */
public interface DatumReader<D> {

    /** Read a datum.  Traverse the schema, depth-first, reading all leaf values
     * in the schema into a datum that is returned.  If the provided datum is
     * non-null it may be reused and returned.
     */
    D read(D reuse, Decoder in) throws IOException;
}
