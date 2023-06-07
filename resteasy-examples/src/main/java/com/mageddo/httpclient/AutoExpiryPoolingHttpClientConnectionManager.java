package com.mageddo.httpclient;

import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.pool.AbstractConnPool;
import org.apache.http.pool.PoolEntry;

public class AutoExpiryPoolingHttpClientConnectionManager extends PoolingHttpClientConnectionManager {

  private final AbstractConnPool pool;

  public AutoExpiryPoolingHttpClientConnectionManager(int size, Duration ttl) {
    super(getDefaultRegistry(), null, null, null, ttl.toMillis(), TimeUnit.MILLISECONDS);
    setMaxTotal(size);
    setDefaultMaxPerRoute(size);
    try {
      this.pool = (AbstractConnPool) FieldUtils.readField(
          FieldUtils.getField(PoolingHttpClientConnectionManager.class, "pool", true),
          this
      );
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  static Registry<ConnectionSocketFactory> getDefaultRegistry() {
    return RegistryBuilder.<ConnectionSocketFactory>create()
        .register("http", PlainConnectionSocketFactory.getSocketFactory())
        .register("https", SSLConnectionSocketFactory.getSocketFactory())
        .build();
  }

  @Override
  public void closeExpiredConnections() {
    final long now = System.currentTimeMillis();

    final var toClose = new ArrayList<PoolEntry>();
    this.enumLeased(entry -> {
      if (entry.isExpired(now)) {
        toClose.add(entry);
      }
    });
    for (PoolEntry entry : toClose) {
      this.pool.release(entry, false);
    }
    super.closeExpiredConnections();
  }
}
