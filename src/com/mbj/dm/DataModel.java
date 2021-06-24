package com.mbj.dm;

import java.util.Objects;

/**
 * represent the data that save in the memory and in the file
 * @param <T>
 */
public class DataModel<T> implements java.io.Serializable {


    private Long id;
    private T content;

    public DataModel(Long id, T content) {
        this.id = id;
        this.content = content;
    }


    public Long getDataModelId() {
        return id;
    }

    public void setDataModelId(Long id) {
        this.id = id;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    /**
     * equals between objects
     * @param o other object
     * @return if this object equal to the other object
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataModel)) return false;
        DataModel<?> dataModel = (DataModel<?>) o;
        return id.equals(dataModel.id) && content.equals(dataModel.content);
    }

    /**
     * hash code function
     * @return hash value
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, content);
    }

    /**
     * represent object in string
     * @return string
     */
    @Override
    public String toString() {
        return "DataModel{" +
                "id=" + id +
                ", content=" + content +
                '}';
    }
}
