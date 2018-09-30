package com.example.yuseiatlas.estimotetest

import com.estimote.indoorsdk.api.EstimoteIndoorRenderer
import com.estimote.indoorsdk.api.ZoneEvent

class OnZoneEventActionHandlingRenderer(private val renderer: EstimoteIndoorRenderer,
                                        private val action: (ZoneEvent) -> Unit): EstimoteIndoorRenderer by renderer {
    override fun onZoneEvent(event: ZoneEvent) {
        action(event)
        renderer.onZoneEvent(event)
    }
}