package com.team12.myopensecret

import android.graphics.Color
import java.io.Serializable

data class DataFieldData(var alwaysAdd:String, var name:String, var type:String, var value:String?, var dbId:Int?, var dfId:Int) : Serializable
