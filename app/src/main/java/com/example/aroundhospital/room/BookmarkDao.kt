package com.example.aroundhospital.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.aroundhospital.response.Document
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM book")
    fun getAll() : Flow<List<Document>>

    @Delete
    fun delete(entity: Document)

    // 동일한 자료가 있을시 REPLACE
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: Document)

    @Update
    fun update(memo: Document)
}