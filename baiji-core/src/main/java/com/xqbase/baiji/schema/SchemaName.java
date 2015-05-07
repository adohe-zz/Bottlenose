package com.xqbase.baiji.schema;

/**
 * This class represents the name of {@link NamedSchema}.
 *
 * @author Tony He
 */
public class SchemaName {

    private final String name;
    private final String namespace;
    private final String fullName;

    public SchemaName(String name, String namespace) {
        if (name == null) {                         // anonymous
            this.name = this.namespace = this.fullName = null;
            return;
        }
        int lastDot = name.lastIndexOf('.');
        if (lastDot < 0) {                          // unqualified name
            this.name = name;
        } else {                                    // qualified name
            this.name = name.substring(lastDot + 1, name.length());
            namespace = name.substring(0, lastDot);
        }
        if ("".equals(namespace))
            namespace = null;
        this.namespace = namespace;
        this.fullName = (this.namespace == null) ? this.name : this.namespace + "." + this.name;
    }

    public String getName() {
        return name;
    }

    public String getNameSpace() {
        return namespace;
    }

    public String getFullName() {
        return fullName;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof SchemaName)) return false;
        SchemaName that = (SchemaName) o;
        return fullName == null ? that.fullName == null : fullName.equals(that.fullName);
    }

    public int hashCode() {
        return fullName == null ? 0 : fullName.hashCode();
    }

    public String toString() {
        return fullName;
    }
}
