package com.yangdroid.hierarchymemo.model.local.mapper

interface EntityMapper<L, E> {
    fun mapToEntity(local: L): E
    fun mapToLocal(entity: E): L
}