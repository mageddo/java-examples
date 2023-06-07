package com.mageddo.httpclient;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class AutoExpiryPoolingHttpClientConnectionManager extends PoolingHttpClientConnectionManager {

  public AutoExpiryPoolingHttpClientConnectionManager(int size, Duration ttl) {
    super(getDefaultRegistry(), null, null, null, ttl.toMillis(), TimeUnit.MILLISECONDS);
    setMaxTotal(size);
    setDefaultMaxPerRoute(size);
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
    this.enumLeased(entry -> {
      if (entry.isExpired(now)) {
        final ManagedHttpClientConnection connection = entry.getConnection();
        if (connection != null) {
          try {
            connection.shutdown();
          } catch (final IOException iox) {
//            if (log.isDebugEnabled()) {
//              log.debug("I/O exception shutting down connection", iox);
//            }
          }
        }
        entry.close();
//        entry.close();
      }
    });
    super.closeExpiredConnections();
  }
}
