package com.xqbase.baiji.generic;

import com.xqbase.baiji.io.DatumReader;
import com.xqbase.baiji.io.Decoder;
import com.xqbase.baiji.schema.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link com.xqbase.baiji.io.DatumReader} for generic Java objects.
 *
 * @author Tony He
 */
public class GenericDatumReader<D> extends PreResolvingDatumReader<D> {

    public GenericDatumReader(Schema schema) {
        super(schema);
    }

    @Override
    protected boolean isReusable(SchemaType type) {
        switch (type) {
            case DOUBLE:
            case BOOLEAN:
            case INT:
            case LONG:
            case FLOAT:
            case BYTES:
            case STRING:
            case NULL:
                return false;
        }
        return true;
    }

    @Override
    protected ArrayAccess getArrayAccess(ArraySchema schema) {
        return new GenericArrayAccess();
    }

    @Override
    protected EnumAccess getEnumAccess(EnumSchema schema) {
        return new GenericEnumAccess(schema);
    }

    @Override
    protected MapAccess getMapAccess(MapSchema schema) {
        return new GenericMapAccess();
    }

    @Override
    protected RecordAccess getRecordAccess(RecordSchema schema) {
        return new GenericRecordAccess(schema);
    }

    private static class GenericEnumAccess implements EnumAccess {
        private final EnumSchema _schema;

        public GenericEnumAccess(EnumSchema schema) {
            this._schema = schema;
        }

        @Override
        public Object createEnum(Object reuse, int ordinal) {
            if (reuse instanceof GenericEnum) {
                GenericEnum ge = (GenericEnum) reuse;
                if (ge.getSchema().equals(_schema)) {
                    ge.setValue(_schema.get(ordinal));
                    return ge;
                }
            }
            return new GenericEnum(_schema, _schema.get(ordinal));
        }
    }

    public static class GenericRecordAccess implements RecordAccess {
        private RecordSchema _schema;

        public GenericRecordAccess(RecordSchema schema) {
            _schema = schema;
        }

        @Override
        public Object createRecord(Object reuse) {
            GenericRecord ru = (!(reuse instanceof GenericRecord) || !((GenericRecord) reuse).getSchema().equals(_schema))
                    ? new GenericRecord(_schema)
                    : (GenericRecord) reuse;
            return ru;
        }

        @Override
        public Object getField(Object record, String fieldName, int fieldPos) {
            return ((GenericRecord) record).get(fieldName);
        }

        @Override
        public void addField(Object record, String fieldName, int fieldPos, Object fieldValue) {
            ((GenericRecord) record).add(fieldName, fieldValue);
        }
    }

    private static class GenericArrayAccess implements ArrayAccess {
        @Override
        public Object create(Object reuse) {
            return (reuse instanceof Object[]) ? reuse : new Object[0];
        }

        @Override
        public Object ensureSize(Object array, int targetSize) {
            if (((Object[]) array).length < targetSize) {
                array = sizeTo(array, targetSize);
            }
            return array;
        }

        @Override
        public Object resize(Object array, int targetSize) {
            return sizeTo(array, targetSize);
        }

        @Override
        public void addElements(Object arrayObj, int elements, int index, ItemReader itemReader, Decoder decoder,
                                boolean reuse) throws IOException {
            Object[] array = (Object[]) arrayObj;
            for (int i = index; i < index + elements; i++) {
                array[i] = reuse ? itemReader.read(array[i], decoder) : itemReader.read(null, decoder);
            }
        }

        private static Object sizeTo(Object array, int targetSize) {
            return Arrays.copyOf((Object[]) array, targetSize);
        }
    }

    private static class GenericMapAccess implements MapAccess {
        @Override
        public Object create(Object reuse) {
            if (reuse instanceof Map<?, ?>) {
                Map<?, ?> result = (Map<?, ?>) reuse;
                result.clear();
                return result;
            }
            return new HashMap<String, Object>();
        }

        @Override
        public void addElements(Object mapObj, int elements, ItemReader itemReader, Decoder decoder, boolean reuse)
                throws IOException {
            Map<String, Object> map = (Map<String, Object>) mapObj;
            for (int i = 0; i < elements; i++) {
                String key = decoder.readString();
                map.put(key, itemReader.read(null, decoder));
            }
        }
    }
}
