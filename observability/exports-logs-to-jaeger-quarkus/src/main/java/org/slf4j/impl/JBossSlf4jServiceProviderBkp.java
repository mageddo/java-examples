/*
 * JBoss, Home of Professional Open Source.
 *
 * Copyright 2023 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.slf4j.impl;

import com.mageddo.utils.Tmp;

import org.slf4j.ILoggerFactory;
import org.slf4j.IMarkerFactory;
import org.slf4j.helpers.BasicMarkerFactory;
import org.slf4j.spi.MDCAdapter;
import org.slf4j.spi.SLF4JServiceProvider;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
public class JBossSlf4jServiceProviderBkp implements SLF4JServiceProvider {

  private final ILoggerFactory loggerFactory;
  private final IMarkerFactory markerFactory;
  private final MDCAdapter mdcAdapter;

  public JBossSlf4jServiceProviderBkp() {
    System.out.println("aha!!!");
    final var foundLoggerFactory = Tmp.findBestLoggerFactory();
    this.loggerFactory = foundLoggerFactory;
    this.markerFactory = new BasicMarkerFactory();
    this.mdcAdapter = new Slf4jMDCAdapter();
  }

  @Override
  public ILoggerFactory getLoggerFactory() {
    return loggerFactory;
  }

  @Override
  public IMarkerFactory getMarkerFactory() {
    return markerFactory;
  }

  @Override
  public MDCAdapter getMDCAdapter() {
    return mdcAdapter;
  }

  @Override
  public String getRequestedApiVersion() {
    return "2.0.6";
  }

  @Override
  public void initialize() {
    // do nothing
  }
}
