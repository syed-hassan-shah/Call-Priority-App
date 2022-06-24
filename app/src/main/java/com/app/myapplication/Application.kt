package com.app.myapplication

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@HiltAndroidApp
class Application: Application() {
}
inline fun <reified T> logger(marker: String? = null): Logger =
    logger(T::class.java.simpleName, marker)

fun logger(tag: String = "", marker: String? = null): Logger =
    LoggerFactory.getLogger(tag.run { marker?.let { "$this -> $it" } ?: this })
