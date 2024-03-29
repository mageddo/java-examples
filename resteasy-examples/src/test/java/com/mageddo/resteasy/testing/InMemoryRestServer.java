package com.mageddo.resteasy.testing;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.jboss.resteasy.core.ResteasyDeploymentImpl;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.junit.rules.ExternalResource;

import io.undertow.Undertow;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Application;
import static org.junit.Assert.assertNotNull;

public class InMemoryRestServer extends ExternalResource {

  public static final String HOST = "localhost";
  private static final Map<String, String> contextParams = new HashMap<>();
  private static final Map<String, String> initParams = new HashMap<>();
  private final Set<Class<?>> classes = new HashSet<>();

  private Application application = null;
  private int port;
  private UndertowJaxrsServer server;
  private Client client;

  public InMemoryRestServer(Application application) {
    this.application = application;
  }

  public InMemoryRestServer(Client client, Class... objects) {
    assertNotNull("Provide at least one controller", objects);
    this.client = client;
    for (final Class o : objects) {
      classes.add(o);
    }
  }

  public InMemoryRestServer(Class... objects) {
    this(new ResteasyClientBuilderImpl().build(), objects);
  }

  public static int findFreePort() throws IOException {
    final ServerSocket server = new ServerSocket(0);
    final int port = server.getLocalPort();
    server.close();
    return port;
  }

  @Override
  protected void before() throws Throwable {

    contextParams.put("contextKey1", "contextValue1");
    contextParams.put("contextKey2", "contextValue2");
    initParams.put("initKey1", "initValue1");
    initParams.put("initKey2", "initValue2");
    initParams.put("resteasy.servlet.context.deployment", "false");

    port = findFreePort();
    this.server = new UndertowJaxrsServer();
    this.server.setContextParams(contextParams);
    this.server.setInitParams(initParams);
    this.server.start(Undertow.builder().addHttpListener(port, HOST));

    final var deployment = new ResteasyDeploymentImpl();
    deployment.setDeploymentSensitiveFactoryEnabled(true);
    if (this.application == null) {
      deployment.setApplication(new Application() {
        @Override
        public Set<Class<?>> getClasses() {
          return classes;
        }
      });
    } else {
      deployment.setApplication(this.application);
    }
    deployment.start();
    this.server.deploy(deployment);
  }

  public String getURL() {
    return String.format("http://%s:%s", HOST, port);
  }

  public int getPort() {
    return port;
  }

  @Override
  protected void after() {
    server.stop();
    client.close();
  }

  public Client getClient() {
    return client;
  }

  public WebTarget target() {
    return client.target(getURL());
  }
}
