package com.example.yuseiatlas.estimotetest

import android.app.Application
import com.wafel.skald.api.LogLevel
import com.wafel.skald.api.skald
import com.wafel.skald.plugins.logcat.toLogcat

class EstimoteTest : Application() {
    override fun onCreate() {
        super.onCreate()
        skald {
            writeSaga {
                toLogcat { withTag { "INDOOR-SDK" } }
                withLevel { LogLevel.TRACE }
                withPattern { "${it.simplePath} :: ${it.message}" }
            }
        }
    }
}