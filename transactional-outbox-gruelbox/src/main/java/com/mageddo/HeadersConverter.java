//package com.mageddo;
//
//import java.util.StringTokenizer;
//
//import com.mageddo.tobby.Header;
//import com.mageddo.tobby.Headers;
//import com.mageddo.tobby.internal.utils.StringUtils;
//
//import lombok.AccessLevel;
//import lombok.NoArgsConstructor;
//
//@NoArgsConstructor(access = AccessLevel.PRIVATE)
//public class HeadersConverter {
//
//  public static final String HEADER_DELIMITER = "\n";
//
//  public static String encodeBase64(Headers headers) {
//    if (headers == null || headers.isEmpty()) {
//      return null;
//    }
//    final StringBuilder sb = new StringBuilder();
//    for (Header header : headers) {
//      sb.append(HeaderConverter.encodeBase64(header));
//      sb.append(HEADER_DELIMITER);
//    }
//    return sb.toString();
//  }
//
//  public static Headers decodeFromBase64(String text) {
//    if (StringUtils.isBlank(text)) {
//      return new Headers();
//    }
//    final Headers headers = new Headers();
//    final StringTokenizer tokenizer = new StringTokenizer(text, HEADER_DELIMITER);
//    while (tokenizer.hasMoreTokens()) {
//      headers.add(HeaderConverter.decodeBase64(tokenizer.nextToken()));
//    }
//    return headers;
//  }
//}
