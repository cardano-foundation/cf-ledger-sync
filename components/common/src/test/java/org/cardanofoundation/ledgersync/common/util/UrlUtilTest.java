package org.cardanofoundation.ledgersync.common.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UrlUtilTest {

  @Test
  void oneBlackSpace() {
    String url = "stackoverflow/\b.com";
    String actual = UrlUtil.formatSpecialCharactersUrl(url);
    String expect = "stackoverflow.com";
    Assertions.assertEquals(expect, actual);
  }

  @Test
  void twoBlackSpace() {
    String url = "stackoverflow/\b/\b.com";
    String actual = UrlUtil.formatSpecialCharactersUrl(url);
    String expect = "stackoverflow.com";
    Assertions.assertEquals(expect, actual);
  }

  @Test
  void twoSeparateBlackSpace() {
    String url = "stackoverflow/\b.co/\bm";
    String actual = UrlUtil.formatSpecialCharactersUrl(url);
    String expect = "stackoverflow.com";
    Assertions.assertEquals(expect, actual);
  }

  @Test
  void endLine() {
    String url = "stackoverflow\n.co/\bm";
    String actual = UrlUtil.formatSpecialCharactersUrl(url);
    String expect = "stackoverflow.com";
    Assertions.assertEquals(expect, actual);
  }

  @Test
  void alertMark() {
    String url = "stackoverflow\u0007.co/\bm";
    String actual = UrlUtil.formatSpecialCharactersUrl(url);
    String expect = "stackoverflow.com";
    Assertions.assertEquals(expect, actual);
  }

  @Test
  void formFeed() {
    String url = "stackoverflow\f.co/\bm";
    String actual = UrlUtil.formatSpecialCharactersUrl(url);
    String expect = "stackoverflow.com";
    Assertions.assertEquals(expect, actual);
  }

  @Test
  void carriageReturn() {
    String url = "stackoverflow\r.co/\bm";
    String actual = UrlUtil.formatSpecialCharactersUrl(url);
    String expect = "stackoverflow.com";
    Assertions.assertEquals(expect, actual);
  }

  @Test
  void nullCharacter() {
    String url = "stackoverflow\0.co/\bm";
    String actual = UrlUtil.formatSpecialCharactersUrl(url);
    String expect = "stackoverflow.com";
    Assertions.assertEquals(expect, actual);
  }

  @Test
  void httpUrl() {
    String url = "http://adroit.ventures/cardano/test1_poolMetadata.json";
    boolean actual = UrlUtil.isUrl(url);
    Assertions.assertEquals(Boolean.TRUE, actual);
  }

  @Test
  void notContainHttpUrl() {
    String url = "shorturl.at/knAK2";
    boolean actual = UrlUtil.isUrl(url);
    Assertions.assertEquals(Boolean.TRUE, actual);
  }

  @Test
  void missingSlat(){
    String url = "https:/gist.github.com/jmdv1974/610360d7760d820fc052c692f879336f";
    boolean actual = UrlUtil.isUrl(url);
    Assertions.assertEquals(Boolean.FALSE, actual);
  }

  @Test
  void httpsUrl() {
    String url = "https://adroit.ventures/cardano/test1_poolMetadata.json";
    boolean actual = UrlUtil.isUrl(url);
    Assertions.assertEquals(Boolean.TRUE, actual);
  }

  @Test
  void httpsContaintwwwUrl() {
    String url = "https://www.adroit.ventures/cardano/test1_poolMetadata.json";
    boolean actual = UrlUtil.isUrl(url);
    Assertions.assertEquals(Boolean.TRUE, actual);
  }
  @Test
  void wwwUrl() {
    String url = "www.fourbrothersforada.compoolMetaData.json";
    boolean actual = UrlUtil.isUrl(url);
    Assertions.assertEquals(Boolean.TRUE, actual);
  }

  @Test
  void httpsMultiSlashQuarda() {
    String url = "https:////red-sky.one/poolmeta.json";
    boolean actual = UrlUtil.isUrl(url);
    Assertions.assertEquals(Boolean.FALSE, actual);
  }


}
