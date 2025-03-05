package com.mageddo.logsastracing;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public interface LoggerDAO {

  void addLogger(Logger logger);

  List<Logger> findAvailableLoggers();

  Set<String> findConfiguredLoggersAsStringSet();
}
