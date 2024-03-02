package com.segel.room

import androidx.room.*
import com.segel.model.Leaves
@Dao
interface LeavesDAO {
    @Query("SELECT * FROM leaves")
    fun getAll(): List<Leaves>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(leave: List<Leaves>)

    @Update
    fun update(leave: Leaves)

    @Delete
    fun delete(leave: Leaves)
}