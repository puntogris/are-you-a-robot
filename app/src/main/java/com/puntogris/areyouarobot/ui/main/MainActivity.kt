package com.puntogris.areyouarobot.ui.main

import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.puntogris.areyouarobot.R
import com.puntogris.areyouarobot.databinding.ActivityMainBinding
import com.puntogris.areyouarobot.utils.getNavController
import com.puntogris.areyouarobot.utils.gone
import com.puntogris.areyouarobot.utils.viewBinding
import com.puntogris.areyouarobot.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        applySystemBarInsets()
        setupNavigation()

        binding.mainToolbar.apply {
            setSupportActionBar(this)
            setupWithNavController(navController, appBarConfiguration)
        }
    }

    private fun applySystemBarInsets() {
        val root = binding.mainRoot
        val initialLeft = root.paddingLeft
        val initialTop = root.paddingTop
        val initialRight = root.paddingRight
        val initialBottom = root.paddingBottom

        ViewCompat.setOnApplyWindowInsetsListener(root) { view, windowInsets ->
            val systemBars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(
                left = initialLeft + systemBars.left,
                top = initialTop + systemBars.top,
                right = initialRight + systemBars.right,
                bottom = initialBottom + systemBars.bottom
            )
            windowInsets
        }
        ViewCompat.requestApplyInsets(root)
    }

    private fun setupNavigation() {
        navController = getNavController()
        appBarConfiguration = getAppBarConfiguration()
        navController.addOnDestinationChangedListener(this@MainActivity)
        binding.bottomNavigation.setupWithNavController(navController)
    }

    private fun getAppBarConfiguration(): AppBarConfiguration {
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
        when (destination.id) {
            R.id.singlePlayerGameFragment,
            R.id.playerSettingsFragment,
            R.id.matchFragment -> {
                binding.mainToolbar.visible()
                binding.bottomNavigation.gone()
            }

            R.id.rankingsFragment, R.id.postGameFragment -> {
                binding.mainToolbar.gone()
                binding.bottomNavigation.visible()
            }

            else -> {
                binding.mainToolbar.visible()
                binding.bottomNavigation.visible()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_top_app_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.welcomeFragment) {
            if (!navController.popBackStack(R.id.welcomeFragment, false)) {
                navController.navigate(R.id.welcomeFragment)
            }
            return true
        }
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }
}
