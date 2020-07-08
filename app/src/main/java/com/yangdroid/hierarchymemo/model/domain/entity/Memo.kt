package com.yangdroid.hierarchymemo.model.domain.entity

import java.util.*

data class Memo(
    val id: Long?,
    var parentId: Long?,
    var content: String,
    var childMemoContentList: List<String>,
    var createdDate: Date,
    var completedDate: Date?
)