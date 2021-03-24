package com.team12.myopensecret

import java.io.Serializable

data class JournalDataEntry(var title:String, var description:String, var labels:ArrayList<String>) : Serializable
