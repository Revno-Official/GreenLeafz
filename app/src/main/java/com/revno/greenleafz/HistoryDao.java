package com.revno.greenleafz;

import android.graphics.Bitmap;
import android.provider.ContactsContract;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface HistoryDao {
    @Query("INSERT INTO history (content,image) VALUES ('New note', '//.')")
    void create();

    //@Query("SELECT * FROM history")
    //ArrayList< History> getAllHistory();

    @Query("UPDATE history SET content = :content AND image = :image WHERE id = :id")
    void save(String content, String image, int id);

    @Query("DELETE FROM history WHERE id = :id")
    void delete(int id);
}