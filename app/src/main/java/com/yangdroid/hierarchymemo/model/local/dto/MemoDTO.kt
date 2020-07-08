package com.yangdroid.hierarchymemo.model.local.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "memo_table")
class MemoDTO (
    @PrimaryKey(autoGenerate = true) val id: Long?,
    val parentId: Long?,
    val content: String,
    val createdDate: Date,
    val completedDate: Date?
)