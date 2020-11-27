package com.revno.greenleafz;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {History.class}, version = 1)
public abstract class HistoryDatabase extends RoomDatabase {
    public abstract HistoryDao historyDao();
}