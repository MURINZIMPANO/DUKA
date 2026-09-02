package com.duka.app.data.repository;

import com.duka.app.data.local.dao.FeedbackReportDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class FeedbackReportRepository_Factory implements Factory<FeedbackReportRepository> {
  private final Provider<FeedbackReportDao> feedbackReportDaoProvider;

  public FeedbackReportRepository_Factory(Provider<FeedbackReportDao> feedbackReportDaoProvider) {
    this.feedbackReportDaoProvider = feedbackReportDaoProvider;
  }

  @Override
  public FeedbackReportRepository get() {
    return newInstance(feedbackReportDaoProvider.get());
  }

  public static FeedbackReportRepository_Factory create(
      Provider<FeedbackReportDao> feedbackReportDaoProvider) {
    return new FeedbackReportRepository_Factory(feedbackReportDaoProvider);
  }

  public static FeedbackReportRepository newInstance(FeedbackReportDao feedbackReportDao) {
    return new FeedbackReportRepository(feedbackReportDao);
  }
}
