package com.xqbase.baiji.generic;

import com.xqbase.baiji.exceptions.BaijiRuntimeException;
import com.xqbase.baiji.io.DatumReader;
import com.xqbase.baiji.io.Decoder;
import com.xqbase.baiji.schema.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A general purpose reader of data from Baiji streams.
 * This reader analyzes and resolves the reader and writer schemas
 * when constructed so that reads can be more efficient.
 * Once constructed, a reader can be reused or shared among threads
 * to avoid incurring more resolution costs.
 *
 * @author Tony He
 */
public abstract class PreResolvingDatumReader<T> implements DatumReader<T> {

    private final Schema schema;
    private final ItemReader reader;
    private final Map<Schema, ItemReader> recordReaders = new HashMap<>();

    protected PreResolvingDatumReader(Schema schema) {
        this.schema = schema;
        reader = resolveReader(schema);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T read(T reuse, Decoder decoder) throws IOException {
        return (T) reader.read(reuse, decoder);
    }

    protected abstract ArrayAccess getArrayAccess(ArraySchema schema);

    protected abstract EnumAccess getEnumAccess(EnumSchema schema);

    protected abstract MapAccess getMapAccess(MapSchema schema);

    protected abstract RecordAccess getRecordAccess(RecordSchema schema);

    /**
     * Build a reader that accounts for schema differences between the reader and writer schemas.
     */
    private ItemReader resolveReader(Schema schema) {
        switch (schema.getType()) {
            case NULL:
                return NullReader.INSTANCE;
            case BOOLEAN:
                return BooleanReader.INSTANCE;
            case INT:
                return IntReader.INSTANCE;
            case LONG:
                return LongReader.INSTANCE;
            case FLOAT:
                return FloatReader.INSTANCE;
            case DOUBLE:
                return DoubleReader.INSTANCE;
            case STRING:
                return StringReader.INSTANCE;
            case BYTES:
                return BytesReader.INSTANCE;
            case DATETIME:
                return DatetimeReader.INSTANCE;
            case RECORD:
                return resolveRecord((RecordSchema) schema);
            case ENUM:
                return resolveEnum((EnumSchema) schema);
            case ARRAY:
                return resolveArray((ArraySchema) schema);
            case MAP:
                return resolveMap((MapSchema) schema);
            case UNION:
                return resolveUnion((UnionSchema) schema);
            default:
                throw new BaijiRuntimeException("Unknown schema type: " + schema);
        }
    }

    private ItemReader resolveRecord(RecordSchema schema) {
        ItemReader recordReader = recordReaders.get(schema);
        if (recordReader != null) {
            return recordReader;
        }

        RecordAccess recordAccess = getRecordAccess(schema);
        List<FieldReader> fieldReaders = new ArrayList<FieldReader>();
        recordReader = new RecordReader(recordAccess, fieldReaders);
        recordReaders.put(schema, recordReader);

        for (Field field : schema) {
            ItemReader itemReader = resolveReader(field.getSchema());
            boolean reuse = isReusable(field.getSchema().getType());
            FieldReader fieldReader = new FieldReader(field, recordAccess, itemReader, reuse);
            fieldReaders.add(fieldReader);
        }

        return recordReader;
    }

    private ItemReader resolveEnum(EnumSchema schema) {
        EnumAccess enumAccess = getEnumAccess(schema);
        return new EnumReader(enumAccess);
    }

    private ItemReader resolveArray(ArraySchema schema) {
        ItemReader itemReader = resolveReader(schema.getItemSchema());
        ArrayAccess arrayAccess = getArrayAccess(schema);
        return new ArrayReader(arrayAccess, itemReader, isReusable(schema.getItemSchema().getType()));
    }

    private ItemReader resolveMap(MapSchema schema) {
        Schema valueSchema = schema.getValueSchema();
        ItemReader reader = resolveReader(valueSchema);
        MapAccess mapAccess = getMapAccess(schema);
        return new MapReader(mapAccess, reader);
    }

    private ItemReader resolveUnion(UnionSchema schema) {
        ItemReader[] lookup = new ItemReader[schema.size()];
        for (int i = 0; i < schema.size(); i++) {
            lookup[i] = resolveReader(schema.get(i));
        }
        return new UnionReader(lookup);
    }

    protected static interface ItemReader {
        Object read(Object reuse, Decoder dec) throws IOException;
    }

    private static class NullReader implements ItemReader {
        public static final NullReader INSTANCE = new NullReader();

        @Override
        public Object read(Object reuse, Decoder dec) throws IOException {
            dec.readNull();
            return null;
        }
    }

    private static class BooleanReader implements ItemReader {
        public static final BooleanReader INSTANCE = new BooleanReader();

        @Override
        public Object read(Object reuse, Decoder dec) throws IOException {
            return dec.readBoolean();
        }
    }

    private static class IntReader implements ItemReader {
        public static final IntReader INSTANCE = new IntReader();

        @Override
        public Object read(Object reuse, Decoder dec) throws IOException {
            return dec.readInt();
        }
    }

    private static class LongReader implements ItemReader {
        public static final LongReader INSTANCE = new LongReader();

        @Override
        public Object read(Object reuse, Decoder dec) throws IOException {
            return dec.readLong();
        }
    }

    private static class FloatReader implements ItemReader {
        public static final FloatReader INSTANCE = new FloatReader();

        @Override
        public Object read(Object reuse, Decoder dec) throws IOException {
            return dec.readFloat();
        }
    }

    private static class DoubleReader implements ItemReader {
        public static final DoubleReader INSTANCE = new DoubleReader();

        @Override
        public Object read(Object reuse, Decoder dec) throws IOException {
            return dec.readDouble();
        }
    }

    private static class StringReader implements ItemReader {
        public static final StringReader INSTANCE = new StringReader();

        @Override
        public Object read(Object reuse, Decoder dec) throws IOException {
            return dec.readString();
        }
    }

    private static class BytesReader implements ItemReader {
        public static final BytesReader INSTANCE = new BytesReader();

        @Override
        public Object read(Object reuse, Decoder dec) throws IOException {
            return dec.readBytes();
        }
    }

    private static class DatetimeReader implements ItemReader {
        public static final DatetimeReader INSTANCE = new DatetimeReader();

        @Override
        public Object read(Object reuse, Decoder dec) throws IOException {
            return dec.readDatetime();
        }
    }

    private static class RecordReader implements ItemReader {
        private final RecordAccess _recordAccess;
        private final List<FieldReader> _fieldReaders;

        public RecordReader(RecordAccess recordAccess, List<FieldReader> fieldReaders) {
            _recordAccess = recordAccess;
            _fieldReaders = fieldReaders;
        }

        @Override
        public Object read(Object reuse, Decoder dec) throws IOException {
            Object rec = _recordAccess.createRecord(reuse);
            for (FieldReader fr : _fieldReaders) {
                fr.read(rec, dec);
                // TODO: on exception, report offending field
            }
            return rec;
        }
    }

    private static class EnumReader implements ItemReader {
        private final EnumAccess _enumAccess;

        public EnumReader(EnumAccess enumAccess) {
            _enumAccess = enumAccess;
        }

        @Override
        public Object read(Object reuse, Decoder dec) throws IOException {
            return _enumAccess.createEnum(reuse, dec.readEnum());
        }
    }

    private static class ArrayReader implements ItemReader {
        private final ArrayAccess _arrayAccess;
        private final ItemReader _itemReader;
        private final boolean _reusable;

        public ArrayReader(ArrayAccess arrayAccess, ItemReader itemReader, boolean reusable) {
            _arrayAccess = arrayAccess;
            _itemReader = itemReader;
            _reusable = reusable;
        }

        @Override
        public Object read(Object reuse, Decoder dec) throws IOException {
            Object array = _arrayAccess.create(reuse);
            int i = 0;
            for (int n = (int) dec.readArrayStart(); n != 0; n = (int) dec.readArrayNext()) {
                array = _arrayAccess.ensureSize(array, i + n);
                _arrayAccess.addElements(array, n, i, _itemReader, dec, _reusable);
                i += n;
            }
            array = _arrayAccess.resize(array, i);
            return array;
        }
    }

    private static class MapReader implements ItemReader {
        private final MapAccess _mapAccess;
        private final ItemReader _valueReader;

        public MapReader(MapAccess mapAccess, ItemReader valueReader) {
            _mapAccess = mapAccess;
            _valueReader = valueReader;
        }

        @Override
        public Object read(Object reuse, Decoder dec) throws IOException {
            Object map = _mapAccess.create(reuse);
            for (int n = (int) dec.readMapStart(); n != 0; n = (int) dec.readMapNext()) {
                _mapAccess.addElements(map, n, _valueReader, dec, false);
            }
            return map;
        }
    }

    private static class UnionReader implements ItemReader {
        private final ItemReader[] _branchLookup;

        public UnionReader(ItemReader[] branchLookup) {
            _branchLookup = branchLookup;
        }

        @Override
        public Object read(Object reuse, Decoder dec) throws IOException {
            return _branchLookup[dec.readUnionIndex()].read(reuse, dec);
        }
    }

    /**
     * Read & set fields on a record
     */
    private static class FieldReader {
        private final Field _field;
        private final RecordAccess _recordAccess;
        private final ItemReader _itemReader;
        private final boolean _reuse;

        public FieldReader(Field field, RecordAccess recordAccess, ItemReader itemReader, boolean reuse) {
            _field = field;
            _recordAccess = recordAccess;
            _itemReader = itemReader;
            _reuse = reuse;
        }

        public void read(Object record, Decoder dec) throws IOException {
            Object reuseObj = null;
            if (_reuse) {
                _recordAccess.getField(record, _field.getName(), _field.getPos());
            }
            _recordAccess.addField(record, _field.getName(), _field.getPos(),
                    _itemReader.read(reuseObj, dec));
        }
    }

    /**
     * Indicates if it's possible to reuse an object of the specified type. Generally
     * false for immutable objects like int, long, string, etc but may differ between
     * the Specific and Generic implementations. Used to avoid retrieving the existing
     * value if it's not reusable.
     *
     * @param type
     * @return
     */
    protected boolean isReusable(SchemaType type) {
        return true;
    }

    // interfaces to handle details of working with Specific vs Generic objects

    protected static interface RecordAccess {
        /**
         * Creates a new record object. Derived classes can override this to return an object of their choice.
         *
         * @param reuse If appropriate, will reuse this object instead of constructing a new one
         * @return
         */
        Object createRecord(Object reuse);

        /**
         * Used by the default implementation of ReadRecord() to get the existing field of a record object. The derived
         * classes can override this to make their own interpretation of the record object.
         *
         * @param record    The record object to be probed into. This is guaranteed to be one that was returned
         *                  by a previous call to CreateRecord.
         * @param fieldName The name of the field to probe.
         * @param fieldPos  field number
         * @return The value of the field, if found. null otherwise.
         */
        Object getField(Object record, String fieldName, int fieldPos);

        /**
         * Used by the default implementation of ReadRecord() to add a field to a record object. The derived
         * classes can override this to suit their own implementation of the record object.
         *
         * @param record     The record object to be probed into. This is guaranteed to be one that was returned
         *                   by a previous call to CreateRecord.
         * @param fieldName  The name of the field to probe.
         * @param fieldPos   field number
         * @param fieldValue The value to be added for the field
         */
        void addField(Object record, String fieldName, int fieldPos, Object fieldValue);
    }

    protected static interface EnumAccess {
        Object createEnum(Object reuse, int ordinal);
    }

    protected static interface ArrayAccess {
        /// <summary>
        /// Creates a new array object. The initial size of the object could be anything.
        /// </summary>
        /// <param name="reuse">If appropriate use this instead of creating a new one.</param>
        /// <returns>An object suitable to deserialize a Baiji array</returns>
        Object create(Object reuse);

        /**
         * Hint that the array should be able to handle at least targetSize elements. The array
         * is not required to be resized
         *
         * @param array      Array object who needs to support targetSize elements. This is guaranteed to be something returned by
         *                   a previous call to CreateArray().
         * @param targetSize The new size.
         * @return
         */
        Object ensureSize(Object array, int targetSize);

        /**
         * Resizes the array to the new value.
         *
         * @param array      Array object whose size is required. This is guaranteed to be something returned by
         *                   a previous call to CreateArray().
         * @param targetSize The new size.
         * @return
         */
        Object resize(Object array, int targetSize);

        void addElements(Object array, int elements, int index, ItemReader itemReader, Decoder decoder, boolean reuse) throws IOException;
    }

    protected static interface MapAccess {
        /**
         * Creates a new map object.
         *
         * @param reuse If appropriate, use this map object instead of creating a new one.
         * @return An empty map object.
         */
        Object create(Object reuse);

        void addElements(Object map, int elements, ItemReader itemReader, Decoder decoder, boolean reuse) throws IOException;
    }
}
