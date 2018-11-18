package com.Import.codetime.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FavouriteDao {
    @Query("select * from favourite")
    List<FavouriteEntry> getAllFavContests();

    @Insert
    void insertFavouriteContest(FavouriteEntry contest);

    @Query("select * from favourite where contestId=:cid")
    FavouriteEntry getFavContestByContestId(int cid);
}
