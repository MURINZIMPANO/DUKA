package com.duka.intelligence;

import android.content.Context;
import androidx.work.WorkerParameters;
import dagger.internal.DaggerGenerated;
import dagger.internal.InstanceFactory;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class StockIntelligenceWorker_AssistedFactory_Impl implements StockIntelligenceWorker_AssistedFactory {
  private final StockIntelligenceWorker_Factory delegateFactory;

  StockIntelligenceWorker_AssistedFactory_Impl(StockIntelligenceWorker_Factory delegateFactory) {
    this.delegateFactory = delegateFactory;
  }

  @Override
  public StockIntelligenceWorker create(Context p0, WorkerParameters p1) {
    return delegateFactory.get(p0, p1);
  }

  public static Provider<StockIntelligenceWorker_AssistedFactory> create(
      StockIntelligenceWorker_Factory delegateFactory) {
    return InstanceFactory.create(new StockIntelligenceWorker_AssistedFactory_Impl(delegateFactory));
  }

  public static dagger.internal.Provider<StockIntelligenceWorker_AssistedFactory> createFactoryProvider(
      StockIntelligenceWorker_Factory delegateFactory) {
    return InstanceFactory.create(new StockIntelligenceWorker_AssistedFactory_Impl(delegateFactory));
  }
}
