package com.yangdroid.hierarchymemo.model.local

import android.content.Context
import androidx.room.*
import com.yangdroid.hierarchymemo.model.local.converter.Converters
import com.yangdroid.hierarchymemo.model.local.dao.MemoDAO
import com.yangdroid.hierarchymemo.model.local.dto.MemoDTO

@Database(entities = [MemoDTO::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun memoDAO(): MemoDAO

    companion object {
        private var uniqueInstance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context) = uniqueInstance ?: run {
            uniqueInstance = Room.databaseBuilder(context, AppDatabase::class.java, "hierarchy_memo.db").build()
            uniqueInstance!!
        }
    }

}