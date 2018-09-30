package com.example.yuseiatlas.estimotetest

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import android.text.method.ScrollingMovementMethod
import android.content.ClipData
import android.content.ClipboardManager
import android.widget.Toast
import com.estimote.indoorsdk.api.*
import kotlinx.android.synthetic.main.activity_estimote2.*


class EstimoteActivity : AppCompatActivity() {

    private val assetId: String by lazy {
        intent.getStringExtra(EXTRA_ASSET_ID)
    }

    private val locationId: String by lazy {
        intent.getStringExtra(EXTRA_LOCATION_ID)
    }
    private val indoorSDK by lazy {
        EstimoteIndoorSDK.create(this,
                "mutaz-kasem-s-your-own-app-acm",
                "7f564985db7be6a0f157785250a8f5cf")
    }
    private var trackingHandler: TrackingHandler? = null
    private var locationPresenter: EstimoteIndoorLocationPresenter? = null

    companion object {
        const val EXTRA_ASSET_ID = "EXTRA_ASSET_ID"
        const val EXTRA_LOCATION_ID = "EXTRA_LOCATION_ID"
        fun getStartIntent(context: Context, assetId: String, locationId: String): Intent {
            val intent = Intent(context, EstimoteActivity::class.java)
            intent.putExtra(EXTRA_ASSET_ID, assetId)
            intent.putExtra(EXTRA_LOCATION_ID, locationId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estimote2)

        // enable logTV scrolling
        logTV.movementMethod = ScrollingMovementMethod()

        // copy text to clipboard
        copyLogBTN.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label", logTV.text.toString())
            clipboard.primaryClip = clip
            Toast.makeText(this@EstimoteActivity, "Saved to clip board", Toast.LENGTH_SHORT).show()
        }

        addToLog("Starting...")

        /**
         * Build BLE-scanning indication notification. It well be shown in notification bar
         * when EstimoteIndoorSDK launch the tracking process.
         * It will ensure the tracking process will continue to work while application goes
         * to background
         */
        addToLog("Building notification...")
        val notification = createNotificationBuilder()
                .setSmallIcon(android.R.drawable.star_on)
                .setContentTitle("Estimote Test. \u00AE")
                .setContentText("Scan is running...")
                .build()
        addToLog("Notification built")

        /**
         * Start tracking process. Once this gets called, EstimoteIndoorSDK will start
         * to calculate user position within location and push calculated result in to
         * Estimote Cloud.
         */
        addToLog("Initializing tracking handler")
        trackingHandler = indoorSDK.trackPosition()
                .trackAs(assetId)
                .where(locatonWithIdentifier(locationId)) // ADNOCDist
                .withScannerInForegroundService(notification)
                .withErrorHandler {
                    addToLog("Error occurred while tracking position; cause: $it")
                    it.printStackTrace()
                }
                .start()
        addToLog("Tracking handler started")
        /**
         * Initiate indoor position presentation. Once this gets called, EstimoteIndoorSDK will
         * open persistent connection to Estimote Cloud. In response, Estimote Cloud will start
         * to notify EstimoteIndoorSDK about user position updates, zones' enter/exit events etc.
         */
        addToLog("Starting show position")
        with(indoorSDK
                .showPositions()
                .where(locatonWithIdentifier(locationId))
                .withErrorHandler {
                    addToLog("locationPresenter error: $it")
                    it.printStackTrace()
                }
                .build(getRenderer())) {
            addToLog("Started show position")
            start()
            showWalls()
            showAnchors()
            locationPresenter = this
        }

        addToLog("Finished onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        addToLog("Destroying activity")
        trackingHandler?.stopTracking()
        locationPresenter?.stop()
    }

    private fun getRenderer() =
            OnZoneEventActionHandlingRenderer(CustomEstimoteIndoorRenderer(applicationContext)) { zoneEvent ->
                addToLog(zoneEvent)
            }

    private fun createNotificationBuilder() =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                Notification.Builder(this, createNotificationChannel())
            else
                Notification.Builder(this)

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() =
            "Estimote Indoor Test".apply {
                with(NotificationChannel(this, "Scanning Service", NotificationManager.IMPORTANCE_NONE)) {
                    this.lightColor = Color.BLUE
                    this.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
                    val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    service.createNotificationChannel(this)
                }
            }

    // adds text to logTV
    private fun addToLog(text: String) {
        val previousText = logTV.text.toString()
        logTV.text = getString(R.string.log_text, previousText, text)
    }

    // adds zone event to the log
    private fun addToLog(zoneEvent: ZoneEvent) {
        addToLog("-------\nZONE EVENT:  $zoneEvent\n-------")
    }
}
