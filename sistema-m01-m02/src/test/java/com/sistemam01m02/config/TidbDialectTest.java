package com.sistemam01m02.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TidbDialectTest {
  @Test
  void emitsTiDbCompatibleLocksWithoutAliasList() {
    var dialect = new TidbDialect();
    assertEquals(dialect.getForUpdateString(), dialect.getForUpdateString("u"));
    assertEquals(dialect.getForUpdateNowaitString(), dialect.getForUpdateNowaitString("u"));
    assertEquals(
        dialect.getForUpdateSkipLockedString(), dialect.getForUpdateSkipLockedString("u"));
  }
}
