package com.yangdroid.hierarchymemo.model.domain.entity

import java.util.*

data class Memo(
    private val id: Long,
    private var content: String,
    private val childMemoList: List<Memo>,
    private var lastModifiedDate: Date,
    private var completedDate: Date
)