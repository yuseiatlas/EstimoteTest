package com.example.yuseiatlas.estimotetest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.crash.FirebaseCrash
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
            startActivity(EstimoteActivity.getStartIntent(this, assetET.text.toString(), locationIdET.text.toString()))
        }
    }
}
