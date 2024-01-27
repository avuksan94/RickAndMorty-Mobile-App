package hr.algebra.rickandmortyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import hr.algebra.rickandmortyapp.api.RickNMortyWorker
import hr.algebra.rickandmortyapp.databinding.ActivitySplashScreenBinding
import hr.algebra.rickandmortyapp.framework.applyAnimation
import hr.algebra.rickandmortyapp.framework.callDelayed
import hr.algebra.rickandmortyapp.framework.getBooleanPreference
import hr.algebra.rickandmortyapp.framework.isOnline
import hr.algebra.rickandmortyapp.framework.startActivity

private const val DELAY = 3000L

const val DATA_IMPORTED = "hr.algebra.rickandmortyapp.data_imported"

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startAnimations()
        redirect()
    }

    private fun startAnimations() {
        binding.ivSplash.applyAnimation(R.anim.rotate)
        binding.tvSplash.applyAnimation(R.anim.blink)
    }

    private fun redirect() {
        if (getBooleanPreference(DATA_IMPORTED)) {
            callDelayed(DELAY) {
                startActivity<HostActivity>()
            }
        } else {
            if (isOnline()) {
                println("ONLINE")
                WorkManager.getInstance(this).apply {
                    enqueueUniqueWork(
                        DATA_IMPORTED,
                        ExistingWorkPolicy.KEEP,
                        OneTimeWorkRequest.from(RickNMortyWorker::class.java)
                    )
                }


            } else {
                binding.tvSplash.text = getString(R.string.no_internet)
                callDelayed(DELAY) {
                    finish()
                }
            }
        }
    }

}