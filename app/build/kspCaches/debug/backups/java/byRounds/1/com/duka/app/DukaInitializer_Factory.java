package com.duka.app;

import com.duka.app.data.local.dao.BudgetGoalDao;
import com.duka.app.data.local.dao.BusinessDao;
import com.duka.app.data.local.dao.ChatMessageDao;
import com.duka.app.data.local.dao.EmployeeDao;
import com.duka.app.data.local.dao.FeedbackReportDao;
import com.duka.app.data.local.dao.ProductDao;
import com.duka.app.data.local.dao.PurchaseDao;
import com.duka.app.data.local.dao.SaleDao;
import com.duka.app.data.local.dao.ShopRatingDao;
import com.duka.app.data.local.dao.UserDao;
import com.duka.app.data.local.dao.WholesalerDao;
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
public final class DukaInitializer_Factory implements Factory<DukaInitializer> {
  private final Provider<BusinessDao> businessDaoProvider;

  private final Provider<ProductDao> productDaoProvider;

  private final Provider<SaleDao> saleDaoProvider;

  private final Provider<EmployeeDao> employeeDaoProvider;

  private final Provider<ChatMessageDao> chatMessageDaoProvider;

  private final Provider<UserDao> userDaoProvider;

  private final Provider<PurchaseDao> purchaseDaoProvider;

  private final Provider<BudgetGoalDao> budgetGoalDaoProvider;

  private final Provider<FeedbackReportDao> feedbackReportDaoProvider;

  private final Provider<ShopRatingDao> shopRatingDaoProvider;

  private final Provider<WholesalerDao> wholesalerDaoProvider;

  public DukaInitializer_Factory(Provider<BusinessDao> businessDaoProvider,
      Provider<ProductDao> productDaoProvider, Provider<SaleDao> saleDaoProvider,
      Provider<EmployeeDao> employeeDaoProvider, Provider<ChatMessageDao> chatMessageDaoProvider,
      Provider<UserDao> userDaoProvider, Provider<PurchaseDao> purchaseDaoProvider,
      Provider<BudgetGoalDao> budgetGoalDaoProvider,
      Provider<FeedbackReportDao> feedbackReportDaoProvider,
      Provider<ShopRatingDao> shopRatingDaoProvider,
      Provider<WholesalerDao> wholesalerDaoProvider) {
    this.businessDaoProvider = businessDaoProvider;
    this.productDaoProvider = productDaoProvider;
    this.saleDaoProvider = saleDaoProvider;
    this.employeeDaoProvider = employeeDaoProvider;
    this.chatMessageDaoProvider = chatMessageDaoProvider;
    this.userDaoProvider = userDaoProvider;
    this.purchaseDaoProvider = purchaseDaoProvider;
    this.budgetGoalDaoProvider = budgetGoalDaoProvider;
    this.feedbackReportDaoProvider = feedbackReportDaoProvider;
    this.shopRatingDaoProvider = shopRatingDaoProvider;
    this.wholesalerDaoProvider = wholesalerDaoProvider;
  }

  @Override
  public DukaInitializer get() {
    return newInstance(businessDaoProvider.get(), productDaoProvider.get(), saleDaoProvider.get(), employeeDaoProvider.get(), chatMessageDaoProvider.get(), userDaoProvider.get(), purchaseDaoProvider.get(), budgetGoalDaoProvider.get(), feedbackReportDaoProvider.get(), shopRatingDaoProvider.get(), wholesalerDaoProvider.get());
  }

  public static DukaInitializer_Factory create(Provider<BusinessDao> businessDaoProvider,
      Provider<ProductDao> productDaoProvider, Provider<SaleDao> saleDaoProvider,
      Provider<EmployeeDao> employeeDaoProvider, Provider<ChatMessageDao> chatMessageDaoProvider,
      Provider<UserDao> userDaoProvider, Provider<PurchaseDao> purchaseDaoProvider,
      Provider<BudgetGoalDao> budgetGoalDaoProvider,
      Provider<FeedbackReportDao> feedbackReportDaoProvider,
      Provider<ShopRatingDao> shopRatingDaoProvider,
      Provider<WholesalerDao> wholesalerDaoProvider) {
    return new DukaInitializer_Factory(businessDaoProvider, productDaoProvider, saleDaoProvider, employeeDaoProvider, chatMessageDaoProvider, userDaoProvider, purchaseDaoProvider, budgetGoalDaoProvider, feedbackReportDaoProvider, shopRatingDaoProvider, wholesalerDaoProvider);
  }

  public static DukaInitializer newInstance(BusinessDao businessDao, ProductDao productDao,
      SaleDao saleDao, EmployeeDao employeeDao, ChatMessageDao chatMessageDao, UserDao userDao,
      PurchaseDao purchaseDao, BudgetGoalDao budgetGoalDao, FeedbackReportDao feedbackReportDao,
      ShopRatingDao shopRatingDao, WholesalerDao wholesalerDao) {
    return new DukaInitializer(businessDao, productDao, saleDao, employeeDao, chatMessageDao, userDao, purchaseDao, budgetGoalDao, feedbackReportDao, shopRatingDao, wholesalerDao);
  }
}
