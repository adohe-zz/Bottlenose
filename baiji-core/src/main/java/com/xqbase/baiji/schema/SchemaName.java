package com.xqbase.baiji.schema;

/**
 * Schema Name Class.
 *
 * @author Tony He
 */
public class SchemaName {

    private final String name;
    private final String space;
    private final String encSpace;
    private final String fullName;

    public SchemaName(String name, String space, String encSpace) {
        if (name == null) {                         // anonymous
            this.name = this.space = null;
            this.encSpace = encSpace;
            this.fullName = null;
            return;
        }
        int lastDot = name.lastIndexOf('.');
        if (lastDot < 0) {                          // unqualified name
            this.name = name;
            this.space = space;
            this.encSpace = encSpace;
        } else {                                    // qualified name
            this.space = name.substring(0, lastDot);
            this.name = name.substring(lastDot + 1, name.length());
            this.encSpace = encSpace;
        }
        String namespace = getNamespace();
        fullName = namespace != null && namespace.length() != 0 ? namespace + "." + name : name;
    }

    public String getName() {
        return name;
    }

    public String getSpace() {
        return space;
    }

    public String getEncSpace() {
        return encSpace;
    }

    public String getNamespace() {
        return space != null && space.length() != 0 ? space : encSpace;
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
