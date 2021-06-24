package com.mbj.dao;

import java.io.IOException;

/**
 * interface that represents the memory unit
 * @param <ID> id of the memory entity
 * @param <T> the content of the memory entity
 */
public interface IDao <ID extends java.io.Serializable,T>{

    /**
     * save the entity in the memory unit
     * @param entity the memory entity
     */
    public void save(T entity) throws IOException;

    /**
     * delete entity from the memory unit
     * @param entity the memory entity
     * @throws IllegalArgumentException if the argument is null
     */
    public void delete(T entity) throws IllegalArgumentException, IOException;

    /**
     *
     * @param id the id of the memory entity
     * @return the value of the memory entity
     * @throws IllegalArgumentException if the id is null
     */
    public T find(ID id) throws IllegalArgumentException;
}
