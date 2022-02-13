package com.example.urlshortener.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class TokenUtil {

  public String createToken(Map<String, String> params, String secretKey) {
    StringBuilder stringBuilder = new StringBuilder();
    Map<String, String> map = new TreeMap<>(params);
    for (Entry<String, String> stringStringEntry : map.entrySet()) {
      stringBuilder.append(stringStringEntry.getValue());
    }
    stringBuilder.append(secretKey);
    String resultString = stringBuilder.toString();
    String sha1 = "";
    try {
      MessageDigest crypt = MessageDigest.getInstance("SHA-1");
      crypt.reset();
      crypt.update(resultString.getBytes(StandardCharsets.UTF_8));
      sha1 = byteToHex(crypt.digest());
    }
    catch(NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return sha1;
  }

  private String byteToHex(final byte[] hash) {
    Formatter formatter = new Formatter();
    for (byte b : hash) {
      formatter.format("%02x", b);
    }
    String result = formatter.toString();
    formatter.close();
    return result;
  }
}
