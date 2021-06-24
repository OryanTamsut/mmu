package com.mbj.services;

import com.mbj.algorithm.IAlgoCache;
import com.mbj.algorithm.LRUAlgoCacheImpl;
import com.mbj.dm.DataModel;

import java.io.IOException;
import java.util.Map;

public class CacheUnitController<T> {

    private final CacheUnitService<T> cacheUnitService;
    private int counterRequest;

    final int capacity=30;
    /**
     * constructor- initialize the cache unit service with 3 capacity and LRU algorithm
     */
    public CacheUnitController() {
        IAlgoCache<Long, DataModel<T>> algo = new LRUAlgoCacheImpl<>(capacity);
        this.cacheUnitService = new CacheUnitService<>(algo);
    }

    /**
     * delete data models
     * @param dataModels data models to delete
     * @return true if not errors occurred during deletion,else- false
     */
    public synchronized boolean delete(DataModel<T>[] dataModels) throws IOException {
        this.counterRequest++;
        return this.cacheUnitService.delete(dataModels);
    }

    /**
     * get data models
     * @param dataModels to get them- and transfer to the cache from the disk
     * @return the data models
     */
    public synchronized DataModel<T>[] get(DataModel<T>[] dataModels) throws IOException {
        this.counterRequest++;
        return this.cacheUnitService.get(dataModels);
    }

    /**
     * update the data models
     * @param dataModels to update or to add
     * @return  true if not errors occurred during updating, else- false
     */
    public synchronized boolean update(DataModel<T>[] dataModels) throws IOException {
        this.counterRequest++;
        return this.cacheUnitService.update(dataModels);
    }

    /**
     * update the memory file from the cache
     */
    public synchronized void updateFile() {
        this.cacheUnitService.updateFile();
    }

    public synchronized String showStatistics(){
        return "Capacity: "+capacity+";"+
                "Algorithm: LRU;"+
                "Total number of requests: "+this.counterRequest+";"+
                this.cacheUnitService.showStatistics();
    }
}
