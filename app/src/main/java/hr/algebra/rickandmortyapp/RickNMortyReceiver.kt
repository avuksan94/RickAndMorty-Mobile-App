package hr.algebra.rickandmortyapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import hr.algebra.rickandmortyapp.framework.setBooleanPreference
import hr.algebra.rickandmortyapp.framework.startActivity

class RickNMortyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        context.setBooleanPreference(DATA_IMPORTED)
        context.startActivity<HostActivity>()
    }
}