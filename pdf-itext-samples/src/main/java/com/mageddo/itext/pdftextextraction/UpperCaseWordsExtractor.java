package com.mageddo.itext.pdftextextraction;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.CharUtils;

public class UpperCaseWordsExtractor {

  public static List<String> extractUppercaseWordsSentenceFromText(String text) {
    return extractUppercaseWordsSentenceFromText(text, 3);
  }

  public static List<String> extractUppercaseWordsSentenceFromText(String text, int minWordLength) {
    final var length = text.length();
    final var buff = new StringBuilder();
    final var sentences = new ArrayList<String>();
    for (int i = 0; i < length; i++) {
      final var c = text.charAt(i);
      if (CharUtils.isAsciiAlphaUpper(c) || isNonTextAfterTheStartOfTheSentence(buff, c)) {
        buff.append(c);
      } else if (sentenceIsValidToBeConsidered(buff, minWordLength, c)) {
        sentences.add(buff.toString().trim());
        buff.delete(0, buff.length());
      } else if (!buff.isEmpty()) {
        buff.delete(0, buff.length());
      }
    }
    if (metMinLength(buff, minWordLength)) {
      sentences.add(buff.toString().trim());
    }
    return sentences;
  }

  private static boolean metMinLength(StringBuilder buff, int minWordLength) {
    return buff.toString().trim().length() >= minWordLength;
  }

  private static boolean sentenceIsValidToBeConsidered(
      StringBuilder buff, int minWordLength, char c
  ) {
    return metMinLength(buff, minWordLength) && (!CharUtils.isAsciiAlpha(c)
                                                 || Character.isWhitespace(buff.charAt(buff.length() - 1)));
  }

  private static boolean isNonTextAfterTheStartOfTheSentence(StringBuilder buff, char c) {
    return !(c == '\r' || c == '\n')
           && (
               CharUtils.isAsciiNumeric(c) || Character.isWhitespace(c)
               || c == '-' || c == '_'
           ) && !buff.isEmpty();
  }
}
