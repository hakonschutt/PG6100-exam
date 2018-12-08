package org.bjh.cacheManager

import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.concurrent.ConcurrentMapCache
import org.springframework.cache.support.SimpleCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableCaching
class CacheConfig {
    @Bean
    fun cacheManager(): CacheManager {
        val smpCacheManager =SimpleCacheManager()
        val cacheList:MutableList<Cache> = ArrayList()
        //Add your caches here:
        cacheList.add(ConcurrentMapCache("venuesCache"))
        cacheList.add(ConcurrentMapCache("moviesCache"))
        smpCacheManager.setCaches(cacheList)
        return smpCacheManager
    }
}