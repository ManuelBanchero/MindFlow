package com.example.mindflow.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import com.example.mindflow.data.local.entity.IdeaEntity

@Dao interface IdeaDAO {
    @Insert suspend fun insert(idea: IdeaEntity)
}