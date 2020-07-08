package com.yangdroid.hierarchymemo.model.local.mapper

import com.yangdroid.hierarchymemo.model.domain.entity.Memo
import com.yangdroid.hierarchymemo.model.local.dto.MemoDTO

class MemoMapper : EntityMapper<MemoDTO, Memo> {

    override fun mapToEntity(local: MemoDTO): Memo {
        return Memo(
            local.id,
            local.parentId,
            local.content,
            emptyList(),
            local.createdDate,
            local.completedDate
        )
    }

    override fun mapToLocal(entity: Memo): MemoDTO {
        return MemoDTO(
            entity.id,
            entity.parentId,
            entity.content,
            entity.createdDate,
            entity.completedDate
        )
    }

}