package com.team12.myopensecret

import android.graphics.Color
import java.io.Serializable

data class LabelData(var name:String, var color:Color, var dbId:Int) : Serializable
