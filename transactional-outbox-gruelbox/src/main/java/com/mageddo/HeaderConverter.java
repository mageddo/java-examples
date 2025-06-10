package com.mageddo;

import org.apache.kafka.common.header.Header;

import lombok.NoArgsConstructor;

import java.util.Base64;

@NoArgsConstructor
public class HeaderConverter {

  public static String encodeBase64(Header header) {
    return String.format("%s:%s", header.key(), Base64.getEncoder().encodeToString(header.value()));
  }

  public static Header decodeBase64(String encodedHeader) {
    final String[] tokens = encodedHeader.split(":");
    return HeaderUtils.of(tokens[0], Base64.getDecoder().decode(tokens[1]));
  }
}
