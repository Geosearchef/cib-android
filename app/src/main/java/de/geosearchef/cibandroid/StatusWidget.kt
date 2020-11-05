package de.geosearchef.cibandroid

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.os.Handler
import android.widget.RemoteViews
import java.time.Duration

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in [StatusWidgetConfigureActivity]
 */
class StatusWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            StatusWidgetConfigureActivity.deleteCityPref(context, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {}
    override fun onDisabled(context: Context) {}

    companion object {
        const val minUpdateInterval = 9 * 60 * 1000
        var lastUpdate = 0L

        fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val city: String = StatusWidgetConfigureActivity.getConfiguredCity(context, appWidgetId)

            val views = RemoteViews(context.packageName, R.layout.status_widget)

            Thread {
                if(System.currentTimeMillis() - lastUpdate < minUpdateInterval) {
                    println("aborting update")
                }

                lastUpdate = System.currentTimeMillis()

                println("Loading city info")
                val info = MainActivity.getCityInfo(city)
                println(info)
                Handler(context.mainLooper).post {
                    views.setTextViewText(R.id.cityTextField, info.toInfoString())

                    // Instruct the widget manager to update the widget
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
            }.start()
        }
    }
}