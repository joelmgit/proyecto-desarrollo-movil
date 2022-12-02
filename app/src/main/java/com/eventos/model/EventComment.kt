package com.eventos.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EventComment (
    var id: String,
    var authorId: String,
    var authorName: String,
    var body: String,
    var latitude: Double,
    var longitude: Double,
    var altitude: Double,
    var commentDateTime:String,
) : Parcelable {
    constructor() :
            this("",
                "",
                "",
                "",
                0.0,
                0.0,
                0.0,
                "",
                )
}