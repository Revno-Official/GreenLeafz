package com.revno.greenleafz;


import android.graphics.Bitmap;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "history")
public class History {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "content")
    public String content;

    @ColumnInfo(name = "image")
    public String image;
}