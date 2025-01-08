package com.example.mathhelper

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mathhelper.navigation.NavigationItem
import com.example.mathhelper.ui.screens.CameraScreen
import com.example.mathhelper.ui.screens.ImagePreviewScreen
import com.example.mathhelper.ui.screens.QueryScreen
import com.example.mathhelper.ui.theme.MathHelperTheme
import com.example.mathhelper.viewmodel.QueryViewModel

const val KEY_EVENT_ACTION = "key_event_action"
const val KEY_EVENT_EXTRA = "key_event_extra"
const val TAG = "MainActivity"

//CHEETAH
class MainActivity : ComponentActivity() {

    private val queryViewModel by viewModels<QueryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            // Key Point: Collecting Camera Permission State
//            val permissionGranted = isCameraPermissionGranted.collectAsState().value

            MathHelperTheme {
                MyNavigation(queryViewModel)
            }
        }
    }

    /** When key down event is triggered, relay it via local broadcast so fragments can handle it */
    //todo, make volume down key lead to photo click
//    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
//        return when (keyCode) {
//            KeyEvent.KEYCODE_VOLUME_DOWN -> {
//                val intent = Intent(KEY_EVENT_ACTION).apply { putExtra(KEY_EVENT_EXTRA, keyCode) }
//                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
//                true
//            }
//            else -> super.onKeyDown(keyCode, event)
//        }
//    }

}

@Composable
private fun MyNavigation(queryViewModel: QueryViewModel) {
    val navController = rememberNavController()
    NavHost(
        navController = navController, startDestination = NavigationItem.Query.route
    ) {
        composable(
            route = NavigationItem.Query.route
        ) {
            QueryScreen(navController, queryViewModel)
        }
        composable(route = NavigationItem.Camera.route) {
            CameraScreen(navController)
        }
        composable(
            route = NavigationItem.PreviewImage.route + "/{uri}",
            arguments = listOf(navArgument("uri") { type = NavType.StringType })
        ) {
            val encodedUri = it.arguments?.getString("uri") ?: ""
            val uri = Uri.decode(encodedUri)
            ImagePreviewScreen(navController, uri, queryViewModel)
        }
    }
}