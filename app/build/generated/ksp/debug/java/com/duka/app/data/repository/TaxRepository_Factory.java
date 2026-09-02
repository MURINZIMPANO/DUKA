package com.duka.app.data.repository;

import com.duka.app.data.local.dao.TaxProfileDao;
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
public final class TaxRepository_Factory implements Factory<TaxRepository> {
  private final Provider<TaxProfileDao> taxProfileDaoProvider;

  public TaxRepository_Factory(Provider<TaxProfileDao> taxProfileDaoProvider) {
    this.taxProfileDaoProvider = taxProfileDaoProvider;
  }

  @Override
  public TaxRepository get() {
    return newInstance(taxProfileDaoProvider.get());
  }

  public static TaxRepository_Factory create(Provider<TaxProfileDao> taxProfileDaoProvider) {
    return new TaxRepository_Factory(taxProfileDaoProvider);
  }

  public static TaxRepository newInstance(TaxProfileDao taxProfileDao) {
    return new TaxRepository(taxProfileDao);
  }
}
