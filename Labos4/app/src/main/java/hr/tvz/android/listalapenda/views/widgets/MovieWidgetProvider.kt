package hr.tvz.android.listalapenda.views.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.RemoteViews
import hr.tvz.android.listalapenda.R
import hr.tvz.android.listalapenda.app.App
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

class MovieWidgetProvider : AppWidgetProvider() {
    private val TAG = "MovieWidgetProvider"

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.d(TAG, "Updating widget")
        val app = context.applicationContext as? App
        if (app == null) {
            Log.e(TAG, "App context not available")
            showError(context, appWidgetManager, appWidgetIds)
            return
        }

        CoroutineScope(Dispatchers.Main).launch {
            try {
                Log.d(TAG, "Attempting to fetch movies from API")
                val movies = app.movieApi.getAllMovies()
                Log.d(TAG, "Fetched ${movies.size} movies")
                val lastMovie = movies.lastOrNull()
                if (lastMovie != null) {
                    val bitmap = withContext(Dispatchers.IO) {
                        downloadImage(lastMovie.imageUrl)
                    }
                    appWidgetIds.forEach { appWidgetId ->
                        Log.d(TAG, "Updating widget ID $appWidgetId with movie: ${lastMovie.title}")
                        val views = RemoteViews(context.packageName, R.layout.movie_widget)
                        views.setTextViewText(R.id.widget_title, lastMovie.title)
                        if (bitmap != null) {
                            Log.d(TAG, "Setting widget image bitmap for ${lastMovie.imageUrl}")
                            views.setImageViewBitmap(R.id.widget_image, bitmap)
                        } else {
                            Log.w(TAG, "Failed to load image, using placeholder")
                            views.setImageViewResource(R.id.widget_image, android.R.drawable.ic_menu_gallery)
                        }
                        appWidgetManager.updateAppWidget(appWidgetId, views)
                    }
                } else {
                    Log.w(TAG, "No movies available")
                    showError(context, appWidgetManager, appWidgetIds)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error updating widget: ${e.message}, cause: ${e.cause}", e)
                showError(context, appWidgetManager, appWidgetIds)
            }
        }
    }

    private fun showError(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        Log.d(TAG, "Showing error for widget IDs: ${appWidgetIds.joinToString()}")
        appWidgetIds.forEach { appWidgetId ->
            val views = RemoteViews(context.packageName, R.layout.movie_widget)
            views.setTextViewText(R.id.widget_title, "Can't load widget")
            views.setImageViewResource(R.id.widget_image, android.R.drawable.ic_menu_gallery)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    private suspend fun downloadImage(url: String): Bitmap? {
        return try {
            val client = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                response.body?.byteStream()?.use { inputStream ->
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    if (bitmap == null) {
                        Log.e(TAG, "Failed to decode bitmap from $url")
                    }
                    bitmap
                }
            } else {
                Log.e(TAG, "Failed to download image: ${response.code} ${response.message}")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error downloading image $url: ${e.message}", e)
            null
        }
    }
}