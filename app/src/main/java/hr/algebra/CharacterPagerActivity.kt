package hr.algebra.rickandmortyapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import hr.algebra.rickandmortyapp.adapter.CharacterAdapter
import hr.algebra.rickandmortyapp.adapter.CharacterPagerAdapter
import hr.algebra.rickandmortyapp.databinding.ActivityCharacterPagerBinding
import hr.algebra.rickandmortyapp.model.Character
import hr.algebra.rickandmortyapp.framework.fetchItems

const val POSITION = "hr.algebra.rickandmortyapp.item_pos"

class CharacterPagerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCharacterPagerBinding

    private lateinit var characters: MutableList<Character>
    private var position = 0

    companion object {
        private const val TAG = "CharacterPagerActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharacterPagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "Activity onCreate started")
        initPager()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initPager() {
        Log.d(TAG, "Initializing pager")
        characters = fetchItems()
        Log.d(TAG, "Fetched items: ${characters.size}")

        position = intent.getIntExtra(POSITION, position)
        Log.d(TAG, "Received position: $position")

        binding.viewPager.adapter = CharacterPagerAdapter(this, characters)
        binding.viewPager.currentItem = position
    }

    override fun onSupportNavigateUp(): Boolean {
        Log.d(TAG, "Navigating up")
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }
}
