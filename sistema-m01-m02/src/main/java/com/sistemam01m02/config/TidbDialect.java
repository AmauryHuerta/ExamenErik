package com.sistemam01m02.config;

import org.hibernate.dialect.MySQLDialect;

/** TiDB supports row locking, but older Serverless versions do not accept Hibernate's `OF alias`. */
public final class TidbDialect extends MySQLDialect {
  @Override
  public String getForUpdateString(String aliases) {
    return getForUpdateString();
  }

  @Override
  public String getForUpdateNowaitString(String aliases) {
    return getForUpdateNowaitString();
  }

  @Override
  public String getForUpdateSkipLockedString(String aliases) {
    return getForUpdateSkipLockedString();
  }
}
