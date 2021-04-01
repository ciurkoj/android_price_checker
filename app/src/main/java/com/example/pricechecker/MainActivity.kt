package com.example.pricechecker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.pricechecker.barcode_reader.BarcodeReaderActivity
import com.example.pricechecker.barcode_reader.BarcodeReaderFragment
import com.example.pricechecker.fragments.BarcodeFragment
import com.example.pricechecker.fragments.ManualFragment
import com.example.pricechecker.fragments.RecentFragment
import com.example.pricechecker.fragments.ScanFragment
import com.example.pricechecker.fragments.adapters.ViewPagerAdapter
import com.example.pricechecker.ui.login.LoginActivity
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.app_bar_main.*


class MainActivity : AppCompatActivity(), BarcodeReaderFragment.BarcodeReaderListener {

    private val BARCODE_READER_ACTIVITY_REQUEST = 1208
    private val mTvResult: TextView? = null
    private val mTvResultHeader: TextView? = null
//    private var btn_fragment: Button = findViewById<Button>(R.id.btn_fragment)

    //    private val currentUser: Any
    private lateinit var appBarConfiguration: AppBarConfiguration
    val db = Firebase.firestore
    val TAG = "<============>"
    val currentUser = Firebase.auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_splash)
        setContentView(R.layout.activity_main)

//        val currentUser = Firebase.auth.currentUser
        findViewById<TextView>(R.id.tv_result)
        findViewById<TextView>(R.id.tv_result_head)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
//                R.id.nav_gallery,
//                R.id.nav_slideshow,
                R.id.settings_activity
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        setUpTabs()

//        val signup = findViewById<Button>(R.id.button1)
//
//        signup.setOnClickListener {
//            startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
//
//        }

        updateNavHeader()
        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val headerView = navigationView.getHeaderView(0)
        val logout_btn: FloatingActionButton = headerView.findViewById(R.id.logout)
        logout_btn.setOnClickListener {
            signOut()
        }

    }

    private fun setUpTabs() {
        val adapter = ViewPagerAdapter(supportFragmentManager)

        adapter.addFragment(RecentFragment(), "Recent")
        adapter.addFragment(ScanFragment(), "Scan")
        adapter.addFragment(ManualFragment(), "Manual")
        adapter.addFragment(BarcodeFragment(), "Barcode")

        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            // User chose the "Settings" item, show the app settings UI...
            Toast.makeText(
                applicationContext,
                "welcome displayName",
                Toast.LENGTH_LONG
            ).show()
            startActivity(Intent(this, SettingsActivity::class.java))
            true
        }


        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun signOut() {
        // [START auth_sign_out]
        Firebase.auth.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
        // [END auth_sign_out]
    }

    private fun updateNavHeader() {
        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val headerView = navigationView.getHeaderView(0)
        val navUsername: TextView = headerView.findViewById(R.id.show_username)
        val navUserMail: TextView = headerView.findViewById(R.id.email)
//        val navUserPhot: ImageView = headerView.findViewById(R.id.nav_user_photo)

        navUserMail.setText(currentUser.getEmail())
        navUsername.setText(currentUser.getDisplayName())
        Log.i(TAG, "(((((((((===> ${currentUser.displayName}")
        // now we will use Glide to load user image
        // first we need to import the library


        // now we will use Glide to load user image
        // first we need to import the library
//        Glide.with(this).load(currentUser.getPhotoUrl()).into(navUserPhot)

    }

    override fun onScanned(barcode: Barcode?) {
        TODO("Not yet implemented")
    }

    override fun onScannedMultiple(barcodes: MutableList<Barcode>?) {
        TODO("Not yet implemented")
    }

    override fun onBitmapScanned(sparseArray: SparseArray<Barcode>?) {
        TODO("Not yet implemented")
    }

    override fun onScanError(errorMessage: String?) {
        TODO("Not yet implemented")
    }

    override fun onCameraPermissionDenied() {
        TODO("Not yet implemented")
    }


}