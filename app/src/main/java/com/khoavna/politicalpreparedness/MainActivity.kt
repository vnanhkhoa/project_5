package com.khoavna.politicalpreparedness

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.khoavna.politicalpreparedness.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var navController: NavController
    private val appBarConfig = AppBarConfiguration(
        setOf(
            R.id.launchFragment,
            R.id.electionsFragment,
            R.id.voterInfoFragment,
            R.id.representativeFragment
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        navController = supportFragmentManager.findFragmentById(R.id.nav_host_fragment).let {
            (it as NavHostFragment).findNavController()
        }

        navController.addOnDestinationChangedListener { _, des, _ ->
            binding.toolbar.isActivated = des.id == R.id.launchFragment
        }

        setupActionBarWithNavController(navController, appBarConfig)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}
