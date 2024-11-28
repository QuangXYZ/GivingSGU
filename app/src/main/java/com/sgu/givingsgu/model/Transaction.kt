package com.sgu.givingsgu.model

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

data class Transaction(
    val id: Long? = null,
    val donationId: Long,
    val amount: Double,
    val transactionDate: Date,
    val paymentMethod: String,
    val status: String,
    val token: String,
    val transactionId: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readLong(),
        parcel.readDouble(),
        Date(parcel.readLong()),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeLong(donationId)
        parcel.writeDouble(amount)
        parcel.writeString(paymentMethod)
        parcel.writeString(status)
        parcel.writeString(token)
        parcel.writeString(transactionId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Transaction> {
        override fun createFromParcel(parcel: Parcel): Transaction {
            return Transaction(parcel)
        }

        override fun newArray(size: Int): Array<Transaction?> {
            return arrayOfNulls(size)
        }
    }
}