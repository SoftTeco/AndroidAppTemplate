package com.softteco.template

import android.app.admin.DevicePolicyManager
import android.app.admin.SystemUpdatePolicy
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.os.UserManager
import android.provider.Settings
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.softteco.template.navigation.Graph
import com.softteco.template.ui.AppContent
import com.softteco.template.ui.components.dialog.DialogController
import com.softteco.template.ui.components.snackbar.SnackbarController
import com.softteco.template.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var snackbarController: SnackbarController

    @Inject
    lateinit var dialogController: DialogController

    private lateinit var adminComponentName: ComponentName
    private lateinit var dpm: DevicePolicyManager

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        adminComponentName = AppDeviceAdminReceiver.getComponentName(this)
        dpm = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager

        val isDeviceAdmin = dpm.isDeviceOwnerApp(packageName)
        if (isDeviceAdmin) {
            setKioskPolicies(true, true)
        }

        setContent {
            val isUserLoggedIn by viewModel.isUserLoggedIn.collectAsState()
            val theme by viewModel.theme.collectAsState()

            // Keep the splash screen on-screen while we check if the user is logged in
            val content: View = findViewById(android.R.id.content)
            content.viewTreeObserver.addOnPreDrawListener(
                object : ViewTreeObserver.OnPreDrawListener {
                    override fun onPreDraw(): Boolean {
                        return if (isUserLoggedIn != null) {
                            content.viewTreeObserver.removeOnPreDrawListener(this)
                            true
                        } else {
                            false
                        }
                    }
                }
            )

            key(isUserLoggedIn) {
                AppTheme(theme) {
                    isUserLoggedIn?.let { isLoggedIn ->
                        val graph = if (isLoggedIn) Graph.Main else Graph.Login
                        AppContent(
                            startDestination = graph.route,
                            snackbarController = snackbarController,
                            dialogController = dialogController,
                            isDeviceAdmin = isDeviceAdmin
                        )
                    }
                }
            }
        }
    }

    private fun setKioskPolicies(enable: Boolean, isAdmin: Boolean) {
        if (isAdmin) {
            setRestrictions(enable)
            enableStayOnWhilePluggedIn(enable)
            setUpdatePolicy(enable)
            setAsHomeApp(enable)
            setKeyGuardEnabled(enable)
        }
        setLockTask(enable, isAdmin)
        setImmersiveMode(enable)
    }

    private fun setRestrictions(disallow: Boolean) {
        setUserRestriction(UserManager.DISALLOW_SAFE_BOOT, disallow)
        setUserRestriction(UserManager.DISALLOW_FACTORY_RESET, disallow)
        setUserRestriction(UserManager.DISALLOW_ADD_USER, disallow)
        setUserRestriction(UserManager.DISALLOW_MOUNT_PHYSICAL_MEDIA, disallow)
        setUserRestriction(UserManager.DISALLOW_ADJUST_VOLUME, disallow)
        dpm.setStatusBarDisabled(adminComponentName, disallow)
    }

    private fun setUserRestriction(restriction: String, disallow: Boolean) = if (disallow) {
        dpm.addUserRestriction(adminComponentName, restriction)
    } else {
        dpm.clearUserRestriction(adminComponentName, restriction)
    }

    private fun enableStayOnWhilePluggedIn(active: Boolean) = if (active) {
        dpm.setGlobalSetting(
            adminComponentName,
            Settings.Global.STAY_ON_WHILE_PLUGGED_IN,
            (BatteryManager.BATTERY_PLUGGED_AC
                    or BatteryManager.BATTERY_PLUGGED_USB
                    or BatteryManager.BATTERY_PLUGGED_WIRELESS).toString()
        )
    } else {
        dpm.setGlobalSetting(
            adminComponentName,
            Settings.Global.STAY_ON_WHILE_PLUGGED_IN,
            "0"
        )
    }

    private fun setLockTask(start: Boolean, isAdmin: Boolean) {
        if (isAdmin) {
            dpm.setLockTaskPackages(
                adminComponentName,
                if (start) arrayOf(
                    packageName,
                    "com.android.chrome",
                    "com.android.settings"
                ) else arrayOf()
            )
        }

        if (start) {
            startLockTask()
        } else {
            stopLockTask()
        }
    }

    private fun setUpdatePolicy(enable: Boolean) {
        if (enable) {
            dpm.setSystemUpdatePolicy(
                adminComponentName,
                SystemUpdatePolicy.createWindowedInstallPolicy(60, 120)
            )
        } else {
            dpm.setSystemUpdatePolicy(adminComponentName, null)
        }
    }

    private fun setAsHomeApp(enable: Boolean) {
        if (enable) {
            val intentFilter = IntentFilter(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_HOME)
                addCategory(Intent.CATEGORY_DEFAULT)
            }
            dpm.addPersistentPreferredActivity(
                adminComponentName,
                intentFilter,
                componentName
            )
        } else {
            dpm.clearPackagePersistentPreferredActivities(
                adminComponentName, packageName
            )
        }
    }

    private fun setKeyGuardEnabled(enable: Boolean) {
        dpm.setKeyguardDisabled(adminComponentName, !enable)
    }

    private fun setImmersiveMode(enable: Boolean) {
        val window = window
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

        // Hide the system bars
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        if (enable) {
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        } else {
            windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
        }

        // Make content appear under the system bars
        WindowCompat.setDecorFitsSystemWindows(window, !enable)
    }
}
