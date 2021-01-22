package br.gov.am.detran.appvistoria.presentation

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import br.gov.am.detran.appvistoria.R
import br.gov.am.detran.appvistoria.databinding.ActivityMainBinding
import br.gov.am.detran.appvistoria.databinding.NavHeaderMainBinding
import br.gov.am.detran.appvistoria.presentation.ui.auth.LoginActivity
import br.gov.am.detran.appvistoria.session.UserPreferences
import com.google.android.material.navigation.NavigationView

//import kotlinx.android.synthetic.main.activity_main.*
//import kotlinx.android.synthetic.main.nav_header_main.view.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        UserPreferences.init(this)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val hView = binding.navView.getHeaderView(0)
        val nav = NavHeaderMainBinding.bind(hView)
        nav.tvName.text = UserPreferences.username
        nav.tvEmail.text = UserPreferences.email

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home, R.id.nav_info, R.id.nav_settings), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setNavigationItemSelectedListener { dest ->
            when (dest.itemId) {
                R.id.nav_logout -> logout()
                else -> {
                    NavigationUI.onNavDestinationSelected(dest, navController)
                    drawerLayout.closeDrawers()
                }
            }

            true
        }
//        navView.menu.findItem(R.id.nav_logout).setOnMenuItemClickListener { menuItem: MenuItem? ->
//            UserPreferences.clear(this)
//            val i = Intent(applicationContext, LoginActivity::class.java)
//
//            // Closing all the Activities
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//
//            // Staring Login Activity
//            startActivity(i)
//            finish()
//            true
//        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    private fun logout() {
        UserPreferences.clear(this)
        val i = Intent(applicationContext, LoginActivity::class.java)

        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        // Staring Login Activity
        startActivity(i)
        finish()
    }


}
