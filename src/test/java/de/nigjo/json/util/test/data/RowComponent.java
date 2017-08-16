/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  de.nigjo.json.util.JSONParameter
 */
package de.nigjo.json.util.test.data;

import de.nigjo.json.util.JSONParameter;
import java.util.Objects;

public class RowComponent {
    @JSONParameter
    private String column;
    @JSONParameter
    private String type;
    @JSONParameter
    private Object value;

    public String getColumn() {
        return this.column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.column);
        hash = 23 * hash + Objects.hashCode(this.type);
        hash = 23 * hash + Objects.hashCode(this.value);
        return hash;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        RowComponent other = (RowComponent)obj;
        if (!Objects.equals(this.column, other.column)) {
            return false;
        }
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "{\"column\":\"" + this.column + "\",\"type\":\"" + this.type + "\",\"value\":" + (this.value instanceof String ? new StringBuilder().append("\"").append(this.value).append('\"').toString() : this.value) + '}';
    }
}

