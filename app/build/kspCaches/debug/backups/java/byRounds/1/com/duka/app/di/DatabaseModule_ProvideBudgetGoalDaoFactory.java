package com.duka.app.di;

import com.duka.app.data.local.DukaDatabase;
import com.duka.app.data.local.dao.BudgetGoalDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class DatabaseModule_ProvideBudgetGoalDaoFactory implements Factory<BudgetGoalDao> {
  private final Provider<DukaDatabase> dbProvider;

  public DatabaseModule_ProvideBudgetGoalDaoFactory(Provider<DukaDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public BudgetGoalDao get() {
    return provideBudgetGoalDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideBudgetGoalDaoFactory create(
      Provider<DukaDatabase> dbProvider) {
    return new DatabaseModule_ProvideBudgetGoalDaoFactory(dbProvider);
  }

  public static BudgetGoalDao provideBudgetGoalDao(DukaDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideBudgetGoalDao(db));
  }
}
