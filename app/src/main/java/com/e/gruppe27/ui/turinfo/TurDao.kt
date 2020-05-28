package com.e.gruppe27.ui.turinfo

import androidx.lifecycle.LiveData
import androidx.room.*
import com.e.gruppe27.model.Tur


@Dao
interface TurDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(tur : Tur)

    @Delete
    fun delete(tur : Tur)

    @Query("SELECT * FROM tur_table")
    fun getTurer() : LiveData<List<Tur>>

}