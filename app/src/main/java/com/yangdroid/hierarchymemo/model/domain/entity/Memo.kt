package com.yangdroid.hierarchymemo.model.domain.entity

import java.util.*

data class Memo(
    var id: Long? = null,
    var parentId: Long? = null,
    var content: String,
    var childMemoContentList: List<String>,
    var createdDate: Date,
    var completedDate: Date? = null
)