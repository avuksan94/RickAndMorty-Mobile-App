package hr.algebra.rickandmortyapp.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import hr.algebra.rickandmortyapp.model.Character

private const val DB_NAME = "characters.db"
private const val DB_VERSION = 1
private const val TABLE_NAME = "characters"
private val CREATE_TABLE = "create table $TABLE_NAME( " +
        "${Character::id.name} integer primary key, " +
        "${Character::name.name} text not null, " +
        "${Character::status.name} text not null, " +
        "${Character::species.name} text not null, " +
        "${Character::type.name} text not null, " +
        "${Character::gender.name} text not null, " +
        //"${Character::origin.name} text not null, " +
        //"${Character::location.name} text not null, " +
        "${Character::image.name} text not null, " +
        //"${Character::episode.name} text not null, " +
        "${Character::url.name} text not null, " +
        "${Character::created.name} text not null, " +
        "${Character::read.name} integer not null " +
        ")"

private const val DROP_TABLE = "drop table $TABLE_NAME"

class RNMSqlHelper (context: Context?) : SQLiteOpenHelper(
    context, DB_NAME, null, DB_VERSION
), Repository {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(DROP_TABLE)
        onCreate(db)
    }

    override fun delete(selection: String?, selectionArgs: Array<String>?)
            = writableDatabase.delete(
        TABLE_NAME,
        selection,
        selectionArgs
    )

    override fun insert(values: ContentValues?) =
        writableDatabase.insert(
            TABLE_NAME,
            null,
            values
        )

    override fun query(
        projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor = readableDatabase.query(
        TABLE_NAME,
        projection,
        selection,
        selectionArgs,
        null,
        null,
        sortOrder
    )

    override fun update(
        values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ) = writableDatabase.update(
        TABLE_NAME,
        values,
        selection,
        selectionArgs
    )
}