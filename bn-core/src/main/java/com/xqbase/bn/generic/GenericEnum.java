package com.xqbase.bn.generic;

import com.xqbase.bn.exceptions.BaijiRuntimeException;
import com.xqbase.bn.schema.EnumSchema;

/**
 * Created by tonyhe on 15-5-15.
 */
public class GenericEnum {

    private final EnumSchema _schema;

    private String _value;

    public GenericEnum(EnumSchema schema, String value) {
        _schema = schema;
        setValue(value);
    }

    public EnumSchema getSchema() {
        return _schema;
    }

    public String getValue() {
        return _value;
    }

    public void setValue(String value) {
        if (!_schema.contains(value)) {
            throw new BaijiRuntimeException("Unknown value for enum: " + value + "(" + _schema + ")");
        }
        _value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        return obj instanceof GenericEnum && _value.equals(((GenericEnum) obj)._value);
    }

    @Override
    public int hashCode() {
        return 17 * _value.hashCode();
    }

    @Override
    public String toString() {
        return "Schema: " + _schema + ", value: " + _value;
    }
}
