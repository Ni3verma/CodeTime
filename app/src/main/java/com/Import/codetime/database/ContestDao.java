package com.Import.codetime.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ContestDao {
    @Query("select * from contest")
    List<ContestEntry> getAllContests();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertContests(ContestEntry... contests); //bulk insert
}
