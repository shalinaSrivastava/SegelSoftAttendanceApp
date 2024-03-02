package com.segel.room

import androidx.room.*
import com.segel.model.Profile
@Dao
interface ProfileDAO {
    @Query("SELECT * FROM profile")
    fun getAll(): List<Profile>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: Profile)

    @Update
    fun update(user: Profile)

    @Delete
    fun delete(user: Profile)
}