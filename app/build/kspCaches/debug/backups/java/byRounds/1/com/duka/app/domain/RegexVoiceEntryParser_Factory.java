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
public final class RegexVoiceEntryParser_Factory implements Factory<RegexVoiceEntryParser> {
  @Override
  public RegexVoiceEntryParser get() {
    return newInstance();
  }

  public static RegexVoiceEntryParser_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static RegexVoiceEntryParser newInstance() {
    return new RegexVoiceEntryParser();
  }

  private static final class InstanceHolder {
    private static final RegexVoiceEntryParser_Factory INSTANCE = new RegexVoiceEntryParser_Factory();
  }
}
