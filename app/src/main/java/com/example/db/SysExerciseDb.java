package com.example.db;

import com.example.bean.SysExercise;

import org.litepal.LitePal;

import java.util.List;

public class SysExerciseDb {

    public SysExerciseDb() {
        LitePal.getDatabase();
    }

    //存储所有题目
    public void saveExerciseList(List<SysExercise> exerciseList) {
        LitePal.saveAll(exerciseList);
    }

    //存储一个题目
    public void save(SysExercise exercise) {
        exercise.save();
    }

    //删除
    public void deleteAll() {
        LitePal.deleteAll(SysExercise.class);
    }

    //获得所有
    public List<SysExercise> getAllExercise() {
        return LitePal.findAll(SysExercise.class);
    }

}
