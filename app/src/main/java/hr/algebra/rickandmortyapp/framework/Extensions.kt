package hr.algebra.rickandmortyapp.framework

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Handler
import android.os.Looper
import android.view.View
import hr.algebra.rickandmortyapp.model.Character
import android.view.animation.AnimationUtils
import androidx.core.content.getSystemService
import androidx.preference.PreferenceManager
import hr.algebra.rickandmortyapp.api.RNM_PROVIDER_CONTENT_URI

fun View.applyAnimation(animationId: Int) =
    startAnimation(AnimationUtils.loadAnimation(context, animationId))

inline fun <reified T : Activity> Context.startActivity() =
    startActivity(Intent(this, T::class.java)
        .apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })

inline fun <reified T : Activity> Context.startActivity(key: String, value: Int) =
    startActivity(Intent(this, T::class.java)
        .apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra(key, value)
        })

inline fun <reified T : BroadcastReceiver> Context.sendBroadcast() =
    sendBroadcast(Intent(this, T::class.java))

fun Context.setBooleanPreference(key: String, value: Boolean = true)
    = PreferenceManager.getDefaultSharedPreferences(this)
    .edit()
    .putBoolean(key, value)
    .apply()

fun Context.getBooleanPreference(key: String)
    = PreferenceManager.getDefaultSharedPreferences(this)
    .getBoolean(key, false)

fun callDelayed(delay: Long, work: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed(
        work,
        delay
    )
}

fun Context.isOnline() : Boolean {
    val connectivityManager = getSystemService<ConnectivityManager>()
    connectivityManager?.activeNetwork?.let {network ->
        connectivityManager.getNetworkCapabilities(network)?.let {networkCapabilities ->
            return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        }
    }
    return false
}

@SuppressLint("Range")
fun Context.fetchItems(): MutableList<Character> {
    val items = mutableListOf<Character>()

    val cursor = contentResolver.query(RNM_PROVIDER_CONTENT_URI, null, null, null, null)
    cursor?.use {
        while (it.moveToNext()) {
            val id = it.getLong(it.getColumnIndexOrThrow(Character::id.name))
            val name = it.getString(it.getColumnIndexOrThrow(Character::name.name))
            val status = it.getString(it.getColumnIndexOrThrow(Character::status.name))
            val species = it.getString(it.getColumnIndexOrThrow(Character::species.name))
            val type = it.getString(it.getColumnIndexOrThrow(Character::type.name))
            val gender = it.getString(it.getColumnIndexOrThrow(Character::gender.name))
            val image = it.getString(it.getColumnIndexOrThrow(Character::image.name))
            val url = it.getString(it.getColumnIndexOrThrow(Character::url.name))
            val created = it.getString(it.getColumnIndexOrThrow(Character::created.name))
            val readValue = it.getInt(it.getColumnIndexOrThrow(Character::read.name))
            val read = readValue == 1

            items.add(Character(id, name, status, species, type, gender, image, url, created, read))
        }
    }

    return items
}
