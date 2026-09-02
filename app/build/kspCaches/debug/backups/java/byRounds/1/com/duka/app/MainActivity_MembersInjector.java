package com.duka.app;

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
import com.duka.app.data.repository.WholesalerRepository;
import com.duka.app.data.session.SessionManager;
import com.duka.app.domain.EbmGateway;
import com.duka.app.domain.VoiceEntryParser;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class MainActivity_MembersInjector implements MembersInjector<MainActivity> {
  private final Provider<BusinessRepository> businessRepositoryProvider;

  private final Provider<ProductRepository> productRepositoryProvider;

  private final Provider<SaleRepository> saleRepositoryProvider;

  private final Provider<EmployeeRepository> employeeRepositoryProvider;

  private final Provider<ChatRepository> chatRepositoryProvider;

  private final Provider<PromoRepository> promoRepositoryProvider;

  private final Provider<IssueRepository> issueRepositoryProvider;

  private final Provider<EbmRepository> ebmRepositoryProvider;

  private final Provider<TaxRepository> taxRepositoryProvider;

  private final Provider<EbmGateway> ebmGatewayProvider;

  private final Provider<VoiceEntryParser> voiceEntryParserProvider;

  private final Provider<PurchaseRepository> purchaseRepositoryProvider;

  private final Provider<BudgetGoalRepository> budgetGoalRepositoryProvider;

  private final Provider<FeedbackReportRepository> feedbackReportRepositoryProvider;

  private final Provider<ShopRatingRepository> shopRatingRepositoryProvider;

  private final Provider<WholesalerRepository> wholesalerRepositoryProvider;

  private final Provider<RestockRequestRepository> restockRequestRepositoryProvider;

  private final Provider<SessionManager> sessionManagerProvider;

  private final Provider<ProductAlertRepository> productAlertRepositoryProvider;

  private final Provider<AppNotificationRepository> appNotificationRepositoryProvider;

  private final Provider<StockAdjustmentRepository> stockAdjustmentRepositoryProvider;

  private final Provider<ExpenseRepository> expenseRepositoryProvider;

  public MainActivity_MembersInjector(Provider<BusinessRepository> businessRepositoryProvider,
      Provider<ProductRepository> productRepositoryProvider,
      Provider<SaleRepository> saleRepositoryProvider,
      Provider<EmployeeRepository> employeeRepositoryProvider,
      Provider<ChatRepository> chatRepositoryProvider,
      Provider<PromoRepository> promoRepositoryProvider,
      Provider<IssueRepository> issueRepositoryProvider,
      Provider<EbmRepository> ebmRepositoryProvider, Provider<TaxRepository> taxRepositoryProvider,
      Provider<EbmGateway> ebmGatewayProvider, Provider<VoiceEntryParser> voiceEntryParserProvider,
      Provider<PurchaseRepository> purchaseRepositoryProvider,
      Provider<BudgetGoalRepository> budgetGoalRepositoryProvider,
      Provider<FeedbackReportRepository> feedbackReportRepositoryProvider,
      Provider<ShopRatingRepository> shopRatingRepositoryProvider,
      Provider<WholesalerRepository> wholesalerRepositoryProvider,
      Provider<RestockRequestRepository> restockRequestRepositoryProvider,
      Provider<SessionManager> sessionManagerProvider,
      Provider<ProductAlertRepository> productAlertRepositoryProvider,
      Provider<AppNotificationRepository> appNotificationRepositoryProvider,
      Provider<StockAdjustmentRepository> stockAdjustmentRepositoryProvider,
      Provider<ExpenseRepository> expenseRepositoryProvider) {
    this.businessRepositoryProvider = businessRepositoryProvider;
    this.productRepositoryProvider = productRepositoryProvider;
    this.saleRepositoryProvider = saleRepositoryProvider;
    this.employeeRepositoryProvider = employeeRepositoryProvider;
    this.chatRepositoryProvider = chatRepositoryProvider;
    this.promoRepositoryProvider = promoRepositoryProvider;
    this.issueRepositoryProvider = issueRepositoryProvider;
    this.ebmRepositoryProvider = ebmRepositoryProvider;
    this.taxRepositoryProvider = taxRepositoryProvider;
    this.ebmGatewayProvider = ebmGatewayProvider;
    this.voiceEntryParserProvider = voiceEntryParserProvider;
    this.purchaseRepositoryProvider = purchaseRepositoryProvider;
    this.budgetGoalRepositoryProvider = budgetGoalRepositoryProvider;
    this.feedbackReportRepositoryProvider = feedbackReportRepositoryProvider;
    this.shopRatingRepositoryProvider = shopRatingRepositoryProvider;
    this.wholesalerRepositoryProvider = wholesalerRepositoryProvider;
    this.restockRequestRepositoryProvider = restockRequestRepositoryProvider;
    this.sessionManagerProvider = sessionManagerProvider;
    this.productAlertRepositoryProvider = productAlertRepositoryProvider;
    this.appNotificationRepositoryProvider = appNotificationRepositoryProvider;
    this.stockAdjustmentRepositoryProvider = stockAdjustmentRepositoryProvider;
    this.expenseRepositoryProvider = expenseRepositoryProvider;
  }

  public static MembersInjector<MainActivity> create(
      Provider<BusinessRepository> businessRepositoryProvider,
      Provider<ProductRepository> productRepositoryProvider,
      Provider<SaleRepository> saleRepositoryProvider,
      Provider<EmployeeRepository> employeeRepositoryProvider,
      Provider<ChatRepository> chatRepositoryProvider,
      Provider<PromoRepository> promoRepositoryProvider,
      Provider<IssueRepository> issueRepositoryProvider,
      Provider<EbmRepository> ebmRepositoryProvider, Provider<TaxRepository> taxRepositoryProvider,
      Provider<EbmGateway> ebmGatewayProvider, Provider<VoiceEntryParser> voiceEntryParserProvider,
      Provider<PurchaseRepository> purchaseRepositoryProvider,
      Provider<BudgetGoalRepository> budgetGoalRepositoryProvider,
      Provider<FeedbackReportRepository> feedbackReportRepositoryProvider,
      Provider<ShopRatingRepository> shopRatingRepositoryProvider,
      Provider<WholesalerRepository> wholesalerRepositoryProvider,
      Provider<RestockRequestRepository> restockRequestRepositoryProvider,
      Provider<SessionManager> sessionManagerProvider,
      Provider<ProductAlertRepository> productAlertRepositoryProvider,
      Provider<AppNotificationRepository> appNotificationRepositoryProvider,
      Provider<StockAdjustmentRepository> stockAdjustmentRepositoryProvider,
      Provider<ExpenseRepository> expenseRepositoryProvider) {
    return new MainActivity_MembersInjector(businessRepositoryProvider, productRepositoryProvider, saleRepositoryProvider, employeeRepositoryProvider, chatRepositoryProvider, promoRepositoryProvider, issueRepositoryProvider, ebmRepositoryProvider, taxRepositoryProvider, ebmGatewayProvider, voiceEntryParserProvider, purchaseRepositoryProvider, budgetGoalRepositoryProvider, feedbackReportRepositoryProvider, shopRatingRepositoryProvider, wholesalerRepositoryProvider, restockRequestRepositoryProvider, sessionManagerProvider, productAlertRepositoryProvider, appNotificationRepositoryProvider, stockAdjustmentRepositoryProvider, expenseRepositoryProvider);
  }

  @Override
  public void injectMembers(MainActivity instance) {
    injectBusinessRepository(instance, businessRepositoryProvider.get());
    injectProductRepository(instance, productRepositoryProvider.get());
    injectSaleRepository(instance, saleRepositoryProvider.get());
    injectEmployeeRepository(instance, employeeRepositoryProvider.get());
    injectChatRepository(instance, chatRepositoryProvider.get());
    injectPromoRepository(instance, promoRepositoryProvider.get());
    injectIssueRepository(instance, issueRepositoryProvider.get());
    injectEbmRepository(instance, ebmRepositoryProvider.get());
    injectTaxRepository(instance, taxRepositoryProvider.get());
    injectEbmGateway(instance, ebmGatewayProvider.get());
    injectVoiceEntryParser(instance, voiceEntryParserProvider.get());
    injectPurchaseRepository(instance, purchaseRepositoryProvider.get());
    injectBudgetGoalRepository(instance, budgetGoalRepositoryProvider.get());
    injectFeedbackReportRepository(instance, feedbackReportRepositoryProvider.get());
    injectShopRatingRepository(instance, shopRatingRepositoryProvider.get());
    injectWholesalerRepository(instance, wholesalerRepositoryProvider.get());
    injectRestockRequestRepository(instance, restockRequestRepositoryProvider.get());
    injectSessionManager(instance, sessionManagerProvider.get());
    injectProductAlertRepository(instance, productAlertRepositoryProvider.get());
    injectAppNotificationRepository(instance, appNotificationRepositoryProvider.get());
    injectStockAdjustmentRepository(instance, stockAdjustmentRepositoryProvider.get());
    injectExpenseRepository(instance, expenseRepositoryProvider.get());
  }

  @InjectedFieldSignature("com.duka.app.MainActivity.businessRepository")
  public static void injectBusinessRepository(MainActivity instance,
      BusinessRepository businessRepository) {
    instance.businessRepository = businessRepository;
  }

  @InjectedFieldSignature("com.duka.app.MainActivity.productRepository")
  public static void injectProductRepository(MainActivity instance,
      ProductRepository productRepository) {
    instance.productRepository = productRepository;
  }

  @InjectedFieldSignature("com.duka.app.MainActivity.saleRepository")
  public static void injectSaleRepository(MainActivity instance, SaleRepository saleRepository) {
    instance.saleRepository = saleRepository;
  }

  @InjectedFieldSignature("com.duka.app.MainActivity.employeeRepository")
  public static void injectEmployeeRepository(MainActivity instance,
      EmployeeRepository employeeRepository) {
    instance.employeeRepository = employeeRepository;
  }

  @InjectedFieldSignature("com.duka.app.MainActivity.chatRepository")
  public static void injectChatRepository(MainActivity instance, ChatRepository chatRepository) {
    instance.chatRepository = chatRepository;
  }

  @InjectedFieldSignature("com.duka.app.MainActivity.promoRepository")
  public static void injectPromoRepository(MainActivity instance, PromoRepository promoRepository) {
    instance.promoRepository = promoRepository;
  }

  @InjectedFieldSignature("com.duka.app.MainActivity.issueRepository")
  public static void injectIssueRepository(MainActivity instance, IssueRepository issueRepository) {
    instance.issueRepository = issueRepository;
  }

  @InjectedFieldSignature("com.duka.app.MainActivity.ebmRepository")
  public static void injectEbmRepository(MainActivity instance, EbmRepository ebmRepository) {
    instance.ebmRepository = ebmRepository;
  }

  @InjectedFieldSignature("com.duka.app.MainActivity.taxRepository")
  public static void injectTaxRepository(MainActivity instance, TaxRepository taxRepository) {
    instance.taxRepository = taxRepository;
  }

  @InjectedFieldSignature("com.duka.app.MainActivity.ebmGateway")
  public static void injectEbmGateway(MainActivity instance, EbmGateway ebmGateway) {
    instance.ebmGateway = ebmGateway;
  }

  @InjectedFieldSignature("com.duka.app.MainActivity.voiceEntryParser")
  public static void injectVoiceEntryParser(MainActivity instance,
      VoiceEntryParser voiceEntryParser) {
    instance.voiceEntryParser = voiceEntryParser;
  }

  @InjectedFieldSignature("com.duka.app.MainActivity.purchaseRepository")
  public static void injectPurchaseRepository(MainActivity instance,
      PurchaseRepository purchaseRepository) {
    instance.purchaseRepository = purchaseRepository;
  }

  @InjectedFieldSignature("com.duka.app.MainActivity.budgetGoalRepository")
  public static void injectBudgetGoalRepository(MainActivity instance,
      BudgetGoalRepository budgetGoalRepository) {
    instance.budgetGoalRepository = budgetGoalRepository;
  }

  @InjectedFieldSignature("com.duka.app.MainActivity.feedbackReportRepository")
  public static void injectFeedbackReportRepository(MainActivity instance,
      FeedbackReportRepository feedbackReportRepository) {
    instance.feedbackReportRepository = feedbackReportRepository;
  }

  @InjectedFieldSignature("com.duka.app.MainActivity.shopRatingRepository")
  public static void injectShopRatingRepository(MainActivity instance,
      ShopRatingRepository shopRatingRepository) {
    instance.shopRatingRepository = shopRatingRepository;
  }

  @InjectedFieldSignature("com.duka.app.MainActivity.wholesalerRepository")
  public static void injectWholesalerRepository(MainActivity instance,
      WholesalerRepository wholesalerRepository) {
    instance.wholesalerRepository = wholesalerRepository;
  }

  @InjectedFieldSignature("com.duka.app.MainActivity.restockRequestRepository")
  public static void injectRestockRequestRepository(MainActivity instance,
      RestockRequestRepository restockRequestRepository) {
    instance.restockRequestRepository = restockRequestRepository;
  }

  @InjectedFieldSignature("com.duka.app.MainActivity.sessionManager")
  public static void injectSessionManager(MainActivity instance, SessionManager sessionManager) {
    instance.sessionManager = sessionManager;
  }

  @InjectedFieldSignature("com.duka.app.MainActivity.productAlertRepository")
  public static void injectProductAlertRepository(MainActivity instance,
      ProductAlertRepository productAlertRepository) {
    instance.productAlertRepository = productAlertRepository;
  }

  @InjectedFieldSignature("com.duka.app.MainActivity.appNotificationRepository")
  public static void injectAppNotificationRepository(MainActivity instance,
      AppNotificationRepository appNotificationRepository) {
    instance.appNotificationRepository = appNotificationRepository;
  }

  @InjectedFieldSignature("com.duka.app.MainActivity.stockAdjustmentRepository")
  public static void injectStockAdjustmentRepository(MainActivity instance,
      StockAdjustmentRepository stockAdjustmentRepository) {
    instance.stockAdjustmentRepository = stockAdjustmentRepository;
  }

  @InjectedFieldSignature("com.duka.app.MainActivity.expenseRepository")
  public static void injectExpenseRepository(MainActivity instance,
      ExpenseRepository expenseRepository) {
    instance.expenseRepository = expenseRepository;
  }
}
