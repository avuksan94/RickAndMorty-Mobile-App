package hr.algebra.rickandmortyapp.api

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import hr.algebra.rickandmortyapp.dao.Repository
import hr.algebra.rickandmortyapp.factory.getRNMRepository
import hr.algebra.rickandmortyapp.model.Character

private const val AUTHORITY = "hr.algebra.rickandmortyapp.api.provider"
private const val PATH = "characters"
val RNM_PROVIDER_CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$PATH")

private val URI_MATCHER = with(UriMatcher(UriMatcher.NO_MATCH)) {
    addURI(AUTHORITY, PATH, ITEMS) //content://hr.algebra.rickandmortyapp.api.provider/characters
    addURI(AUTHORITY, "$PATH/#", ITEM_ID) //content://hr.algebra.rickandmortyapp.api.provider/characters/5
    this
}

private const val ITEMS = 10
private const val ITEM_ID = 20

class RickNMortyContentProvider : ContentProvider() {
    private lateinit var repository: Repository

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        when(URI_MATCHER.match(uri)) {
            ITEMS -> return repository.delete(selection, selectionArgs)
            ITEM_ID -> {
                uri.lastPathSegment?.let {
                    return repository.delete(
                        "${Character::id.name}=?",
                        arrayOf(it)
                    )
                }
            }
        }
        throw IllegalArgumentException("WRONG URI")
    }

    override fun getType(uri: Uri): String? {
        TODO(
            "Implement this to handle requests for the MIME type of the data" +
                    "at the given URI"
        )
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val id = repository.insert(values)
        return ContentUris.withAppendedId(RNM_PROVIDER_CONTENT_URI, id)
    }

    override fun onCreate(): Boolean {
        repository = getRNMRepository(context)
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? = repository.query(
        projection,
        selection,
        selectionArgs,
        sortOrder
    )

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        when(URI_MATCHER.match(uri)) {
            ITEMS -> return repository.update(values, selection, selectionArgs)
            ITEM_ID -> {
                uri.lastPathSegment?.let {
                    return repository.update(
                        values,
                        "${Character::id.name}=?",
                        arrayOf(it)
                    )
                }
            }
        }
        throw IllegalArgumentException("WRONG URI")

    }
}