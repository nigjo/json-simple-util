/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  de.nigjo.json.util.JSONParameter
 */
package de.nigjo.json.util.test.data;

import de.nigjo.json.util.JSONParameter;
import de.nigjo.json.util.test.data.AutoGuiDefinition;
import de.nigjo.json.util.test.data.Value;
import java.util.List;
import java.util.Objects;

public class Group {
    @JSONParameter
    private String name;
    @JSONParameter
    private String title;
    @JSONParameter
    private String column;
    @JSONParameter
    private String row;
    @JSONParameter
    private List<Group> groups;
    @JSONParameter
    private List<Value> values;

    public String toString() {
        return "{\"name\":\"" + this.name + "\",\"title\":\"" + this.title + "\",\"column\":\"" + this.column + "\",\"row\":\"" + this.row + "\"" + (this.groups != null ? new StringBuilder().append(",\"groups\":").append(AutoGuiDefinition.printArray(this.groups)).toString() : "") + (this.values != null ? new StringBuilder().append(",\"values\":").append(AutoGuiDefinition.printArray(this.values)).toString() : "") + '}';
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColumn() {
        return this.column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getRow() {
        return this.row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public List<Group> getGroups() {
        return this.groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Value> getValues() {
        return this.values;
    }

    public void setValues(List<Value> values) {
        this.values = values;
    }

    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + Objects.hashCode(this.name);
        hash = 31 * hash + Objects.hashCode(this.title);
        hash = 31 * hash + Objects.hashCode(this.column);
        hash = 31 * hash + Objects.hashCode(this.row);
        hash = 31 * hash + Objects.hashCode(this.groups);
        hash = 31 * hash + Objects.hashCode(this.values);
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
        Group other = (Group)obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.column, other.column)) {
            return false;
        }
        if (!Objects.equals(this.row, other.row)) {
            return false;
        }
        if (!Objects.equals(this.groups, other.groups)) {
            return false;
        }
        if (!Objects.equals(this.values, other.values)) {
            return false;
        }
        return true;
    }
}

