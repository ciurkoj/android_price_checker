package com.example.pricechecker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.util.SparseArray
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
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
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.request.RequestOptions
import com.example.pricechecker.barcode_reader.BarcodeReaderFragment
import com.example.pricechecker.fragments.*
import com.example.pricechecker.fragments.adapters.ViewPagerAdapter
import com.example.pricechecker.ui.login.LoginActivity
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity(), BarcodeReaderFragment.BarcodeReaderListener  {



    val adapter = ViewPagerAdapter(supportFragmentManager)

    //    private var btn_fragment: Button = findViewById<Button>(R.id.btn_fragment)
    //    private val currentUser: Any
    private lateinit var appBarConfiguration: AppBarConfiguration
    val db = Firebase.firestore
    val TAG = "<============>"
    private lateinit var currentUser : FirebaseUser

    private lateinit var fragmentA: ScanFragment
    private lateinit var fragmentB: ManualFragment
    lateinit var navView: NavigationView
    private lateinit var scanFragment: ScanFragment
    private lateinit var fTranslation: FragmentTransaction
    private lateinit var fManager: FragmentManager
    lateinit var viewPager: ViewPager
    private var storageReference = FirebaseStorage.getInstance().reference.child("images/")// = FirebaseStorage.getInstance().getReference("/images/F75eae455-7690-4f0a-93d4-84e7e60b3470?alt=media&token=d62907a0-421e-489c-9ab9-ddf213ea3534")
    private lateinit var mainActivityViewModel: MainActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
//        setContentView(R.layout.activity_splash)
        setContentView(R.layout.activity_main)
        viewPager = findViewById(R.id.viewPager)
        currentUser =Firebase.auth.currentUser

//        currentUser = Firebase.auth.currentUser
//        val currentUser = Firebase.auth.currentUser
        findViewById<TextView>(R.id.tv_result)
        findViewById<TextView>(R.id.tv_result_head)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.settings_activity
            ), drawerLayout
        )

        val headerView = navView.getHeaderView(0)
        val profile_pic: ImageView = headerView.findViewById(R.id.profile_pic)

        if(intent.getStringExtra("previousActivity").equals("LoginActivity")){
            Log.e("taggg","duuupa")
            startActivity(Intent(this@MainActivity, MainActivity::class.java))
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        setUpTabs()

        fManager = supportFragmentManager
        fTranslation = fManager.beginTransaction()
        scanFragment = ScanFragment()
        fTranslation.replace(R.id.viewPager, scanFragment)
        fTranslation.commit()


        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val logout_btn: FloatingActionButton = headerView.findViewById(R.id.logout)
        logout_btn.setOnClickListener {
            signOut()
        }
        updateNavHeader()


    }

    private fun setUpTabs() {
        adapter.addFragment(RecentFragment(), "Recent")
        adapter.addFragment(ScanFragment(), "Barcode Scan")
        adapter.addFragment(ManualFragment(), "Manual Search")
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)

        fragmentA = ScanFragment()
        fragmentB = ManualFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.viewPager, fragmentA)
            .replace(R.id.viewPager, fragmentB!!)
            .commit()

    }

    fun refreshManual(){
        fragmentA = ScanFragment()
        fragmentB = ManualFragment()
        supportFragmentManager.beginTransaction()
            .detach(fragmentB)
            .attach(fragmentB)
            .commit()
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

    fun swipeRight(x: Int) {
        if (x < 4) {
            viewPager.adapter = adapter
            println(viewPager.currentItem)
            viewPager.currentItem = 2
        }
    }

    fun swipeLeft(x: Int) {
        if (x > 0) {
            viewPager.currentItem = x - 1
        }
    }

    private fun signOut() {
        // [START auth_sign_out]
        Firebase.auth.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
        // [END auth_sign_out]
    }

    private fun updateNavHeader() {
//        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val headerView = navView.getHeaderView(0)
        val navUsername: TextView = headerView.findViewById(R.id.show_username)
        val navUserMail: TextView = headerView.findViewById(R.id.email)
        val profile_pic: ImageView = headerView.findViewById(R.id.profile_pic)
        navUserMail.text = currentUser.email
        navUsername.text = currentUser.displayName

            storageReference.child(currentUser.uid).downloadUrl.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    GlideApp.with(this@MainActivity)
                        .load(task.result)
                        .apply(RequestOptions.circleCropTransform())
                        .into(profile_pic)
                } else {
                    Toast.makeText(this@MainActivity, task.exception?.message, Toast.LENGTH_SHORT)
                        .show()
                    Log.e("Firebase id", currentUser.uid)
                }
            }

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