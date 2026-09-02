package com.duka.shared.db

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import com.duka.shared.db.shared.newInstance
import com.duka.shared.db.shared.schema
import kotlin.Unit

public interface DukaDatabase : Transacter {
  public val dukaQueries: DukaQueries

  public companion object {
    public val Schema: SqlSchema<QueryResult.Value<Unit>>
      get() = DukaDatabase::class.schema

    public operator fun invoke(driver: SqlDriver): DukaDatabase =
        DukaDatabase::class.newInstance(driver)
  }
}
