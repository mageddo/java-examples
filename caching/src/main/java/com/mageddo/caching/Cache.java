package com.mageddo.caching;

import java.time.Duration;
import java.util.concurrent.ConcurrentMap;

import com.github.benmanes.caffeine.cache.Caffeine;

import org.mapdb.DB;
import org.mapdb.DBMaker;

public class Cache<K, V> {

  private final com.github.benmanes.caffeine.cache.Cache<K, V> caffeineCache;
  private final ConcurrentMap<K, V> mapDbMap;

  public Cache() {

    this.mapDbMap = this
        .diskOnlyCache()
        .hashMap("cache", org.mapdb.Serializer.JAVA, org.mapdb.Serializer.JAVA)
        .createOrOpen();

    // guarda uma certa quantidade na memoria por um certo tempo e depois
    // faz fallback para o dbmap que usa disk
    this.caffeineCache = Caffeine
        .newBuilder()
        .maximumSize(1)
        .expireAfterAccess(Duration.ofMinutes(10))
        .build(key -> mapDbMap.get(key));
  }

  DB diskOnlyCache() {
    return DBMaker
        .fileDB("cache.db")
        .transactionEnable()
        .allocateStartSize(0) // não pré-aloca memória
        .allocateIncrement(0) // não aumenta o arquivo automaticamente
        .make();
  }

  DB hibridCache() {
    return DBMaker
        .fileDB("cache.db")
        .fileMmapEnableIfSupported()
        .closeOnJvmShutdown()
        .make();
  }

  public V get(K key) {
    return this.caffeineCache.get(key, this.mapDbMap::get);
  }

  public void put(K key, V value) {
    this.mapDbMap.put(key, value);
    this.caffeineCache.put(key, value);
  }

  public void invalidate(K key) {
    this.mapDbMap.remove(key);
    this.caffeineCache.invalidate(key);
  }
}
