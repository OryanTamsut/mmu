package com.mbj.services;

import com.mbj.algorithm.IAlgoCache;
import com.mbj.algorithm.LRUAlgoCacheImpl;
import com.mbj.dao.DaoFileImpl;
import com.mbj.dao.IDao;
import com.mbj.dm.DataModel;
import com.mbj.memory.CacheUnit;

import java.io.IOException;
import java.util.Map;

public class CacheUnitService<T> {

    private final CacheUnit<T> cacheUnit;
    private final IDao<Long, DataModel<T>> IDao;
    private int swapDisk;
    private int totalDataModelNum;

    /**
     *  constructor- initialize the cache unit and the memory json file
     * @param algo for the cache unit using
     */
    public CacheUnitService(IAlgoCache<Long, DataModel<T>> algo) {
        this.cacheUnit = new CacheUnit<>(algo);
        this.IDao = new DaoFileImpl<T>("memory.json");
        this.swapDisk=0;
        this.totalDataModelNum=0;
    }

    /**
     * delete data models from the file and from the cache
     * @param dataModels the data models to delete
     * @return true if not errors occurred during deletion,else- false
     */
    public boolean delete(DataModel<T>[] dataModels) throws IOException {
        boolean isDeleted=true;
        //delete the data models from the file
        for (DataModel<T> dataModel : dataModels) {
            this.totalDataModelNum++;
            IDao.delete(dataModel);

            //delete from the cache
            if (cacheUnit.getDataModels(new Long[]{dataModel.getDataModelId()})[0] != null) {
                cacheUnit.removeDataModels(new Long[]{dataModel.getDataModelId()});
            }
        }
        return isDeleted;
    }

    /**
     * get the data models from the cache, if not exist in the cache- transfer them to the cache from the file
     * @param dataModels to get them- and transfer to the cache from the disk
     * @return the data models
     */
    public DataModel<T>[] get(DataModel<T>[] dataModels) throws IOException {

        DataModel<T>[] dmToReturn = new DataModel[dataModels.length];
        DataModel<T> dmToSave;
        for (int i = 0; i < dataModels.length; i++) {
            this.totalDataModelNum++;
            //check if data model exist in the cache
            dmToReturn[i] = cacheUnit.getDataModels(new Long[]{dataModels[i].getDataModelId()})[0];
            if (dmToReturn[i] == null) {
                //if not exist check if exist in the file
                dmToReturn[i] = IDao.find(dataModels[i].getDataModelId());
                //if not exist in the file return the data model
                if (dmToReturn[i] == null) {
                    dmToReturn[i] = dataModels[i];
                    dmToSave = cacheUnit.putDataModels(new DataModel[]{dataModels[i]})[0];
                } else {
                    //if the data model found in the file- save it in the cache
                    // and save the data model that remove from the cache for him
                    dmToSave = cacheUnit.putDataModels(new DataModel[]{dmToReturn[i]})[0];
                }
                //if pop out a data model because the cache was full, save it in the file
                if (dmToSave != null) {
                    try {
                        IDao.save(dmToSave);
                        this.swapDisk++;
                    } catch (IOException e) {
                        System.out.println("error:" +  e.getMessage() );
                        throw (e);
                    }
                }
            }
        }
        return dmToReturn;
    }

    /**
     * update the data models in the cache or add them- if not exist
     * @param dataModels the data models to update
     * @return true if not errors occurred during updating, else- false
     */
    public boolean update(DataModel<T>[] dataModels) throws IOException {
        DataModel<T> dmToSave;
        boolean isUpdate = true;
        for (DataModel<T> dataModel : dataModels) {
            this.totalDataModelNum++;

            //check if the data model not exist in the cache unit
            if (cacheUnit.getDataModels(new Long[]{dataModel.getDataModelId()})[0] == null) {
                //add the data model to the cache and save the data model that was replaced with it
                dmToSave = cacheUnit.putDataModels(new DataModel[]{dataModel})[0];
                if (dmToSave != null) {
                    try {
                        IDao.save(dmToSave);
                        this.swapDisk++;
                    } catch (IOException e) {
                        isUpdate = false;
                        throw (e);
                    }
                }
            } else {
                //if exist in the data model- update it
                cacheUnit.removeDataModels(new Long[]{dataModel.getDataModelId()});
                cacheUnit.putDataModels(new DataModel[]{dataModel});
            }
        }
        return isUpdate;
    }

    /**
     * update the file from the cache unit memory
     */
    public void updateFile() {
        Map<Long,DataModel<T>> cacheMap=cacheUnit.getMemoryMap();
        for (Long key : cacheMap.keySet()) {
            try {
                IDao.save(cacheMap.get(key));
            } catch (IOException e) {
                System.out.println("error in save in file: "+e.getMessage());
            }
        }
    }

    public String showStatistics(){
        return "Total number of DataModels (GET/DELETE/UPDATE requests): "+ this.totalDataModelNum+";" +
                "Total number of DataModels swap (from Cache to Disk): "+ this.swapDisk+";";
    }

}

