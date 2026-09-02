package com.duka.android.data.repository;

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
public final class AndroidFeedbackReportRepository_Factory implements Factory<AndroidFeedbackReportRepository> {
  private final Provider<FeedbackReportDao> feedbackReportDaoProvider;

  public AndroidFeedbackReportRepository_Factory(
      Provider<FeedbackReportDao> feedbackReportDaoProvider) {
    this.feedbackReportDaoProvider = feedbackReportDaoProvider;
  }

  @Override
  public AndroidFeedbackReportRepository get() {
    return newInstance(feedbackReportDaoProvider.get());
  }

  public static AndroidFeedbackReportRepository_Factory create(
      Provider<FeedbackReportDao> feedbackReportDaoProvider) {
    return new AndroidFeedbackReportRepository_Factory(feedbackReportDaoProvider);
  }

  public static AndroidFeedbackReportRepository newInstance(FeedbackReportDao feedbackReportDao) {
    return new AndroidFeedbackReportRepository(feedbackReportDao);
  }
}
