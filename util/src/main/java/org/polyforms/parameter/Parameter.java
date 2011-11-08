package org.polyforms.parameter;

public class Parameter {
    private Class<?> type;
    private int index;
    private String name;

    public Parameter(final Class<?> type, final int index) {
        this.type = type;
        this.index = index;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(final Class<?> type) {
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(final int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Parameter [type=" + type + ", index=" + index + ", name=" + name + "]";
    }
}
