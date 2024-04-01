package com.mageddo.itext.pdftextextraction;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.CharUtils;

public class UpperCaseWordsExtractor {

  public static List<String> extractUppercaseWordsSentenceFromText(String text) {
    final var length = text.length();
    final var buff = new StringBuilder();
    final var sentences = new ArrayList<String>();
    for (int i = 0; i < length; i++) {
      final var c = text.charAt(i);
      if (CharUtils.isAsciiAlphaUpper(c) || isNonTextAfterTheStartOfTheSentence(buff, c)) {
        buff.append(c);
      } else if (!buff.isEmpty() && (!CharUtils.isAsciiAlpha(c) || Character.isWhitespace(buff.charAt(buff.length() - 1)) )) {
        sentences.add(buff.toString().trim());
        buff.delete(0, buff.length());
      } else if (!buff.isEmpty()) {
        buff.delete(0, buff.length());
      }
    }
    if (!buff.isEmpty()) {
      sentences.add(buff.toString().trim());
    }
    return sentences;
  }

  private static boolean isNonTextAfterTheStartOfTheSentence(StringBuilder buff, char c) {
    return (
        CharUtils.isAsciiNumeric(c) || Character.isWhitespace(c)
            || c == '-' || c == '_'
    ) && !buff.isEmpty();
  }
}
