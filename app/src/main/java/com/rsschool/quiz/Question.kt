package com.rsschool.quiz

import android.os.Parcel
import android.os.Parcelable

data class Question(
    val trueVariant: Int = -1,
    var selected: Int = -1,
    val text: String? = "question",
    val arrVariants: Array<String> = arrayOf("1","2","3","4","5") ) :
    Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.createStringArray() as Array<String>
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Question

        if (trueVariant != other.trueVariant) return false
        if (selected != other.selected) return false
        if (text != other.text) return false
        if (!arrVariants.contentEquals(other.arrVariants)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = trueVariant
        result = 31 * result + selected
        result = 31 * result + text.hashCode()
        result = 31 * result + arrVariants.contentHashCode()
        return result
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(trueVariant)
        parcel.writeInt(selected)
        parcel.writeString(text)
        parcel.writeStringArray(arrVariants)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Question> {
        override fun createFromParcel(parcel: Parcel): Question {
            return Question(parcel)
        }

        override fun newArray(size: Int): Array<Question?> {
            return arrayOfNulls(size)
        }
    }
}

