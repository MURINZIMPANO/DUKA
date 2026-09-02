package com.duka.app.data.repository;

import com.duka.app.data.local.dao.EbmReceiptDao;
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
public final class EbmRepository_Factory implements Factory<EbmRepository> {
  private final Provider<EbmReceiptDao> ebmReceiptDaoProvider;

  public EbmRepository_Factory(Provider<EbmReceiptDao> ebmReceiptDaoProvider) {
    this.ebmReceiptDaoProvider = ebmReceiptDaoProvider;
  }

  @Override
  public EbmRepository get() {
    return newInstance(ebmReceiptDaoProvider.get());
  }

  public static EbmRepository_Factory create(Provider<EbmReceiptDao> ebmReceiptDaoProvider) {
    return new EbmRepository_Factory(ebmReceiptDaoProvider);
  }

  public static EbmRepository newInstance(EbmReceiptDao ebmReceiptDao) {
    return new EbmRepository(ebmReceiptDao);
  }
}
