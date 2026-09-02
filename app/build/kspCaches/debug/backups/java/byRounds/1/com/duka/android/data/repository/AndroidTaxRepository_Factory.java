package com.duka.android.data.repository;

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
public final class AndroidTaxRepository_Factory implements Factory<AndroidTaxRepository> {
  private final Provider<TaxProfileDao> taxProfileDaoProvider;

  public AndroidTaxRepository_Factory(Provider<TaxProfileDao> taxProfileDaoProvider) {
    this.taxProfileDaoProvider = taxProfileDaoProvider;
  }

  @Override
  public AndroidTaxRepository get() {
    return newInstance(taxProfileDaoProvider.get());
  }

  public static AndroidTaxRepository_Factory create(Provider<TaxProfileDao> taxProfileDaoProvider) {
    return new AndroidTaxRepository_Factory(taxProfileDaoProvider);
  }

  public static AndroidTaxRepository newInstance(TaxProfileDao taxProfileDao) {
    return new AndroidTaxRepository(taxProfileDao);
  }
}
