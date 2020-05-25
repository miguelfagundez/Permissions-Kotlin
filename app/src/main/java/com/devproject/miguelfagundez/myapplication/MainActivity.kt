package com.devproject.miguelfagundez.myapplication

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    // Permission code
    private val PERMISSION_CODE = 1001
    var permissionGranted = PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupListeners()
    }

    private fun setupListeners() {
        btnShowPermissions.setOnClickListener {
            if (checkingPermissions()) {
                tvPermissionText.setText("Permissions Granted. Thank you!!")
            } else {
                tvPermissionText.setText("Permissions Not Granted..")
                requestMyPermissions()
            }
        }
    }

    //*****************************************
    // Checking if permissions were granted
    //*****************************************
    private fun checkingPermissions(): Boolean {

        // if both permissions are granted, return true
        return if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == permissionGranted
                && checkSelfPermission(Manifest.permission.CAMERA) == permissionGranted) true else false

        // if one permission or both were not granted, then return false
    }

    //**************************************
    // if permissions were not granted
    // then I request the permissions again
    //**************************************
    private fun requestMyPermissions() {
        if (checkingPermissions()) {
            //Permission were granted
            tvPermissionText.setText("Permissions Granted. Thank you!!")
        } else {

            // Request the permissions
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                    shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                // show a dialog
                AlertDialog.Builder(this)
                        .setMessage("This app needs these permissions..")
                        .setCancelable(true)
                        .setPositiveButton("Ok") { dialogInterface, i -> requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA), PERMISSION_CODE) }
                        .show()
            } else {

                // I show this request again and again
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA), PERMISSION_CODE)
            }
        }
    }

    //*****************************************
    // Checking the permission request result
    //*****************************************
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Checking our PERMISSION_CODE
        when (requestCode) {
            PERMISSION_CODE -> if (grantResults.size > 0 && grantResults[0] == permissionGranted) {
                // Permission granted
                tvPermissionText.setText("Permissions Granted. Thank you!!")
            } else {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                        !shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    // Permission is not granted (Permanently)
                    AlertDialog.Builder(this)
                            .setMessage("You have denied permanently these permissions, please go to setting to enable these permissions.")
                            .setCancelable(true)
                            .setPositiveButton("Go to Settings") { dialogInterface, i -> goToApplicationSettings() }
                            .setNegativeButton("Cancel", null)
                            .show()
                }
            }
        }

    }

    //******************************************************
    // if user denied permanently the permissions,
    //  he should go to setting to granted the permissions
    //******************************************************
    private fun goToApplicationSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }
}
