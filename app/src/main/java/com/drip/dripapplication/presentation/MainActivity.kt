package com.drip.dripapplication.presentation

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.drip.dripapplication.App
import com.drip.dripapplication.NetworkStateUtils
import com.drip.dripapplication.R
import com.drip.dripapplication.TabsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber

private val Context.preferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = "DATA_STORE_NAME")

class MainActivity : AppCompatActivity() {

    //Network Utils
    private var networkStateUtils: NetworkStateUtils? = null

    // Должны хранить ссылку на текущий navController, так как есть корневой контроллер (для main_graph)
    // и контроллер вкладок (для tabs_graph)
    private var navController: NavController? = null

    private val topLevelDestinations = setOf(getTabsDestination(), getLoginDestination())

    //Snackbar
    private var snackbar: Snackbar? = null

    // fragment listener is sued for tracking current nav controller
    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            if (f is TabsFragment || f is NavHostFragment){
                snackbar?.setAnchorView(R.id.bottom_nav)
            }
            onNavControllerActivated(f.findNavController())
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // preparing root nav controller
        val navController = getRootNavController()
        prepareRootNavController(true, navController)
        onNavControllerActivated(navController)



        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, true)

        snackbar = Snackbar.make(findViewById(R.id.coordinatorLayout), R.string.error_from_repository, Snackbar.LENGTH_LONG)

        networkStateUtils = NetworkStateUtils(this.applicationContext, snackbar)






    }

    override fun onDestroy() {
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
        navController = null
        super.onDestroy()
        Timber.d("isFinishing = $isFinishing")
    }

    override fun onBackPressed() {
        if (isStartDestination(navController?.currentDestination)) {
            super.onBackPressed()
        } else {
            navController?.popBackStack()
        }
    }


    override fun onStart() {
        super.onStart()
        networkStateUtils?.registerNetworkCallback()
    }

    override fun onStop() {
        super.onStop()
        networkStateUtils?.unRegisterNetworkCallback()
    }


    override fun onSupportNavigateUp(): Boolean = (navController?.navigateUp() ?: false) || super.onSupportNavigateUp()

    private fun prepareRootNavController(isSignedIn: Boolean, navController: NavController) {
        val graph = navController.navInflater.inflate(getMainGraph())
        graph.setStartDestination(
            if (isSignedIn) {
                getTabsDestination()
            } else {
                getLoginDestination()
            }
        )
        navController.graph = graph
    }

    private fun onNavControllerActivated(navController: NavController) {
        if (this.navController == navController) return
        this.navController = navController
    }

    private fun getRootNavController(): NavController {
        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        return navHost.navController
    }

    private fun isStartDestination(destination: NavDestination?): Boolean {
        if (destination == null) return false
        val graph = destination.parent ?: return false
        val startDestinations = topLevelDestinations + graph.startDestinationId
        return startDestinations.contains(destination.id)
    }

    private fun getMainGraph(): Int = R.navigation.main_graph

    private fun getTabsDestination(): Int = R.id.tabsFragment

    private fun getLoginDestination(): Int = R.id.loginFragment

}

