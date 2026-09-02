package com.duka.android.data.repository;

import com.duka.app.data.local.dao.IssueReportDao;
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
public final class AndroidIssueRepository_Factory implements Factory<AndroidIssueRepository> {
  private final Provider<IssueReportDao> issueReportDaoProvider;

  public AndroidIssueRepository_Factory(Provider<IssueReportDao> issueReportDaoProvider) {
    this.issueReportDaoProvider = issueReportDaoProvider;
  }

  @Override
  public AndroidIssueRepository get() {
    return newInstance(issueReportDaoProvider.get());
  }

  public static AndroidIssueRepository_Factory create(
      Provider<IssueReportDao> issueReportDaoProvider) {
    return new AndroidIssueRepository_Factory(issueReportDaoProvider);
  }

  public static AndroidIssueRepository newInstance(IssueReportDao issueReportDao) {
    return new AndroidIssueRepository(issueReportDao);
  }
}
