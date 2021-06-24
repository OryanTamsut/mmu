package com.mbj.dao;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.mbj.dm.DataModel;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

/**
 * implement the IDao interface
 * @param <T>
 */
public class DaoFileImpl<T> implements IDao<Long, DataModel<T>> {

    //map that save in the class the memory that stored in the file
    private List< DataModel<T>> memoryArray;

    //path of the file that stores the memory entities
    private final String filePath;

    //the capacity of the file, in default- 100
    private final int capacity;

    //save how mach entities we stored
    private int full = 0;

    /**
     * contractor , default capacity-100, and load the data to the memory map
     * @param filePath path of the file that stores the memory entities
     */
    public DaoFileImpl(String filePath) {
        this.capacity = 100;
        this.filePath = filePath;
        loadData();
    }

    /**
     * contractor and load the data to the memory map
     * @param filePath path of the file that stores the memory entities
     * @param capacity capacity of the file
     */
    public DaoFileImpl(String filePath, int capacity) {
        this.capacity = capacity;
        this.filePath = filePath;
        loadData();
    }

    /**
     * save a DataModel entity in the file, if the memory not full
     * @param entity the memory entity
     */
    public void save(DataModel<T> entity) throws IOException {
        if (full == capacity) {
            System.out.println("the memory is full, can't add a new entity");
            return;
        }
        try {
            if(find(entity.getDataModelId())!=null){
                delete(entity);
            }
            memoryArray.add(entity);
            saveData();
            full++;
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage() + "can't save new entity");
            throw e;
        }
    }


    /**
     * delete entity from the memory file
     * @param entity the memory entity
     * @throws IllegalArgumentException if the entity is null
     */
    public void delete(DataModel<T> entity) throws IllegalArgumentException, IOException {
        if (entity == null) {
            throw new IllegalArgumentException("entity can't be null");
        }
        int lengthBefore = memoryArray.size();
        try {
            memoryArray.removeIf(item -> item.getDataModelId().equals(entity.getDataModelId()));
            if (lengthBefore != memoryArray.size()) {
                full--;
                saveData();
            }
            else {
                throw new IllegalArgumentException("can not remove the entity from the file: entity do not exist");
            }

        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage() + "can not delete the entity");
            throw e;
        }
    }

    /**
     * find the memory entity According to the entity's id
     * @param id the id of the memory entity
     * @return the entity if it exist, else return null
     * @throws IllegalArgumentException if id is null
     */
    public DataModel<T> find(Long id) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("id can not be null");
        }
        for (DataModel<T> tDataModel : memoryArray) {
            if (id.equals(tDataModel.getDataModelId())) {
                return tDataModel;
            }
        }
        return null;
    }

    /**
     *load the data from the file to the memory map
     */
    private void loadData() {

        Gson gson = new Gson();
        try {
            Type listType = new TypeToken<ArrayList<DataModel<T>>>() {
            }.getType();
            //read the file
            FileReader fileReader = new FileReader("src/main/resources/"+filePath);
            //convert to list
            ArrayList<DataModel<T>> fileArray = gson.fromJson(fileReader, listType);
            if (fileArray != null) {
                memoryArray = fileArray;
            }
            fileReader.close();
;        } catch (IOException e) {
            memoryArray=new ArrayList<DataModel<T>>(capacity);
        }
    }


    /**
     * save the memory map to the JSON file
     * @throws IOException if was exception during the saving data
     */
    private void saveData() throws IOException {

        File file = new File("src/main/resources/" + filePath);
        PrintWriter emptyFile = new PrintWriter(file);
        emptyFile.print("");
        emptyFile.close();
        Gson gson = new Gson();
        String cust = gson.toJson(memoryArray);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(cust);
        fileWriter.close();
    }

}

