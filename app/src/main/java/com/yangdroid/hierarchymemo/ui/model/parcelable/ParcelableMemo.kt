package com.yangdroid.hierarchymemo.ui.model.parcelable

import android.os.Parcel
import android.os.Parcelable
import com.yangdroid.hierarchymemo.model.domain.entity.Memo
import java.util.*

class ParcelableMemo (
    var id: Long?,
    var parentId: Long?,
    var content: String,
    val childMemoContentList: List<String>,
    var createdDate: Date,
    var completedDate: Date?,
    var isExpand: Boolean = false
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readString() ?: "",
        parcel.createStringArrayList() ?: emptyList(),
        parcel.readSerializable() as Date,
        parcel.readSerializable() as Date?,
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeValue(parentId)
        parcel.writeString(content)
        parcel.writeStringList(childMemoContentList)
        parcel.writeSerializable(createdDate)
        parcel.writeSerializable(completedDate)
        parcel.writeByte(if (isExpand) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ParcelableMemo> {
        override fun createFromParcel(parcel: Parcel): ParcelableMemo {
            return ParcelableMemo(parcel)
        }

        override fun newArray(size: Int): Array<ParcelableMemo?> {
            return arrayOfNulls(size)
        }
    }
}

fun Memo.toParcel() = ParcelableMemo(id, parentId, content, childMemoContentList, createdDate, completedDate)
fun ParcelableMemo.toEntity() = Memo(id, parentId, content, childMemoContentList, createdDate, completedDate)