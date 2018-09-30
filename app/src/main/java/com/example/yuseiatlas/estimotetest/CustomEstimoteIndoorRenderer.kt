package com.example.yuseiatlas.estimotetest

import android.content.Context
import android.util.Log
import com.estimote.indoorsdk.api.*

class CustomEstimoteIndoorRenderer(private val context: Context): EstimoteIndoorRenderer  {
    private val LOGTAG = EstimoteActivity::class.java.simpleName

    override fun hideAsset(assetIdentifier: String) {

    }

    override fun hideGeoJsonLayer(geoJsonString: String) {
    }

    override fun onAnchorEvent(event: AnchorEvent) {
    }

    override fun onAssetOutOfLocation(positionUpdate: AssetPositionUpdate.OutOfLocationAssetPosition) {
    }

    override fun onAssetPositionUpdate(positionUpdate: AssetPositionUpdate.InLocationAssetPositionUpdate) {
    }

    override fun onObstacleEvent(event: ObstacleEvent) {
    }

    override fun onWallEvent(event: WallEvent) {
    }

    override fun onZoneEvent(event: ZoneEvent) {
        Log.v(LOGTAG, "handling: $event")
    }

    override fun setup(assetProfileDataSource: EstimoteAssetProfileDataSource, onAssetClickedListener: (AssetProfileData) -> Unit, styleResolver: EstimoteIndoorPresenterStyleResolver, layers: EstimoteIndoorLocationGeoJsonLayers) {

    }

    override fun showAsset(assetIdentifier: String) {

    }

    override fun showImageLayer(northEastLatitude: Double, northEastLongitude: Double, southWestLatitude: Double, southWestLongitude: Double, imageUrl: String) {
    }

    override fun zoomTo(latitude: Double, longitude: Double, zoomLevel: Float) {
    }
}