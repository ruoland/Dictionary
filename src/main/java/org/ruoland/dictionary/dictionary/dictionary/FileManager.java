package org.ruoland.dictionary.dictionary.dictionary;


import org.ruoland.dictionary.dictionary.dictionary.developer.category.Data;

import java.util.ArrayList;

public class FileManager implements Data {
    private static ArrayList<String> BLACK_LIST = new ArrayList<>();
    private static final FileManager FILE_MANAGER = new FileManager();
    public static FileManager getInstance() {
        return FILE_MANAGER;
    }
    @Override
    public void init() {
        Data.super.init();
    }


}
