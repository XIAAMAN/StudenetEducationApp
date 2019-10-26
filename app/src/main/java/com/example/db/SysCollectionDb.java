package com.example.db;

import com.example.bean.SysCollection;

import org.litepal.LitePal;

import java.util.List;

public class SysCollectionDb {

    public SysCollectionDb() {
        LitePal.getDatabase();
    }

    public void deleteAll() {
        LitePal.deleteAll(SysCollection.class);
    }

    public List<SysCollection> getCollectionList() {
        return LitePal.findAll(SysCollection.class);
    }
    public void saveCollectionList(List<SysCollection> collectionList) {
        LitePal.saveAll(collectionList);
    }
}
