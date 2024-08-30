package org.ruoland.dictionary.dictionary.dictionary.manager;


import org.ruoland.dictionary.dictionary.dictionary.developer.category.Data;

public class FileManager implements Data {
    private static final FileManager FILE_MANAGER = new FileManager();
    public static FileManager getInstance() {
        return FILE_MANAGER;
    }
    @Override
    public void init() {
        Data.super.init();
    }


}
