package com.duka.app.data.repository;

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
public final class IssueRepository_Factory implements Factory<IssueRepository> {
  private final Provider<IssueReportDao> issueReportDaoProvider;

  public IssueRepository_Factory(Provider<IssueReportDao> issueReportDaoProvider) {
    this.issueReportDaoProvider = issueReportDaoProvider;
  }

  @Override
  public IssueRepository get() {
    return newInstance(issueReportDaoProvider.get());
  }

  public static IssueRepository_Factory create(Provider<IssueReportDao> issueReportDaoProvider) {
    return new IssueRepository_Factory(issueReportDaoProvider);
  }

  public static IssueRepository newInstance(IssueReportDao issueReportDao) {
    return new IssueRepository(issueReportDao);
  }
}
