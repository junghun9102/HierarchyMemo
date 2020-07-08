package com.yangdroid.hierarchymemo.model.domain.entity

import java.util.*

data class Memo(
    val id: Long,
    var content: String,
    val childMemoList: List<Memo>,
    var createdDate: Date,
    var completedDate: Date?
)