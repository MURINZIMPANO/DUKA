package com.duka.app.data.repository;

import com.duka.app.data.local.dao.BudgetGoalDao;
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
public final class BudgetGoalRepository_Factory implements Factory<BudgetGoalRepository> {
  private final Provider<BudgetGoalDao> budgetGoalDaoProvider;

  public BudgetGoalRepository_Factory(Provider<BudgetGoalDao> budgetGoalDaoProvider) {
    this.budgetGoalDaoProvider = budgetGoalDaoProvider;
  }

  @Override
  public BudgetGoalRepository get() {
    return newInstance(budgetGoalDaoProvider.get());
  }

  public static BudgetGoalRepository_Factory create(Provider<BudgetGoalDao> budgetGoalDaoProvider) {
    return new BudgetGoalRepository_Factory(budgetGoalDaoProvider);
  }

  public static BudgetGoalRepository newInstance(BudgetGoalDao budgetGoalDao) {
    return new BudgetGoalRepository(budgetGoalDao);
  }
}
