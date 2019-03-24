package com.example.alin.app1.DB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface AplicatieDataDao {

    @Query("SELECT * FROM AplicatieData")
        //LiveData<List<Aplicatie>> getAll();
    List<AplicatieData> getAll();

    //@Query("SELECT * FROM Data where first_name LIKE  :firstName AND last_name LIKE :lastName")
    // Data findByName(String firstName, String lastName);

    // @Query("SELECT COUNT(*) from user")
    // int countUsers();

    @Insert
    void insert(AplicatieData... data);

    @Delete
    void delete(AplicatieData data);

    @Query("DELETE FROM AplicatieData")
    void deleteAll();
}
