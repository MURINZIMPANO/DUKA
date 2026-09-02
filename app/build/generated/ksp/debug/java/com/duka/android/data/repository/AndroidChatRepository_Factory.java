package com.duka.android.data.repository;

import com.duka.app.data.local.dao.ChatMessageDao;
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
public final class AndroidChatRepository_Factory implements Factory<AndroidChatRepository> {
  private final Provider<ChatMessageDao> chatMessageDaoProvider;

  public AndroidChatRepository_Factory(Provider<ChatMessageDao> chatMessageDaoProvider) {
    this.chatMessageDaoProvider = chatMessageDaoProvider;
  }

  @Override
  public AndroidChatRepository get() {
    return newInstance(chatMessageDaoProvider.get());
  }

  public static AndroidChatRepository_Factory create(
      Provider<ChatMessageDao> chatMessageDaoProvider) {
    return new AndroidChatRepository_Factory(chatMessageDaoProvider);
  }

  public static AndroidChatRepository newInstance(ChatMessageDao chatMessageDao) {
    return new AndroidChatRepository(chatMessageDao);
  }
}
