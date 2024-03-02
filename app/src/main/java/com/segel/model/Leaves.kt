package com.segel.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "leaves")
data class Leaves(
    @PrimaryKey
    val id:Int,val type:Int,val duration_type:Int,val start_date:String,val end_date:String,val reason:String)