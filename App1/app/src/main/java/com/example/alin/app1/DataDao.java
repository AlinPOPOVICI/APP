package com.example.alin.app1;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface DataDao {

    @Query("SELECT * FROM Data")
    List<Data> getAll();

    //@Query("SELECT * FROM Data where first_name LIKE  :firstName AND last_name LIKE :lastName")
   // Data findByName(String firstName, String lastName);

   // @Query("SELECT COUNT(*) from user")
   // int countUsers();

    @Insert
    void insert(Data... data);

    @Delete
    void delete(Data data);
}
