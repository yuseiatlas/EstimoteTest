package com.example.yuseiatlas.estimotetest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        assetET.setText(getString(R.string.asset_id))
        locationIdET.setText(getString(R.string.location_id))

        continueBTN.setOnClickListener {
            if (assetET.text.isNullOrBlank()) {
                Toast.makeText(this, getString(R.string.asset_id_hint), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (locationIdET.text.isNullOrBlank()) {
                Toast.makeText(this, getString(R.string.location_id_hint), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            launchEstimote()
        }
    }

    // checks locations permissions & Bluetooth status/permissions before launching EstimoteActivity
    private fun launchEstimote() {
        val requirementsWizard = RequirementsWizardFactory.createEstimoteRequirementsWizard()
        requirementsWizard
                .fulfillRequirements(this,
                        { startActivity(EstimoteActivity.getStartIntent(this, assetET.text.toString(), locationIdET.text.toString())) },
                        { Toast.makeText(this, "Unable to start BT advertising doe to missing requirements: $it", Toast.LENGTH_LONG).show() },
                        { Toast.makeText(this, "Error occurred while obtaining BT permissions: $it", Toast.LENGTH_LONG).show() })
    }
}
