package com.eventos.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EventPost (
    var id: String,
    var name: String,
    var organizerId: String,
    var organizerName: String,
    var phoneNumber: String,
    var description: String,
    var latitude: Double,
    var longitude: Double,
    var altitude: Double,
    var eventDate:String,
    var activities:String,
) : Parcelable {
    constructor() :
            this("",
                "",
                "",
                "",
                "",
                "",
                0.0,
                0.0,
                0.0,
                "",
                "")
}