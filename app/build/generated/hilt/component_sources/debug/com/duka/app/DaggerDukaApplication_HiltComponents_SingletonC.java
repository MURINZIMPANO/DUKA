package com.duka.app;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.Preferences;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.duka.app.data.local.DukaDatabase;
import com.duka.app.data.local.dao.AppNotificationDao;
import com.duka.app.data.local.dao.BudgetGoalDao;
import com.duka.app.data.local.dao.BusinessDao;
import com.duka.app.data.local.dao.ChatMessageDao;
import com.duka.app.data.local.dao.EbmReceiptDao;
import com.duka.app.data.local.dao.EmployeeDao;
import com.duka.app.data.local.dao.ExpenseDao;
import com.duka.app.data.local.dao.FeedbackReportDao;
import com.duka.app.data.local.dao.IssueReportDao;
import com.duka.app.data.local.dao.ProductAlertDao;
import com.duka.app.data.local.dao.ProductDao;
import com.duka.app.data.local.dao.PromoDao;
import com.duka.app.data.local.dao.PurchaseDao;
import com.duka.app.data.local.dao.RestockRequestDao;
import com.duka.app.data.local.dao.SaleDao;
import com.duka.app.data.local.dao.ShopRatingDao;
import com.duka.app.data.local.dao.StockAdjustmentDao;
import com.duka.app.data.local.dao.TaxProfileDao;
import com.duka.app.data.local.dao.UserDao;
import com.duka.app.data.local.dao.WholesalerDao;
import com.duka.app.data.repository.AppNotificationRepository;
import com.duka.app.data.repository.BudgetGoalRepository;
import com.duka.app.data.repository.BusinessRepository;
import com.duka.app.data.repository.ChatRepository;
import com.duka.app.data.repository.EbmRepository;
import com.duka.app.data.repository.EmployeeRepository;
import com.duka.app.data.repository.ExpenseRepository;
import com.duka.app.data.repository.FeedbackReportRepository;
import com.duka.app.data.repository.IssueRepository;
import com.duka.app.data.repository.ProductAlertRepository;
import com.duka.app.data.repository.ProductRepository;
import com.duka.app.data.repository.PromoRepository;
import com.duka.app.data.repository.PurchaseRepository;
import com.duka.app.data.repository.RestockRequestRepository;
import com.duka.app.data.repository.SaleRepository;
import com.duka.app.data.repository.ShopRatingRepository;
import com.duka.app.data.repository.StockAdjustmentRepository;
import com.duka.app.data.repository.TaxRepository;
import com.duka.app.data.repository.UserRepository;
import com.duka.app.data.repository.WholesalerRepository;
import com.duka.app.data.session.SessionManager;
import com.duka.app.di.DatabaseModule_ProvideAppNotificationDaoFactory;
import com.duka.app.di.DatabaseModule_ProvideBudgetGoalDaoFactory;
import com.duka.app.di.DatabaseModule_ProvideBusinessDaoFactory;
import com.duka.app.di.DatabaseModule_ProvideChatMessageDaoFactory;
import com.duka.app.di.DatabaseModule_ProvideDatabaseFactory;
import com.duka.app.di.DatabaseModule_ProvideEbmReceiptDaoFactory;
import com.duka.app.di.DatabaseModule_ProvideEmployeeDaoFactory;
import com.duka.app.di.DatabaseModule_ProvideExpenseDaoFactory;
import com.duka.app.di.DatabaseModule_ProvideFeedbackReportDaoFactory;
import com.duka.app.di.DatabaseModule_ProvideIssueReportDaoFactory;
import com.duka.app.di.DatabaseModule_ProvideProductAlertDaoFactory;
import com.duka.app.di.DatabaseModule_ProvideProductDaoFactory;
import com.duka.app.di.DatabaseModule_ProvidePromoDaoFactory;
import com.duka.app.di.DatabaseModule_ProvidePurchaseDaoFactory;
import com.duka.app.di.DatabaseModule_ProvideRestockRequestDaoFactory;
import com.duka.app.di.DatabaseModule_ProvideSaleDaoFactory;
import com.duka.app.di.DatabaseModule_ProvideShopRatingDaoFactory;
import com.duka.app.di.DatabaseModule_ProvideStockAdjustmentDaoFactory;
import com.duka.app.di.DatabaseModule_ProvideTaxProfileDaoFactory;
import com.duka.app.di.DatabaseModule_ProvideUserDaoFactory;
import com.duka.app.di.DatabaseModule_ProvideWholesalerDaoFactory;
import com.duka.app.di.SessionModule_ProvideDataStoreFactory;
import com.duka.app.di.SessionModule_ProvideSessionManagerFactory;
import com.duka.app.domain.MockEbmGateway;
import com.duka.app.domain.RegexVoiceEntryParser;
import com.duka.app.viewmodel.SessionViewModel;
import com.duka.app.viewmodel.SessionViewModel_HiltModules;
import com.duka.employee.management.EmployeeManagementViewModel;
import com.duka.employee.management.EmployeeManagementViewModel_HiltModules;
import com.duka.settings.shared.SettingsViewModel;
import com.duka.settings.shared.SettingsViewModel_HiltModules;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideApplicationFactory;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

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
public final class DaggerDukaApplication_HiltComponents_SingletonC {
  private DaggerDukaApplication_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public DukaApplication_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements DukaApplication_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public DukaApplication_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements DukaApplication_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public DukaApplication_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements DukaApplication_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public DukaApplication_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements DukaApplication_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public DukaApplication_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements DukaApplication_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public DukaApplication_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements DukaApplication_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public DukaApplication_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements DukaApplication_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public DukaApplication_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends DukaApplication_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends DukaApplication_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends DukaApplication_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends DukaApplication_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
      injectMainActivity2(mainActivity);
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(MapBuilder.<String, Boolean>newMapBuilder(3).put(LazyClassKeyProvider.com_duka_employee_management_EmployeeManagementViewModel, EmployeeManagementViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_duka_app_viewmodel_SessionViewModel, SessionViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_duka_settings_shared_SettingsViewModel, SettingsViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    private MainActivity injectMainActivity2(MainActivity instance) {
      MainActivity_MembersInjector.injectBusinessRepository(instance, singletonCImpl.businessRepositoryProvider.get());
      MainActivity_MembersInjector.injectProductRepository(instance, singletonCImpl.productRepositoryProvider.get());
      MainActivity_MembersInjector.injectSaleRepository(instance, singletonCImpl.saleRepositoryProvider.get());
      MainActivity_MembersInjector.injectEmployeeRepository(instance, singletonCImpl.employeeRepositoryProvider.get());
      MainActivity_MembersInjector.injectChatRepository(instance, singletonCImpl.chatRepositoryProvider.get());
      MainActivity_MembersInjector.injectPromoRepository(instance, singletonCImpl.promoRepositoryProvider.get());
      MainActivity_MembersInjector.injectIssueRepository(instance, singletonCImpl.issueRepositoryProvider.get());
      MainActivity_MembersInjector.injectEbmRepository(instance, singletonCImpl.ebmRepositoryProvider.get());
      MainActivity_MembersInjector.injectTaxRepository(instance, singletonCImpl.taxRepositoryProvider.get());
      MainActivity_MembersInjector.injectEbmGateway(instance, singletonCImpl.mockEbmGatewayProvider.get());
      MainActivity_MembersInjector.injectVoiceEntryParser(instance, singletonCImpl.regexVoiceEntryParserProvider.get());
      MainActivity_MembersInjector.injectPurchaseRepository(instance, singletonCImpl.purchaseRepositoryProvider.get());
      MainActivity_MembersInjector.injectBudgetGoalRepository(instance, singletonCImpl.budgetGoalRepositoryProvider.get());
      MainActivity_MembersInjector.injectFeedbackReportRepository(instance, singletonCImpl.feedbackReportRepositoryProvider.get());
      MainActivity_MembersInjector.injectShopRatingRepository(instance, singletonCImpl.shopRatingRepositoryProvider.get());
      MainActivity_MembersInjector.injectWholesalerRepository(instance, singletonCImpl.wholesalerRepositoryProvider.get());
      MainActivity_MembersInjector.injectRestockRequestRepository(instance, singletonCImpl.restockRequestRepositoryProvider.get());
      MainActivity_MembersInjector.injectSessionManager(instance, singletonCImpl.provideSessionManagerProvider.get());
      MainActivity_MembersInjector.injectProductAlertRepository(instance, singletonCImpl.productAlertRepositoryProvider.get());
      MainActivity_MembersInjector.injectAppNotificationRepository(instance, singletonCImpl.appNotificationRepositoryProvider.get());
      MainActivity_MembersInjector.injectStockAdjustmentRepository(instance, singletonCImpl.stockAdjustmentRepositoryProvider.get());
      MainActivity_MembersInjector.injectExpenseRepository(instance, singletonCImpl.expenseRepositoryProvider.get());
      return instance;
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_duka_employee_management_EmployeeManagementViewModel = "com.duka.employee.management.EmployeeManagementViewModel";

      static String com_duka_settings_shared_SettingsViewModel = "com.duka.settings.shared.SettingsViewModel";

      static String com_duka_app_viewmodel_SessionViewModel = "com.duka.app.viewmodel.SessionViewModel";

      @KeepFieldType
      EmployeeManagementViewModel com_duka_employee_management_EmployeeManagementViewModel2;

      @KeepFieldType
      SettingsViewModel com_duka_settings_shared_SettingsViewModel2;

      @KeepFieldType
      SessionViewModel com_duka_app_viewmodel_SessionViewModel2;
    }
  }

  private static final class ViewModelCImpl extends DukaApplication_HiltComponents.ViewModelC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<EmployeeManagementViewModel> employeeManagementViewModelProvider;

    private Provider<SessionViewModel> sessionViewModelProvider;

    private Provider<SettingsViewModel> settingsViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;

      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.employeeManagementViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.sessionViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.settingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(3).put(LazyClassKeyProvider.com_duka_employee_management_EmployeeManagementViewModel, ((Provider) employeeManagementViewModelProvider)).put(LazyClassKeyProvider.com_duka_app_viewmodel_SessionViewModel, ((Provider) sessionViewModelProvider)).put(LazyClassKeyProvider.com_duka_settings_shared_SettingsViewModel, ((Provider) settingsViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return Collections.<Class<?>, Object>emptyMap();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_duka_app_viewmodel_SessionViewModel = "com.duka.app.viewmodel.SessionViewModel";

      static String com_duka_settings_shared_SettingsViewModel = "com.duka.settings.shared.SettingsViewModel";

      static String com_duka_employee_management_EmployeeManagementViewModel = "com.duka.employee.management.EmployeeManagementViewModel";

      @KeepFieldType
      SessionViewModel com_duka_app_viewmodel_SessionViewModel2;

      @KeepFieldType
      SettingsViewModel com_duka_settings_shared_SettingsViewModel2;

      @KeepFieldType
      EmployeeManagementViewModel com_duka_employee_management_EmployeeManagementViewModel2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.duka.employee.management.EmployeeManagementViewModel 
          return (T) new EmployeeManagementViewModel(singletonCImpl.employeeRepositoryProvider.get(), singletonCImpl.userRepositoryProvider.get(), singletonCImpl.businessRepositoryProvider.get(), singletonCImpl.provideSessionManagerProvider.get());

          case 1: // com.duka.app.viewmodel.SessionViewModel 
          return (T) new SessionViewModel(singletonCImpl.provideSessionManagerProvider.get(), singletonCImpl.userRepositoryProvider.get(), singletonCImpl.businessRepositoryProvider.get(), singletonCImpl.employeeRepositoryProvider.get());

          case 2: // com.duka.settings.shared.SettingsViewModel 
          return (T) new SettingsViewModel(ApplicationContextModule_ProvideApplicationFactory.provideApplication(singletonCImpl.applicationContextModule), singletonCImpl.provideSessionManagerProvider.get(), singletonCImpl.userRepositoryProvider.get(), singletonCImpl.businessRepositoryProvider.get(), singletonCImpl.chatRepositoryProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends DukaApplication_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends DukaApplication_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends DukaApplication_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<DukaDatabase> provideDatabaseProvider;

    private Provider<DukaInitializer> dukaInitializerProvider;

    private Provider<DataStore<Preferences>> provideDataStoreProvider;

    private Provider<SessionManager> provideSessionManagerProvider;

    private Provider<BusinessRepository> businessRepositoryProvider;

    private Provider<ProductRepository> productRepositoryProvider;

    private Provider<SaleRepository> saleRepositoryProvider;

    private Provider<EmployeeRepository> employeeRepositoryProvider;

    private Provider<ChatRepository> chatRepositoryProvider;

    private Provider<PromoRepository> promoRepositoryProvider;

    private Provider<IssueRepository> issueRepositoryProvider;

    private Provider<EbmRepository> ebmRepositoryProvider;

    private Provider<TaxRepository> taxRepositoryProvider;

    private Provider<MockEbmGateway> mockEbmGatewayProvider;

    private Provider<RegexVoiceEntryParser> regexVoiceEntryParserProvider;

    private Provider<PurchaseRepository> purchaseRepositoryProvider;

    private Provider<BudgetGoalRepository> budgetGoalRepositoryProvider;

    private Provider<FeedbackReportRepository> feedbackReportRepositoryProvider;

    private Provider<ShopRatingRepository> shopRatingRepositoryProvider;

    private Provider<WholesalerRepository> wholesalerRepositoryProvider;

    private Provider<RestockRequestRepository> restockRequestRepositoryProvider;

    private Provider<ProductAlertRepository> productAlertRepositoryProvider;

    private Provider<AppNotificationRepository> appNotificationRepositoryProvider;

    private Provider<StockAdjustmentRepository> stockAdjustmentRepositoryProvider;

    private Provider<ExpenseRepository> expenseRepositoryProvider;

    private Provider<UserRepository> userRepositoryProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private BusinessDao businessDao() {
      return DatabaseModule_ProvideBusinessDaoFactory.provideBusinessDao(provideDatabaseProvider.get());
    }

    private ProductDao productDao() {
      return DatabaseModule_ProvideProductDaoFactory.provideProductDao(provideDatabaseProvider.get());
    }

    private SaleDao saleDao() {
      return DatabaseModule_ProvideSaleDaoFactory.provideSaleDao(provideDatabaseProvider.get());
    }

    private EmployeeDao employeeDao() {
      return DatabaseModule_ProvideEmployeeDaoFactory.provideEmployeeDao(provideDatabaseProvider.get());
    }

    private ChatMessageDao chatMessageDao() {
      return DatabaseModule_ProvideChatMessageDaoFactory.provideChatMessageDao(provideDatabaseProvider.get());
    }

    private UserDao userDao() {
      return DatabaseModule_ProvideUserDaoFactory.provideUserDao(provideDatabaseProvider.get());
    }

    private PurchaseDao purchaseDao() {
      return DatabaseModule_ProvidePurchaseDaoFactory.providePurchaseDao(provideDatabaseProvider.get());
    }

    private BudgetGoalDao budgetGoalDao() {
      return DatabaseModule_ProvideBudgetGoalDaoFactory.provideBudgetGoalDao(provideDatabaseProvider.get());
    }

    private FeedbackReportDao feedbackReportDao() {
      return DatabaseModule_ProvideFeedbackReportDaoFactory.provideFeedbackReportDao(provideDatabaseProvider.get());
    }

    private ShopRatingDao shopRatingDao() {
      return DatabaseModule_ProvideShopRatingDaoFactory.provideShopRatingDao(provideDatabaseProvider.get());
    }

    private WholesalerDao wholesalerDao() {
      return DatabaseModule_ProvideWholesalerDaoFactory.provideWholesalerDao(provideDatabaseProvider.get());
    }

    private PromoDao promoDao() {
      return DatabaseModule_ProvidePromoDaoFactory.providePromoDao(provideDatabaseProvider.get());
    }

    private IssueReportDao issueReportDao() {
      return DatabaseModule_ProvideIssueReportDaoFactory.provideIssueReportDao(provideDatabaseProvider.get());
    }

    private EbmReceiptDao ebmReceiptDao() {
      return DatabaseModule_ProvideEbmReceiptDaoFactory.provideEbmReceiptDao(provideDatabaseProvider.get());
    }

    private TaxProfileDao taxProfileDao() {
      return DatabaseModule_ProvideTaxProfileDaoFactory.provideTaxProfileDao(provideDatabaseProvider.get());
    }

    private RestockRequestDao restockRequestDao() {
      return DatabaseModule_ProvideRestockRequestDaoFactory.provideRestockRequestDao(provideDatabaseProvider.get());
    }

    private ProductAlertDao productAlertDao() {
      return DatabaseModule_ProvideProductAlertDaoFactory.provideProductAlertDao(provideDatabaseProvider.get());
    }

    private AppNotificationDao appNotificationDao() {
      return DatabaseModule_ProvideAppNotificationDaoFactory.provideAppNotificationDao(provideDatabaseProvider.get());
    }

    private StockAdjustmentDao stockAdjustmentDao() {
      return DatabaseModule_ProvideStockAdjustmentDaoFactory.provideStockAdjustmentDao(provideDatabaseProvider.get());
    }

    private ExpenseDao expenseDao() {
      return DatabaseModule_ProvideExpenseDaoFactory.provideExpenseDao(provideDatabaseProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<DukaDatabase>(singletonCImpl, 1));
      this.dukaInitializerProvider = DoubleCheck.provider(new SwitchingProvider<DukaInitializer>(singletonCImpl, 0));
      this.provideDataStoreProvider = DoubleCheck.provider(new SwitchingProvider<DataStore<Preferences>>(singletonCImpl, 3));
      this.provideSessionManagerProvider = DoubleCheck.provider(new SwitchingProvider<SessionManager>(singletonCImpl, 2));
      this.businessRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<BusinessRepository>(singletonCImpl, 4));
      this.productRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<ProductRepository>(singletonCImpl, 5));
      this.saleRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<SaleRepository>(singletonCImpl, 6));
      this.employeeRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<EmployeeRepository>(singletonCImpl, 7));
      this.chatRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<ChatRepository>(singletonCImpl, 8));
      this.promoRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<PromoRepository>(singletonCImpl, 9));
      this.issueRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<IssueRepository>(singletonCImpl, 10));
      this.ebmRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<EbmRepository>(singletonCImpl, 11));
      this.taxRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<TaxRepository>(singletonCImpl, 12));
      this.mockEbmGatewayProvider = DoubleCheck.provider(new SwitchingProvider<MockEbmGateway>(singletonCImpl, 13));
      this.regexVoiceEntryParserProvider = DoubleCheck.provider(new SwitchingProvider<RegexVoiceEntryParser>(singletonCImpl, 14));
      this.purchaseRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<PurchaseRepository>(singletonCImpl, 15));
      this.budgetGoalRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<BudgetGoalRepository>(singletonCImpl, 16));
      this.feedbackReportRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<FeedbackReportRepository>(singletonCImpl, 17));
      this.shopRatingRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<ShopRatingRepository>(singletonCImpl, 18));
      this.wholesalerRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<WholesalerRepository>(singletonCImpl, 19));
      this.restockRequestRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<RestockRequestRepository>(singletonCImpl, 20));
      this.productAlertRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<ProductAlertRepository>(singletonCImpl, 21));
      this.appNotificationRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<AppNotificationRepository>(singletonCImpl, 22));
      this.stockAdjustmentRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<StockAdjustmentRepository>(singletonCImpl, 23));
      this.expenseRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<ExpenseRepository>(singletonCImpl, 24));
      this.userRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<UserRepository>(singletonCImpl, 25));
    }

    @Override
    public void injectDukaApplication(DukaApplication dukaApplication) {
      injectDukaApplication2(dukaApplication);
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private DukaApplication injectDukaApplication2(DukaApplication instance) {
      DukaApplication_MembersInjector.injectInitializer(instance, dukaInitializerProvider.get());
      DukaApplication_MembersInjector.injectSessionManager(instance, provideSessionManagerProvider.get());
      return instance;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.duka.app.DukaInitializer 
          return (T) new DukaInitializer(singletonCImpl.businessDao(), singletonCImpl.productDao(), singletonCImpl.saleDao(), singletonCImpl.employeeDao(), singletonCImpl.chatMessageDao(), singletonCImpl.userDao(), singletonCImpl.purchaseDao(), singletonCImpl.budgetGoalDao(), singletonCImpl.feedbackReportDao(), singletonCImpl.shopRatingDao(), singletonCImpl.wholesalerDao());

          case 1: // com.duka.app.data.local.DukaDatabase 
          return (T) DatabaseModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 2: // com.duka.app.data.session.SessionManager 
          return (T) SessionModule_ProvideSessionManagerFactory.provideSessionManager(singletonCImpl.provideDataStoreProvider.get(), singletonCImpl.userDao());

          case 3: // androidx.datastore.core.DataStore<androidx.datastore.preferences.core.Preferences> 
          return (T) SessionModule_ProvideDataStoreFactory.provideDataStore(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 4: // com.duka.app.data.repository.BusinessRepository 
          return (T) new BusinessRepository(singletonCImpl.businessDao());

          case 5: // com.duka.app.data.repository.ProductRepository 
          return (T) new ProductRepository(singletonCImpl.productDao());

          case 6: // com.duka.app.data.repository.SaleRepository 
          return (T) new SaleRepository(singletonCImpl.saleDao());

          case 7: // com.duka.app.data.repository.EmployeeRepository 
          return (T) new EmployeeRepository(singletonCImpl.employeeDao());

          case 8: // com.duka.app.data.repository.ChatRepository 
          return (T) new ChatRepository(singletonCImpl.chatMessageDao());

          case 9: // com.duka.app.data.repository.PromoRepository 
          return (T) new PromoRepository(singletonCImpl.promoDao());

          case 10: // com.duka.app.data.repository.IssueRepository 
          return (T) new IssueRepository(singletonCImpl.issueReportDao());

          case 11: // com.duka.app.data.repository.EbmRepository 
          return (T) new EbmRepository(singletonCImpl.ebmReceiptDao());

          case 12: // com.duka.app.data.repository.TaxRepository 
          return (T) new TaxRepository(singletonCImpl.taxProfileDao());

          case 13: // com.duka.app.domain.MockEbmGateway 
          return (T) new MockEbmGateway();

          case 14: // com.duka.app.domain.RegexVoiceEntryParser 
          return (T) new RegexVoiceEntryParser();

          case 15: // com.duka.app.data.repository.PurchaseRepository 
          return (T) new PurchaseRepository(singletonCImpl.purchaseDao());

          case 16: // com.duka.app.data.repository.BudgetGoalRepository 
          return (T) new BudgetGoalRepository(singletonCImpl.budgetGoalDao());

          case 17: // com.duka.app.data.repository.FeedbackReportRepository 
          return (T) new FeedbackReportRepository(singletonCImpl.feedbackReportDao());

          case 18: // com.duka.app.data.repository.ShopRatingRepository 
          return (T) new ShopRatingRepository(singletonCImpl.shopRatingDao());

          case 19: // com.duka.app.data.repository.WholesalerRepository 
          return (T) new WholesalerRepository(singletonCImpl.wholesalerDao());

          case 20: // com.duka.app.data.repository.RestockRequestRepository 
          return (T) new RestockRequestRepository(singletonCImpl.restockRequestDao());

          case 21: // com.duka.app.data.repository.ProductAlertRepository 
          return (T) new ProductAlertRepository(singletonCImpl.productAlertDao());

          case 22: // com.duka.app.data.repository.AppNotificationRepository 
          return (T) new AppNotificationRepository(singletonCImpl.appNotificationDao());

          case 23: // com.duka.app.data.repository.StockAdjustmentRepository 
          return (T) new StockAdjustmentRepository(singletonCImpl.stockAdjustmentDao());

          case 24: // com.duka.app.data.repository.ExpenseRepository 
          return (T) new ExpenseRepository(singletonCImpl.expenseDao());

          case 25: // com.duka.app.data.repository.UserRepository 
          return (T) new UserRepository(singletonCImpl.userDao());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
