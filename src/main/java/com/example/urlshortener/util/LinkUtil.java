package com.example.urlshortener.util;

import org.apache.commons.text.RandomStringGenerator;

public class LinkUtil {
  private final RandomStringGenerator randomStringGenerator;

  public LinkUtil() {
    this.randomStringGenerator = new RandomStringGenerator
        .Builder().filteredBy(LinkUtil::isLatinLetterOrDigit)
        .build();
  }

  public String generate() {
    return randomStringGenerator.generate(5);
  }

  private static boolean isLatinLetterOrDigit(int codePoint) {
    return ('a' <= codePoint && codePoint <= 'z') || ('0' <= codePoint && codePoint <= '9');

  }

}
