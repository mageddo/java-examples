package com.mageddo;

import java.util.StringTokenizer;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeaders;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HeadersConverter {

  public static final String HEADER_DELIMITER = "\n";

  public static String encodeBase64(Headers headers) {
    if (headers == null) {
      return null;
    }
    final StringBuilder sb = new StringBuilder();
    for (Header header : headers) {
      sb.append(HeaderConverter.encodeBase64(header));
      sb.append(HEADER_DELIMITER);
    }
    return sb.toString();
  }

  public static Headers decodeFromBase64(String text) {
    if (StringUtils.isBlank(text)) {
      return new RecordHeaders();
    }
    final Headers headers = new RecordHeaders();
    final StringTokenizer tokenizer = new StringTokenizer(text, HEADER_DELIMITER);
    while (tokenizer.hasMoreTokens()) {
      headers.add(HeaderConverter.decodeBase64(tokenizer.nextToken()));
    }
    return headers;
  }
}
