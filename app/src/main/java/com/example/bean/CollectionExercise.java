package com.example.bean;

import org.litepal.crud.LitePalSupport;

public class CollectionExercise extends LitePalSupport {
    private String collectionExerciseId;

    private String collectionId;

    private String exerciseId;

    public String getCollectionExerciseId() {
        return collectionExerciseId;
    }

    public void setCollectionExerciseId(String collectionExerciseId) {
        this.collectionExerciseId = collectionExerciseId;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public String getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(String exerciseId) {
        this.exerciseId = exerciseId;
    }
}
