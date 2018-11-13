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
    void insertContests(List<ContestEntry> contests); //bulk insert

    @Query("select * from contest where eventType=:type")
    List<ContestEntry> getContestsByType(int type);

    @Query("select * from contest where id=:id")
    ContestEntry getContestById(int id);

    @Query("delete from contest")
    void deleteAllContests();
}
