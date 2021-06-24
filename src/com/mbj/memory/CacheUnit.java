package com.mbj.memory;

import com.mbj.algorithm.IAlgoCache;
import com.mbj.dm.DataModel;

import java.util.Map;

/**
 * represnt the cache unit
 * @param <T>
 */
public class CacheUnit<T> {
    //algorithm that use in order to menage the cache unit
    private IAlgoCache<Long, DataModel<T>> algo;

    public CacheUnit(IAlgoCache<Long, DataModel<T>> algo) {
        this.algo = algo;
    }

    /**
     * get a array of dataModels according to their ids
     * @param ids of the dataModels to return
     * @return array of data models
     */
    public DataModel<T>[] getDataModels(Long[] ids) {
        DataModel<T>[] dmToReturn = new DataModel[ids.length];
        for (int i = 0; i < ids.length; i++) {
            dmToReturn[i] = algo.getElement(ids[i]);

        }
        return dmToReturn;
    }

    /**
     * put array of data models
     * @param datamodels
     * @return array of the element that return from the chache
     */
    public DataModel<T>[] putDataModels(DataModel<T>[] datamodels) {
        DataModel<T>[] dmToReturn = new DataModel[datamodels.length];
        for (int i = 0; i < datamodels.length; i++) {
            dmToReturn[i] = algo.putElement(datamodels[i].getDataModelId(), datamodels[i]);
        }
        return dmToReturn;
    }

    /**
     * remove dataModels according to their ids
     * @param ids
     */
    public void removeDataModels(Long[] ids) {
        for (Long id : ids) {
            algo.removeElement(id);
        }
    }

    /**
     * @return all the memory that saved in the cache
     */
    public Map<Long,DataModel<T> > getMemoryMap(){
        return algo.getMemoryMap();
    }
}
