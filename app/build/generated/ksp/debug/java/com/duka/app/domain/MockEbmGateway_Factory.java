package com.duka.app.domain;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class MockEbmGateway_Factory implements Factory<MockEbmGateway> {
  @Override
  public MockEbmGateway get() {
    return newInstance();
  }

  public static MockEbmGateway_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static MockEbmGateway newInstance() {
    return new MockEbmGateway();
  }

  private static final class InstanceHolder {
    private static final MockEbmGateway_Factory INSTANCE = new MockEbmGateway_Factory();
  }
}
