package com.puntogris.areyouarobot.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.puntogris.areyouarobot.R
import com.puntogris.areyouarobot.databinding.ActivityMainBinding
import com.puntogris.areyouarobot.ui.base.BaseActivity
import com.puntogris.areyouarobot.utils.getNavController
import com.puntogris.areyouarobot.utils.gone
import com.puntogris.areyouarobot.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun initializeViews() {
        setupNavigation()

        main_toolbar.apply {
            setSupportActionBar(this)
            setupWithNavController(navController, appBarConfiguration)
        }
    }

    private fun setupNavigation(){
        navController = getNavController()
        appBarConfiguration = getAppBarConfiguration()
        navController.addOnDestinationChangedListener(this@MainActivity)
        bottom_navigation.setupWithNavController(navController)
    }

    private fun getAppBarConfiguration(): AppBarConfiguration{
        return AppBarConfiguration(
            setOf(
                R.id.welcomeFragment,
                R.id.rankingsFragment,
                R.id.singlePlayerGameFragment,
                R.id.findMatchFragment,
                R.id.matchFragment,
                R.id.postMultiplayerMatchFragment
            )
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when(destination.id){
            R.id.singlePlayerGameFragment,
            R.id.playerSettingsFragment,
            R.id.matchFragment -> {
                main_toolbar.visible()
                bottom_navigation.gone()
            }
            R.id.rankingsFragment, R.id.postGameFragment -> {
                main_toolbar.gone()
                bottom_navigation.visible()
            }
            else -> {
                main_toolbar.visible()
                bottom_navigation.visible()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_top_app_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

}

