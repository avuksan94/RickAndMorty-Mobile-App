package hr.algebra.rickandmortyapp.factory

import android.content.Context
import hr.algebra.rickandmortyapp.dao.RNMSqlHelper

fun getRNMRepository(context: Context?) = RNMSqlHelper(context)