package com.duka.android.data.repository;

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
public final class AndroidEbmRepository_Factory implements Factory<AndroidEbmRepository> {
  private final Provider<EbmReceiptDao> ebmReceiptDaoProvider;

  public AndroidEbmRepository_Factory(Provider<EbmReceiptDao> ebmReceiptDaoProvider) {
    this.ebmReceiptDaoProvider = ebmReceiptDaoProvider;
  }

  @Override
  public AndroidEbmRepository get() {
    return newInstance(ebmReceiptDaoProvider.get());
  }

  public static AndroidEbmRepository_Factory create(Provider<EbmReceiptDao> ebmReceiptDaoProvider) {
    return new AndroidEbmRepository_Factory(ebmReceiptDaoProvider);
  }

  public static AndroidEbmRepository newInstance(EbmReceiptDao ebmReceiptDao) {
    return new AndroidEbmRepository(ebmReceiptDao);
  }
}
