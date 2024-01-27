package hr.algebra.rickandmortyapp.adapter

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.algebra.rickandmortyapp.R
import hr.algebra.rickandmortyapp.api.RNM_PROVIDER_CONTENT_URI
import hr.algebra.rickandmortyapp.model.Character
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import java.io.File

class CharacterPagerAdapter(
    private val context: Context,
    private val characters: MutableList<Character>
) : RecyclerView.Adapter<CharacterPagerAdapter.ViewHolder>() {

    companion object {
        private const val TAG = "CharacterPagerAdapter"
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivCharacter = itemView.findViewById<ImageView>(R.id.ivCharacter)
        val ivRead = itemView.findViewById<ImageView>(R.id.ivRead)

        private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        private val tvStatus = itemView.findViewById<TextView>(R.id.tvStatus)
        private val tvGender = itemView.findViewById<TextView>(R.id.tvGender)
        private val tvType = itemView.findViewById<TextView>(R.id.tvType)

        fun bind(character: Character) {
            try {
                tvTitle.text = character.name
                tvStatus.text = character.status
                tvGender.text = character.gender
                tvType.text = character.type
                Picasso.get()
                    .load(File(character.image))
                    .error(R.drawable.ricknmortywallpaper)
                    .transform(RoundedCornersTransformation(50, 5))
                    .into(ivCharacter)
                ivRead.setImageResource(if (character.read) R.drawable.green_flag else R.drawable.red_flag)
            } catch (e: Exception) {
                Log.d(TAG, "Error binding character", e)
            }
        }
    }

    //override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    //    return ViewHolder(LayoutInflater.from(context).inflate(R.layout.character_pager, parent, false))
    //}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d(TAG, "Binding character")
        val view = LayoutInflater.from(context).inflate(R.layout.character_pager, parent, false)

        // Set the layout parameters to MATCH_PARENT for both width and height
        val layoutParams = RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            RecyclerView.LayoutParams.MATCH_PARENT
        )
        view.layoutParams = layoutParams
        return ViewHolder(view)
    }


    override fun getItemCount() = characters.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            holder.ivRead.setOnClickListener { updateItem(position) }
            holder.bind(characters[position])
        } catch (e: Exception) {
            Log.d(TAG, "Error in onBindViewHolder", e)
        }
    }

    private fun updateItem(position: Int) {
        try {
            val item = characters[position]
            item.read = !item.read
            context.contentResolver.update(
                ContentUris.withAppendedId(RNM_PROVIDER_CONTENT_URI, item.id),
                ContentValues().apply { put(Character::read.name, item.read) },
                null,
                null
            )
            notifyItemChanged(position)
        } catch (e: Exception) {
            Log.d(TAG, "Error updating item", e)
        }
    }
}
