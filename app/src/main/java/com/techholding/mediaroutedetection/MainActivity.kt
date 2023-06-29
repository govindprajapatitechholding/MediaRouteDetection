package com.techholding.mediaroutedetection

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.mediarouter.media.MediaControlIntent
import androidx.mediarouter.media.MediaRouteSelector
import androidx.mediarouter.media.MediaRouter

class MainActivity : AppCompatActivity() {

    private var mMediaRouter: MediaRouter? = null

    private val mMediaRouterCallback: MediaRouter.Callback = object : MediaRouter.Callback() {
        // BEGIN_INCLUDE(SimpleCallback)
        /**
         * A new route has been selected as active. Disable the current
         * route and enable the new one.
         */
        override fun onRouteSelected(router: MediaRouter, route: MediaRouter.RouteInfo) {
            updatePresentation()
        }

        /**
         * The route has been unselected.
         */
        override fun onRouteUnselected(router: MediaRouter, info: MediaRouter.RouteInfo) {
            updatePresentation()
        }

        /**
         * The route's presentation display has changed. This callback
         * is called when the presentation has been activated, removed
         * or its properties have changed.
         */
        override fun onRoutePresentationDisplayChanged(
            router: MediaRouter,
            route: MediaRouter.RouteInfo
        ) {
            updatePresentation()
        } // END_INCLUDE(SimpleCallback)
    }


    /**
     * Updates the displayed presentation to enable a secondary screen if it has
     * been selected in the [android.media.MediaRouter] for the
     * [android.media.MediaRouter.ROUTE_TYPE_LIVE_VIDEO] type. If no screen has been
     * selected by the [android.media.MediaRouter], the current screen is disabled.
     * Otherwise a new [SamplePresentation] is initialized and shown on
     * the secondary screen.
     */
    private fun updatePresentation() {

        // BEGIN_INCLUDE(updatePresentationInit)
        // Get the selected route for live video
        val selectedRoute: MediaRouter.RouteInfo? = mMediaRouter?.selectedRoute
        Log.e("===============", "${selectedRoute?.name}")
        // END_INCLUDE(updatePresentationNew)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get the media router service.
        mMediaRouter = MediaRouter.getInstance(this)

    }

    override fun onResume() {
        super.onResume()
        // BEGIN_INCLUDE(addCallback)
        // Register a callback for all events related to live video devices
        mMediaRouter!!.addCallback(
            MediaRouteSelector.Builder()
                .addControlCategory(MediaControlIntent.CATEGORY_LIVE_VIDEO)
                .build(),
            mMediaRouterCallback
        )
        // END_INCLUDE(addCallback)

        // Update the displays based on the currently active routes
        updatePresentation()
    }

    override fun onPause() {
        super.onPause()

        // BEGIN_INCLUDE(onPause)
        // Stop listening for changes to media routes.
        mMediaRouter!!.removeCallback(mMediaRouterCallback)
        // END_INCLUDE(onPause)
    }
}