package com.puntogris.areyouarobot.ui

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
import com.puntogris.areyouarobot.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = findNavController(R.id.nav_host_fragment).apply {
            bottom_navigation.setupWithNavController(this)

            addOnDestinationChangedListener { _, destination, _ ->
                bottomNavSelector(destination)
            }
        }

        main_toolbar.apply {
            setupWithNavController(navController, AppBarConfiguration(topLevelFragments()))
            setSupportActionBar(this)
        }
    }

    private fun topLevelFragments(): Set<Int>{
        return setOf(
            R.id.welcomeFragment,
            R.id.rankingsFragment,
            R.id.singlePlayerGameFragment,
            R.id.findMatchFragment,
            R.id.matchFragment,
            R.id.postMultiplayerMatchFragment
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_top_app_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    private fun bottomNavSelector(destination: NavDestination) {
        when(destination.id){
            R.id.singlePlayerGameFragment, R.id.playerSettingsFragment, R.id.matchFragment -> {
                main_toolbar.visibility = View.VISIBLE
                bottom_navigation.visibility = View.GONE
            }
            R.id.rankingsFragment, R.id.postGameFragment -> {
                main_toolbar.visibility = View.GONE
                bottom_navigation.visibility = View.VISIBLE
            }
            else -> {
                main_toolbar.visibility = View.VISIBLE
                bottom_navigation.visibility = View.VISIBLE
            }
        }
    }

}

