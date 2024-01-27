package hr.algebra.rickandmortyapp.api

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class RickNMortyWorker(
    private val context: Context,
    workerParams: WorkerParameters
    ) : Worker(context, workerParams) {

    override fun doWork(): Result {
        println("Fetching items")
        RickNMortyFetcher(context).fetchItems(10)
        return Result.success()
    }
}