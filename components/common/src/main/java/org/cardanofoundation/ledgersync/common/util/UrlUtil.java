package org.cardanofoundation.ledgersync.common.util;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class UrlUtil {

  private UrlUtil() {

  }

  private static final char BACKSPACE = '\b';
  private static final List<Character> SPECIAL_CHARACTERS = Arrays.asList('\n', '\0', '\t', '\r',
      '\f', '\\', '\u0007', BACKSPACE);


  /**
   * Remove backspace in url string
   *
   * @param url
   * @return
   */
  public static String formatSpecialCharactersUrl(String url) {

    StringBuilder builder = new StringBuilder();

    for (int i = 0; i < url.length(); i++) {
      char character = url.charAt(i);

      if (SPECIAL_CHARACTERS.contains(character)) {
        if (character == BACKSPACE) {
          builder.deleteCharAt(builder.length() - 1);
        }
        continue;
      }

      builder.append(url.charAt(i));
    }
    return builder.toString().replaceAll(":/{2,}","://");
  }

  /**
   * Check input string with regex is URL or not
   *
   * @param string url string
   * @return boolean if url string return true else return false
   */
  public static boolean isUrl(String string) {
    String urlRegex = "[-a-zA-Z0-9][-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    if (string.startsWith("http")) {
      urlRegex = "^(http(s)?:/{2}|www.)[-a-zA-Z0-9][-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    }

    Pattern pattern = Pattern.compile(urlRegex);
    Matcher matcher = pattern.matcher(string);
    return matcher.matches();
  }
}
