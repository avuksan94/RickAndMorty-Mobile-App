package hr.algebra.rickandmortyapp.adapter

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.algebra.rickandmortyapp.CharacterPagerActivity
import hr.algebra.rickandmortyapp.POSITION
import hr.algebra.rickandmortyapp.R
import hr.algebra.rickandmortyapp.api.RNM_PROVIDER_CONTENT_URI
import hr.algebra.rickandmortyapp.framework.startActivity
import hr.algebra.rickandmortyapp.model.Character
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation

import java.io.File

class CharacterAdapter (
    private val context: Context,
    private val characters: MutableList<Character>
) : RecyclerView.Adapter<CharacterAdapter.ViewHolder>(){
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivCharacter = itemView.findViewById<ImageView>(R.id.ivCharacter)
        private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        fun bind(character: Character) {
            tvTitle.text = character.name
            Picasso.get()
                .load(File(character.image))
                .error(R.drawable.ricknmortywallpaper)
                .transform(RoundedCornersTransformation(50, 5))
                .into(ivCharacter)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("CharacterAdapter", "onCreateViewHolder called");
        return ViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.charachter, parent, false)
        )
    }

    override fun getItemCount() = characters.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("CharacterAdapter", "onBindViewHolder called for position $position")

        holder.itemView.setOnLongClickListener {
            //delete
            val character = characters[position]
            context.contentResolver.delete(
                ContentUris.withAppendedId(RNM_PROVIDER_CONTENT_URI, character.id.toLong()),
                null, null
            )
            File(character.image).delete()
            characters.removeAt(position)
            notifyDataSetChanged()

            true
        }

        holder.itemView.setOnClickListener {
            // Edit logic
            context.startActivity<CharacterPagerActivity>(POSITION, position)
        }

        holder.bind(characters[position])
    }
}