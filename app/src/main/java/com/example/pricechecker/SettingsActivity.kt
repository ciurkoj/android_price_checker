package com.example.pricechecker

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceFragmentCompat
import com.example.pricechecker.repository.LocationRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import kotlinx.android.synthetic.main.settings_activity.*
import java.util.*
import com.example.pricechecker.model.Location as MyLocation


class SettingsActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    /**
     * Provides the entry point to the Fused Location Provider API.
     */
    private var mFusedLocationClient: FusedLocationProviderClient? = null

    /**
     * Represents a geographical location.
     */
    protected var mLastLocation: Location? = null

    private var mLatitudeLabel: String? = null
    private var mLongitudeLabel: String? = null
    private var mLatitudeText: TextView? = null
    private var mLongitudeText: TextView? = null
    val repository = LocationRepository()
    val viewModelFactory = SettingsViewModelFactory(repository)
    private var currentLocationText: TextView? = null
    private val AUTOCOMPLETE_REQUEST_CODE = 1
    private lateinit var viewModel: SettingsViewModel
    private var full_list_auto_complete: MutableList<MyLocation> = ArrayList<MyLocation>()

    lateinit var aAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings_activity, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mLatitudeLabel = resources.getString(R.string.latitude_label)
        mLongitudeLabel = resources.getString(R.string.longitude_label)
        mLatitudeText = findViewById<View>(R.id.latitude_text) as TextView
        mLongitudeText = findViewById<View>(R.id.longitude_text) as TextView
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        Log.e("=========================>", "dddddddddddddddd")
        currentLocationText = findViewById(R.id.current_location_text)
        val button = findViewById<ImageButton>(R.id.imageButton)
        button.setOnClickListener{
            getLastLocation()
        }
        viewModel = ViewModelProvider(this, viewModelFactory).get(SettingsViewModel::class.java)
        val searchViewSettings = findViewById<SearchView>(R.id.searchViewSettings)
        val listView = findViewById<ListView>(R.id.listViewSettings)
        var list_auto_complete: MutableList<String> = ArrayList()

        aAdapter = ArrayAdapter<String>(this@SettingsActivity,
            R.layout.custom_list_item, R.id.text_view_list_item, list_auto_complete
        )
        searchViewSettings.queryHint = "Set a location..."
        searchViewSettings.requestFocusFromTouch();
        searchViewSettings.setOnClickListener  {
            searchViewSettings.requestFocus();
            searchViewSettings.setIconified(false);
            Log.e("dccsadafdsaf", "clicked")
        }
        settings_container.setOnClickListener {
            // hide soft keyboard on rot layout click
            // it hide soft keyboard on edit text outside root layout click
            hideSoftKeyboard()

            // remove focus from edit text
            searchViewSettings.clearFocus()
        }
        listView.bringToFront()
        searchViewSettings.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchViewSettings.clearFocus()

                viewModel.getCustomLocation(query.toString(), 5)
                viewModel.myCustomLocation.observe(this@SettingsActivity, Observer { response ->
                    Log.e("=======>", response.toString())
                    if (response.isSuccessful) {
                        response.body()?.let {
//                                myAdapter.setData(it)
                            list_auto_complete.clear()
                            aAdapter = ArrayAdapter<String>(this@SettingsActivity,
                                R.layout.custom_list_item, R.id.text_view_list_item, list_auto_complete
                            )
                            aAdapter.notifyDataSetChanged()
                            listView.adapter = aAdapter
                            aAdapter.clear()
                            for (item in it) {
                                full_list_auto_complete.add(MyLocation( item.name, item.canonical_name, item.gps))
                                aAdapter.add(item.canonical_name)
                            }
                            aAdapter.notifyDataSetChanged()
                        }

                    } else {
                        Toast.makeText(applicationContext, response.code(), Toast.LENGTH_SHORT)
                            .show()
                    }
                })

                aAdapter.clear()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
        listView.onItemClickListener =
            OnItemClickListener { adapter, _, position, _ ->
                val value = adapter.getItemAtPosition(position) as String
                searchViewSettings.setQuery(value, false)

                val found =full_list_auto_complete.firstOrNull{it.canonical_name == value}
                if (found != null) {
                    currentLocationText?.text = "Current location: ${found.canonical_name}"
                    mLongitudeText?.text = "$mLongitudeLabel: ${found.gps[0]}"
                    mLatitudeText?.text = "$mLatitudeLabel: ${found.gps[1]}"

                }
                full_list_auto_complete.clear()

                listView.adapter = null
            }

    }

    fun Activity.hideSoftKeyboard(){
        (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).apply {
            hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        Log.i(TAG, "Place: ${place.name}, ${place.id}")
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    // TODO: Handle the error.
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        Log.i(TAG, status.statusMessage)
                    }
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }




    public override fun onStart() {
        super.onStart()
        searchViewSettings.isIconifiedByDefault = false

//        searchViewSettings.clearFocus()
        hideSoftKeyboard()
        if (!checkPermissions()) {
            requestPermissions()
        } else {
            getLastLocation()
        }
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)


        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {

        mFusedLocationClient!!.lastLocation
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful && task.result != null) {
                    mLastLocation = task.result

                    mLatitudeText!!.text = mLatitudeLabel + ":   " +
                            (mLastLocation)!!.latitude
                    mLongitudeText!!.text = mLongitudeLabel + ":   " +
                            (mLastLocation)!!.longitude
                } else {
                    Log.w(TAG, "getLastLocation:exception", task.exception)
                    showMessage(getString(R.string.no_location_detected))
                }
            }
    }

    private fun showMessage(text: String) {
        val container = findViewById<View>(R.id.settings_container)
        if (container != null) {
            Toast.makeText(this@SettingsActivity, text, Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Shows a [].
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * *
     * @param actionStringId   The text of the action item.
     * *
     * @param listener         The listener associated with the Snackbar action.
     */
    private fun showSnackbar(
        mainTextStringId: Int, actionStringId: Int,
        listener: View.OnClickListener
    ) {

        Toast.makeText(this@SettingsActivity, getString(mainTextStringId), Toast.LENGTH_LONG).show()
    }

    /**
     * Return the current state of the permissions needed.
     */
    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(
            this@SettingsActivity,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            REQUEST_PERMISSIONS_REQUEST_CODE
        )
    }

    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")

            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                View.OnClickListener {
                    // Request permission
                    startLocationPermissionRequest()
                })

        } else {
            Log.i(TAG, "Requesting permission")
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startLocationPermissionRequest()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.size <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.")
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getLastLocation()
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                    View.OnClickListener {
                        // Build intent that displays the App settings screen.
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri = Uri.fromParts(
                            "package",
                            BuildConfig.APPLICATION_ID, null
                        )
                        intent.data = uri
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    })
            }
        }
    }

    companion object {

        private const val TAG = "LocationProvider"

        private const val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }



}


