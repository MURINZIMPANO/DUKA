package com.duka.app.di;

import com.duka.app.data.local.DukaDatabase;
import com.duka.app.data.local.dao.FeedbackReportDao;
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
public final class DatabaseModule_ProvideFeedbackReportDaoFactory implements Factory<FeedbackReportDao> {
  private final Provider<DukaDatabase> dbProvider;

  public DatabaseModule_ProvideFeedbackReportDaoFactory(Provider<DukaDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public FeedbackReportDao get() {
    return provideFeedbackReportDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideFeedbackReportDaoFactory create(
      Provider<DukaDatabase> dbProvider) {
    return new DatabaseModule_ProvideFeedbackReportDaoFactory(dbProvider);
  }

  public static FeedbackReportDao provideFeedbackReportDao(DukaDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideFeedbackReportDao(db));
  }
}
