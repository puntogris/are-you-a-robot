package com.puntogris.areyouarobot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Bottom Navigation
        navController = findNavController(
            R.id.nav_host_fragment
        )
        bottom_navigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            bottomNavSelector(destination)
        }

        setSupportActionBar(findViewById(R.id.main_toolbar))

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.welcomeFragment,
                R.id.rankingsFragment,
                R.id.singlePlayerGameFragment,
                R.id.findMatchFragment,
                R.id.matchFragment,
                R.id.postMultiplayerMatchFragment
            )
        )

        main_toolbar.setupWithNavController(navController, appBarConfiguration)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_top_app_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    private fun bottomNavSelector(destination: NavDestination) {
        if (destination.id == R.id.singlePlayerGameFragment ||
            destination.id == R.id.playerSettingsFragment ||
            destination.id ==R.id.matchFragment
        ) {
            main_toolbar.visibility = View.VISIBLE
            bottom_navigation.visibility = View.GONE
        } else if (destination.id == R.id.rankingsFragment ||
            destination.id == R.id.postGameFragment
        ) {
            main_toolbar.visibility = View.GONE
            bottom_navigation.visibility = View.VISIBLE
        } else {
            main_toolbar.visibility = View.VISIBLE
            bottom_navigation.visibility = View.VISIBLE
        }

    }

}

