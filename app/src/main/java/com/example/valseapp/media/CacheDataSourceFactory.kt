package com.example.valseapp.media

import android.content.Context
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.FileDataSource
import androidx.media3.datasource.cache.CacheDataSink
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import java.io.File

// https://stackoverflow.com/questions/28700391/using-cache-in-exoplayer

@UnstableApi
class CacheDataSourceFactory(
    private val context: Context,
    private val maxCacheSize: Long,
    private val maxFileSize: Long
) :
    DataSource.Factory {
    private val defaultDatasourceFactory = DefaultHttpDataSource.Factory()

    override fun createDataSource(): DataSource {

        val cacheDirectory = File(context.cacheDir, "media3_cache")
        val evictor = LeastRecentlyUsedCacheEvictor(maxCacheSize)
        val databaseProvider = StandaloneDatabaseProvider(context)
        val simpleCache = SimpleCache(cacheDirectory, evictor, databaseProvider)

        return CacheDataSource(
            simpleCache, defaultDatasourceFactory.createDataSource(),
            FileDataSource(), CacheDataSink(simpleCache, maxFileSize),
            CacheDataSource.FLAG_BLOCK_ON_CACHE or CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR, null
        )
    }
}