package com.duka.app.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.duka.app.data.local.dao.AppNotificationDao;
import com.duka.app.data.local.dao.AppNotificationDao_Impl;
import com.duka.app.data.local.dao.BudgetGoalDao;
import com.duka.app.data.local.dao.BudgetGoalDao_Impl;
import com.duka.app.data.local.dao.BusinessDao;
import com.duka.app.data.local.dao.BusinessDao_Impl;
import com.duka.app.data.local.dao.ChatMessageDao;
import com.duka.app.data.local.dao.ChatMessageDao_Impl;
import com.duka.app.data.local.dao.EbmReceiptDao;
import com.duka.app.data.local.dao.EbmReceiptDao_Impl;
import com.duka.app.data.local.dao.EmployeeDao;
import com.duka.app.data.local.dao.EmployeeDao_Impl;
import com.duka.app.data.local.dao.ExpenseDao;
import com.duka.app.data.local.dao.ExpenseDao_Impl;
import com.duka.app.data.local.dao.FeedbackReportDao;
import com.duka.app.data.local.dao.FeedbackReportDao_Impl;
import com.duka.app.data.local.dao.IssueReportDao;
import com.duka.app.data.local.dao.IssueReportDao_Impl;
import com.duka.app.data.local.dao.ProductAlertDao;
import com.duka.app.data.local.dao.ProductAlertDao_Impl;
import com.duka.app.data.local.dao.ProductDao;
import com.duka.app.data.local.dao.ProductDao_Impl;
import com.duka.app.data.local.dao.PromoDao;
import com.duka.app.data.local.dao.PromoDao_Impl;
import com.duka.app.data.local.dao.PurchaseDao;
import com.duka.app.data.local.dao.PurchaseDao_Impl;
import com.duka.app.data.local.dao.RestockRequestDao;
import com.duka.app.data.local.dao.RestockRequestDao_Impl;
import com.duka.app.data.local.dao.SaleDao;
import com.duka.app.data.local.dao.SaleDao_Impl;
import com.duka.app.data.local.dao.ShopRatingDao;
import com.duka.app.data.local.dao.ShopRatingDao_Impl;
import com.duka.app.data.local.dao.StockAdjustmentDao;
import com.duka.app.data.local.dao.StockAdjustmentDao_Impl;
import com.duka.app.data.local.dao.TaxProfileDao;
import com.duka.app.data.local.dao.TaxProfileDao_Impl;
import com.duka.app.data.local.dao.UserDao;
import com.duka.app.data.local.dao.UserDao_Impl;
import com.duka.app.data.local.dao.WholesalerDao;
import com.duka.app.data.local.dao.WholesalerDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class DukaDatabase_Impl extends DukaDatabase {
  private volatile BusinessDao _businessDao;

  private volatile ProductDao _productDao;

  private volatile SaleDao _saleDao;

  private volatile EmployeeDao _employeeDao;

  private volatile ChatMessageDao _chatMessageDao;

  private volatile PromoDao _promoDao;

  private volatile IssueReportDao _issueReportDao;

  private volatile EbmReceiptDao _ebmReceiptDao;

  private volatile TaxProfileDao _taxProfileDao;

  private volatile UserDao _userDao;

  private volatile PurchaseDao _purchaseDao;

  private volatile BudgetGoalDao _budgetGoalDao;

  private volatile FeedbackReportDao _feedbackReportDao;

  private volatile ShopRatingDao _shopRatingDao;

  private volatile WholesalerDao _wholesalerDao;

  private volatile RestockRequestDao _restockRequestDao;

  private volatile StockAdjustmentDao _stockAdjustmentDao;

  private volatile ProductAlertDao _productAlertDao;

  private volatile AppNotificationDao _appNotificationDao;

  private volatile ExpenseDao _expenseDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(6) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `businesses` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `type` TEXT NOT NULL, `employeeCount` INTEGER NOT NULL, `language` TEXT NOT NULL, `district` TEXT NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `products` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `businessId` INTEGER NOT NULL, `name` TEXT NOT NULL, `price` REAL NOT NULL, `stockQuantity` INTEGER NOT NULL, `category` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, `isDeleted` INTEGER NOT NULL, `costPrice` INTEGER NOT NULL, `lowStockThreshold` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `sales` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `productId` INTEGER NOT NULL, `businessId` INTEGER NOT NULL, `amount` REAL NOT NULL, `timestamp` INTEGER NOT NULL, `source` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `employees` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `businessId` INTEGER NOT NULL, `userId` INTEGER NOT NULL, `code` TEXT NOT NULL, `name` TEXT NOT NULL, `role` TEXT NOT NULL, `isActive` INTEGER NOT NULL, `joinedAt` INTEGER NOT NULL, `lastSeenAt` INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `chat_messages` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `businessId` INTEGER NOT NULL, `senderRole` TEXT NOT NULL, `senderLabel` TEXT NOT NULL, `text` TEXT NOT NULL, `timestamp` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `promos` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `businessId` INTEGER NOT NULL, `title` TEXT NOT NULL, `discountPercent` INTEGER NOT NULL, `startDate` INTEGER NOT NULL, `endDate` INTEGER NOT NULL, `isLive` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `issue_reports` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `businessId` INTEGER NOT NULL, `category` TEXT NOT NULL, `message` TEXT NOT NULL, `attachBusinessId` INTEGER NOT NULL, `status` TEXT NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `ebm_receipts` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `saleId` INTEGER NOT NULL, `receiptNumber` TEXT NOT NULL, `status` TEXT NOT NULL, `sentAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `tax_profiles` (`businessId` INTEGER NOT NULL, `quarterlyTurnover` REAL NOT NULL, `tier` TEXT NOT NULL, `vatCollected` REAL NOT NULL, PRIMARY KEY(`businessId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `users` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `businessId` INTEGER, `fullName` TEXT NOT NULL, `phoneOrEmail` TEXT NOT NULL, `passwordHash` TEXT NOT NULL, `role` TEXT NOT NULL, `isDeleted` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `purchases` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `clientUserId` INTEGER NOT NULL, `businessId` INTEGER NOT NULL, `productId` INTEGER NOT NULL, `amount` REAL NOT NULL, `timestamp` INTEGER NOT NULL, `receiptNumber` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `budget_goals` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `clientUserId` INTEGER NOT NULL, `weeklyLimit` REAL NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `feedback_reports` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `clientUserId` INTEGER NOT NULL, `targetBusinessId` INTEGER NOT NULL, `category` TEXT NOT NULL, `message` TEXT NOT NULL, `isAnonymous` INTEGER NOT NULL, `status` TEXT NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `shop_ratings` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `businessId` INTEGER NOT NULL, `averageRating` REAL NOT NULL, `ratingCount` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `wholesalers` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `specialty` TEXT NOT NULL, `rating` REAL NOT NULL, `deliveryEstimate` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `restock_requests` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `businessId` INTEGER NOT NULL, `productId` INTEGER NOT NULL, `wholesalerId` INTEGER NOT NULL, `quantity` INTEGER NOT NULL, `status` TEXT NOT NULL, `requestedAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `stock_adjustments` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `productId` INTEGER NOT NULL, `businessId` INTEGER NOT NULL, `delta` INTEGER NOT NULL, `reason` TEXT NOT NULL, `timestamp` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `product_alerts` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `businessId` INTEGER NOT NULL, `productId` INTEGER NOT NULL, `type` TEXT NOT NULL, `message` TEXT NOT NULL, `severity` TEXT NOT NULL, `isRead` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `app_notifications` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `businessId` INTEGER NOT NULL, `type` TEXT NOT NULL, `title` TEXT NOT NULL, `body` TEXT NOT NULL, `actionRoute` TEXT NOT NULL, `isRead` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `expenses` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `businessId` INTEGER NOT NULL, `label` TEXT NOT NULL, `amount` INTEGER NOT NULL, `periodStart` INTEGER NOT NULL, `periodEnd` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '03e6eda41ef40b08ffea553ef46283e6')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `businesses`");
        db.execSQL("DROP TABLE IF EXISTS `products`");
        db.execSQL("DROP TABLE IF EXISTS `sales`");
        db.execSQL("DROP TABLE IF EXISTS `employees`");
        db.execSQL("DROP TABLE IF EXISTS `chat_messages`");
        db.execSQL("DROP TABLE IF EXISTS `promos`");
        db.execSQL("DROP TABLE IF EXISTS `issue_reports`");
        db.execSQL("DROP TABLE IF EXISTS `ebm_receipts`");
        db.execSQL("DROP TABLE IF EXISTS `tax_profiles`");
        db.execSQL("DROP TABLE IF EXISTS `users`");
        db.execSQL("DROP TABLE IF EXISTS `purchases`");
        db.execSQL("DROP TABLE IF EXISTS `budget_goals`");
        db.execSQL("DROP TABLE IF EXISTS `feedback_reports`");
        db.execSQL("DROP TABLE IF EXISTS `shop_ratings`");
        db.execSQL("DROP TABLE IF EXISTS `wholesalers`");
        db.execSQL("DROP TABLE IF EXISTS `restock_requests`");
        db.execSQL("DROP TABLE IF EXISTS `stock_adjustments`");
        db.execSQL("DROP TABLE IF EXISTS `product_alerts`");
        db.execSQL("DROP TABLE IF EXISTS `app_notifications`");
        db.execSQL("DROP TABLE IF EXISTS `expenses`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsBusinesses = new HashMap<String, TableInfo.Column>(7);
        _columnsBusinesses.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBusinesses.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBusinesses.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBusinesses.put("employeeCount", new TableInfo.Column("employeeCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBusinesses.put("language", new TableInfo.Column("language", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBusinesses.put("district", new TableInfo.Column("district", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBusinesses.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBusinesses = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBusinesses = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBusinesses = new TableInfo("businesses", _columnsBusinesses, _foreignKeysBusinesses, _indicesBusinesses);
        final TableInfo _existingBusinesses = TableInfo.read(db, "businesses");
        if (!_infoBusinesses.equals(_existingBusinesses)) {
          return new RoomOpenHelper.ValidationResult(false, "businesses(com.duka.app.data.local.entity.Business).\n"
                  + " Expected:\n" + _infoBusinesses + "\n"
                  + " Found:\n" + _existingBusinesses);
        }
        final HashMap<String, TableInfo.Column> _columnsProducts = new HashMap<String, TableInfo.Column>(10);
        _columnsProducts.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProducts.put("businessId", new TableInfo.Column("businessId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProducts.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProducts.put("price", new TableInfo.Column("price", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProducts.put("stockQuantity", new TableInfo.Column("stockQuantity", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProducts.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProducts.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProducts.put("isDeleted", new TableInfo.Column("isDeleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProducts.put("costPrice", new TableInfo.Column("costPrice", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProducts.put("lowStockThreshold", new TableInfo.Column("lowStockThreshold", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysProducts = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesProducts = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoProducts = new TableInfo("products", _columnsProducts, _foreignKeysProducts, _indicesProducts);
        final TableInfo _existingProducts = TableInfo.read(db, "products");
        if (!_infoProducts.equals(_existingProducts)) {
          return new RoomOpenHelper.ValidationResult(false, "products(com.duka.app.data.local.entity.Product).\n"
                  + " Expected:\n" + _infoProducts + "\n"
                  + " Found:\n" + _existingProducts);
        }
        final HashMap<String, TableInfo.Column> _columnsSales = new HashMap<String, TableInfo.Column>(6);
        _columnsSales.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSales.put("productId", new TableInfo.Column("productId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSales.put("businessId", new TableInfo.Column("businessId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSales.put("amount", new TableInfo.Column("amount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSales.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSales.put("source", new TableInfo.Column("source", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSales = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSales = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSales = new TableInfo("sales", _columnsSales, _foreignKeysSales, _indicesSales);
        final TableInfo _existingSales = TableInfo.read(db, "sales");
        if (!_infoSales.equals(_existingSales)) {
          return new RoomOpenHelper.ValidationResult(false, "sales(com.duka.app.data.local.entity.Sale).\n"
                  + " Expected:\n" + _infoSales + "\n"
                  + " Found:\n" + _existingSales);
        }
        final HashMap<String, TableInfo.Column> _columnsEmployees = new HashMap<String, TableInfo.Column>(9);
        _columnsEmployees.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEmployees.put("businessId", new TableInfo.Column("businessId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEmployees.put("userId", new TableInfo.Column("userId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEmployees.put("code", new TableInfo.Column("code", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEmployees.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEmployees.put("role", new TableInfo.Column("role", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEmployees.put("isActive", new TableInfo.Column("isActive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEmployees.put("joinedAt", new TableInfo.Column("joinedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEmployees.put("lastSeenAt", new TableInfo.Column("lastSeenAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysEmployees = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesEmployees = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoEmployees = new TableInfo("employees", _columnsEmployees, _foreignKeysEmployees, _indicesEmployees);
        final TableInfo _existingEmployees = TableInfo.read(db, "employees");
        if (!_infoEmployees.equals(_existingEmployees)) {
          return new RoomOpenHelper.ValidationResult(false, "employees(com.duka.app.data.local.entity.Employee).\n"
                  + " Expected:\n" + _infoEmployees + "\n"
                  + " Found:\n" + _existingEmployees);
        }
        final HashMap<String, TableInfo.Column> _columnsChatMessages = new HashMap<String, TableInfo.Column>(6);
        _columnsChatMessages.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChatMessages.put("businessId", new TableInfo.Column("businessId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChatMessages.put("senderRole", new TableInfo.Column("senderRole", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChatMessages.put("senderLabel", new TableInfo.Column("senderLabel", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChatMessages.put("text", new TableInfo.Column("text", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChatMessages.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysChatMessages = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesChatMessages = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoChatMessages = new TableInfo("chat_messages", _columnsChatMessages, _foreignKeysChatMessages, _indicesChatMessages);
        final TableInfo _existingChatMessages = TableInfo.read(db, "chat_messages");
        if (!_infoChatMessages.equals(_existingChatMessages)) {
          return new RoomOpenHelper.ValidationResult(false, "chat_messages(com.duka.app.data.local.entity.ChatMessage).\n"
                  + " Expected:\n" + _infoChatMessages + "\n"
                  + " Found:\n" + _existingChatMessages);
        }
        final HashMap<String, TableInfo.Column> _columnsPromos = new HashMap<String, TableInfo.Column>(7);
        _columnsPromos.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPromos.put("businessId", new TableInfo.Column("businessId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPromos.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPromos.put("discountPercent", new TableInfo.Column("discountPercent", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPromos.put("startDate", new TableInfo.Column("startDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPromos.put("endDate", new TableInfo.Column("endDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPromos.put("isLive", new TableInfo.Column("isLive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPromos = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPromos = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPromos = new TableInfo("promos", _columnsPromos, _foreignKeysPromos, _indicesPromos);
        final TableInfo _existingPromos = TableInfo.read(db, "promos");
        if (!_infoPromos.equals(_existingPromos)) {
          return new RoomOpenHelper.ValidationResult(false, "promos(com.duka.app.data.local.entity.Promo).\n"
                  + " Expected:\n" + _infoPromos + "\n"
                  + " Found:\n" + _existingPromos);
        }
        final HashMap<String, TableInfo.Column> _columnsIssueReports = new HashMap<String, TableInfo.Column>(7);
        _columnsIssueReports.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsIssueReports.put("businessId", new TableInfo.Column("businessId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsIssueReports.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsIssueReports.put("message", new TableInfo.Column("message", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsIssueReports.put("attachBusinessId", new TableInfo.Column("attachBusinessId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsIssueReports.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsIssueReports.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysIssueReports = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesIssueReports = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoIssueReports = new TableInfo("issue_reports", _columnsIssueReports, _foreignKeysIssueReports, _indicesIssueReports);
        final TableInfo _existingIssueReports = TableInfo.read(db, "issue_reports");
        if (!_infoIssueReports.equals(_existingIssueReports)) {
          return new RoomOpenHelper.ValidationResult(false, "issue_reports(com.duka.app.data.local.entity.IssueReport).\n"
                  + " Expected:\n" + _infoIssueReports + "\n"
                  + " Found:\n" + _existingIssueReports);
        }
        final HashMap<String, TableInfo.Column> _columnsEbmReceipts = new HashMap<String, TableInfo.Column>(5);
        _columnsEbmReceipts.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEbmReceipts.put("saleId", new TableInfo.Column("saleId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEbmReceipts.put("receiptNumber", new TableInfo.Column("receiptNumber", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEbmReceipts.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEbmReceipts.put("sentAt", new TableInfo.Column("sentAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysEbmReceipts = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesEbmReceipts = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoEbmReceipts = new TableInfo("ebm_receipts", _columnsEbmReceipts, _foreignKeysEbmReceipts, _indicesEbmReceipts);
        final TableInfo _existingEbmReceipts = TableInfo.read(db, "ebm_receipts");
        if (!_infoEbmReceipts.equals(_existingEbmReceipts)) {
          return new RoomOpenHelper.ValidationResult(false, "ebm_receipts(com.duka.app.data.local.entity.EbmReceipt).\n"
                  + " Expected:\n" + _infoEbmReceipts + "\n"
                  + " Found:\n" + _existingEbmReceipts);
        }
        final HashMap<String, TableInfo.Column> _columnsTaxProfiles = new HashMap<String, TableInfo.Column>(4);
        _columnsTaxProfiles.put("businessId", new TableInfo.Column("businessId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTaxProfiles.put("quarterlyTurnover", new TableInfo.Column("quarterlyTurnover", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTaxProfiles.put("tier", new TableInfo.Column("tier", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTaxProfiles.put("vatCollected", new TableInfo.Column("vatCollected", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTaxProfiles = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTaxProfiles = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTaxProfiles = new TableInfo("tax_profiles", _columnsTaxProfiles, _foreignKeysTaxProfiles, _indicesTaxProfiles);
        final TableInfo _existingTaxProfiles = TableInfo.read(db, "tax_profiles");
        if (!_infoTaxProfiles.equals(_existingTaxProfiles)) {
          return new RoomOpenHelper.ValidationResult(false, "tax_profiles(com.duka.app.data.local.entity.TaxProfile).\n"
                  + " Expected:\n" + _infoTaxProfiles + "\n"
                  + " Found:\n" + _existingTaxProfiles);
        }
        final HashMap<String, TableInfo.Column> _columnsUsers = new HashMap<String, TableInfo.Column>(8);
        _columnsUsers.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("businessId", new TableInfo.Column("businessId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("fullName", new TableInfo.Column("fullName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("phoneOrEmail", new TableInfo.Column("phoneOrEmail", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("passwordHash", new TableInfo.Column("passwordHash", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("role", new TableInfo.Column("role", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("isDeleted", new TableInfo.Column("isDeleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUsers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUsers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUsers = new TableInfo("users", _columnsUsers, _foreignKeysUsers, _indicesUsers);
        final TableInfo _existingUsers = TableInfo.read(db, "users");
        if (!_infoUsers.equals(_existingUsers)) {
          return new RoomOpenHelper.ValidationResult(false, "users(com.duka.app.data.local.entity.User).\n"
                  + " Expected:\n" + _infoUsers + "\n"
                  + " Found:\n" + _existingUsers);
        }
        final HashMap<String, TableInfo.Column> _columnsPurchases = new HashMap<String, TableInfo.Column>(7);
        _columnsPurchases.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPurchases.put("clientUserId", new TableInfo.Column("clientUserId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPurchases.put("businessId", new TableInfo.Column("businessId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPurchases.put("productId", new TableInfo.Column("productId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPurchases.put("amount", new TableInfo.Column("amount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPurchases.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPurchases.put("receiptNumber", new TableInfo.Column("receiptNumber", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPurchases = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPurchases = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPurchases = new TableInfo("purchases", _columnsPurchases, _foreignKeysPurchases, _indicesPurchases);
        final TableInfo _existingPurchases = TableInfo.read(db, "purchases");
        if (!_infoPurchases.equals(_existingPurchases)) {
          return new RoomOpenHelper.ValidationResult(false, "purchases(com.duka.app.data.local.entity.Purchase).\n"
                  + " Expected:\n" + _infoPurchases + "\n"
                  + " Found:\n" + _existingPurchases);
        }
        final HashMap<String, TableInfo.Column> _columnsBudgetGoals = new HashMap<String, TableInfo.Column>(4);
        _columnsBudgetGoals.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudgetGoals.put("clientUserId", new TableInfo.Column("clientUserId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudgetGoals.put("weeklyLimit", new TableInfo.Column("weeklyLimit", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudgetGoals.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBudgetGoals = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBudgetGoals = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBudgetGoals = new TableInfo("budget_goals", _columnsBudgetGoals, _foreignKeysBudgetGoals, _indicesBudgetGoals);
        final TableInfo _existingBudgetGoals = TableInfo.read(db, "budget_goals");
        if (!_infoBudgetGoals.equals(_existingBudgetGoals)) {
          return new RoomOpenHelper.ValidationResult(false, "budget_goals(com.duka.app.data.local.entity.BudgetGoal).\n"
                  + " Expected:\n" + _infoBudgetGoals + "\n"
                  + " Found:\n" + _existingBudgetGoals);
        }
        final HashMap<String, TableInfo.Column> _columnsFeedbackReports = new HashMap<String, TableInfo.Column>(8);
        _columnsFeedbackReports.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeedbackReports.put("clientUserId", new TableInfo.Column("clientUserId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeedbackReports.put("targetBusinessId", new TableInfo.Column("targetBusinessId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeedbackReports.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeedbackReports.put("message", new TableInfo.Column("message", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeedbackReports.put("isAnonymous", new TableInfo.Column("isAnonymous", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeedbackReports.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeedbackReports.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFeedbackReports = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFeedbackReports = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFeedbackReports = new TableInfo("feedback_reports", _columnsFeedbackReports, _foreignKeysFeedbackReports, _indicesFeedbackReports);
        final TableInfo _existingFeedbackReports = TableInfo.read(db, "feedback_reports");
        if (!_infoFeedbackReports.equals(_existingFeedbackReports)) {
          return new RoomOpenHelper.ValidationResult(false, "feedback_reports(com.duka.app.data.local.entity.FeedbackReport).\n"
                  + " Expected:\n" + _infoFeedbackReports + "\n"
                  + " Found:\n" + _existingFeedbackReports);
        }
        final HashMap<String, TableInfo.Column> _columnsShopRatings = new HashMap<String, TableInfo.Column>(4);
        _columnsShopRatings.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsShopRatings.put("businessId", new TableInfo.Column("businessId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsShopRatings.put("averageRating", new TableInfo.Column("averageRating", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsShopRatings.put("ratingCount", new TableInfo.Column("ratingCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysShopRatings = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesShopRatings = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoShopRatings = new TableInfo("shop_ratings", _columnsShopRatings, _foreignKeysShopRatings, _indicesShopRatings);
        final TableInfo _existingShopRatings = TableInfo.read(db, "shop_ratings");
        if (!_infoShopRatings.equals(_existingShopRatings)) {
          return new RoomOpenHelper.ValidationResult(false, "shop_ratings(com.duka.app.data.local.entity.ShopRating).\n"
                  + " Expected:\n" + _infoShopRatings + "\n"
                  + " Found:\n" + _existingShopRatings);
        }
        final HashMap<String, TableInfo.Column> _columnsWholesalers = new HashMap<String, TableInfo.Column>(5);
        _columnsWholesalers.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWholesalers.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWholesalers.put("specialty", new TableInfo.Column("specialty", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWholesalers.put("rating", new TableInfo.Column("rating", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWholesalers.put("deliveryEstimate", new TableInfo.Column("deliveryEstimate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysWholesalers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesWholesalers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoWholesalers = new TableInfo("wholesalers", _columnsWholesalers, _foreignKeysWholesalers, _indicesWholesalers);
        final TableInfo _existingWholesalers = TableInfo.read(db, "wholesalers");
        if (!_infoWholesalers.equals(_existingWholesalers)) {
          return new RoomOpenHelper.ValidationResult(false, "wholesalers(com.duka.app.data.local.entity.Wholesaler).\n"
                  + " Expected:\n" + _infoWholesalers + "\n"
                  + " Found:\n" + _existingWholesalers);
        }
        final HashMap<String, TableInfo.Column> _columnsRestockRequests = new HashMap<String, TableInfo.Column>(7);
        _columnsRestockRequests.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestockRequests.put("businessId", new TableInfo.Column("businessId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestockRequests.put("productId", new TableInfo.Column("productId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestockRequests.put("wholesalerId", new TableInfo.Column("wholesalerId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestockRequests.put("quantity", new TableInfo.Column("quantity", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestockRequests.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestockRequests.put("requestedAt", new TableInfo.Column("requestedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRestockRequests = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRestockRequests = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRestockRequests = new TableInfo("restock_requests", _columnsRestockRequests, _foreignKeysRestockRequests, _indicesRestockRequests);
        final TableInfo _existingRestockRequests = TableInfo.read(db, "restock_requests");
        if (!_infoRestockRequests.equals(_existingRestockRequests)) {
          return new RoomOpenHelper.ValidationResult(false, "restock_requests(com.duka.app.data.local.entity.RestockRequest).\n"
                  + " Expected:\n" + _infoRestockRequests + "\n"
                  + " Found:\n" + _existingRestockRequests);
        }
        final HashMap<String, TableInfo.Column> _columnsStockAdjustments = new HashMap<String, TableInfo.Column>(6);
        _columnsStockAdjustments.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStockAdjustments.put("productId", new TableInfo.Column("productId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStockAdjustments.put("businessId", new TableInfo.Column("businessId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStockAdjustments.put("delta", new TableInfo.Column("delta", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStockAdjustments.put("reason", new TableInfo.Column("reason", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStockAdjustments.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysStockAdjustments = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesStockAdjustments = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoStockAdjustments = new TableInfo("stock_adjustments", _columnsStockAdjustments, _foreignKeysStockAdjustments, _indicesStockAdjustments);
        final TableInfo _existingStockAdjustments = TableInfo.read(db, "stock_adjustments");
        if (!_infoStockAdjustments.equals(_existingStockAdjustments)) {
          return new RoomOpenHelper.ValidationResult(false, "stock_adjustments(com.duka.app.data.local.entity.StockAdjustment).\n"
                  + " Expected:\n" + _infoStockAdjustments + "\n"
                  + " Found:\n" + _existingStockAdjustments);
        }
        final HashMap<String, TableInfo.Column> _columnsProductAlerts = new HashMap<String, TableInfo.Column>(8);
        _columnsProductAlerts.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductAlerts.put("businessId", new TableInfo.Column("businessId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductAlerts.put("productId", new TableInfo.Column("productId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductAlerts.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductAlerts.put("message", new TableInfo.Column("message", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductAlerts.put("severity", new TableInfo.Column("severity", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductAlerts.put("isRead", new TableInfo.Column("isRead", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductAlerts.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysProductAlerts = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesProductAlerts = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoProductAlerts = new TableInfo("product_alerts", _columnsProductAlerts, _foreignKeysProductAlerts, _indicesProductAlerts);
        final TableInfo _existingProductAlerts = TableInfo.read(db, "product_alerts");
        if (!_infoProductAlerts.equals(_existingProductAlerts)) {
          return new RoomOpenHelper.ValidationResult(false, "product_alerts(com.duka.app.data.local.entity.ProductAlert).\n"
                  + " Expected:\n" + _infoProductAlerts + "\n"
                  + " Found:\n" + _existingProductAlerts);
        }
        final HashMap<String, TableInfo.Column> _columnsAppNotifications = new HashMap<String, TableInfo.Column>(9);
        _columnsAppNotifications.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppNotifications.put("userId", new TableInfo.Column("userId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppNotifications.put("businessId", new TableInfo.Column("businessId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppNotifications.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppNotifications.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppNotifications.put("body", new TableInfo.Column("body", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppNotifications.put("actionRoute", new TableInfo.Column("actionRoute", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppNotifications.put("isRead", new TableInfo.Column("isRead", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppNotifications.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAppNotifications = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAppNotifications = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAppNotifications = new TableInfo("app_notifications", _columnsAppNotifications, _foreignKeysAppNotifications, _indicesAppNotifications);
        final TableInfo _existingAppNotifications = TableInfo.read(db, "app_notifications");
        if (!_infoAppNotifications.equals(_existingAppNotifications)) {
          return new RoomOpenHelper.ValidationResult(false, "app_notifications(com.duka.app.data.local.entity.AppNotification).\n"
                  + " Expected:\n" + _infoAppNotifications + "\n"
                  + " Found:\n" + _existingAppNotifications);
        }
        final HashMap<String, TableInfo.Column> _columnsExpenses = new HashMap<String, TableInfo.Column>(7);
        _columnsExpenses.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpenses.put("businessId", new TableInfo.Column("businessId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpenses.put("label", new TableInfo.Column("label", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpenses.put("amount", new TableInfo.Column("amount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpenses.put("periodStart", new TableInfo.Column("periodStart", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpenses.put("periodEnd", new TableInfo.Column("periodEnd", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpenses.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysExpenses = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesExpenses = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoExpenses = new TableInfo("expenses", _columnsExpenses, _foreignKeysExpenses, _indicesExpenses);
        final TableInfo _existingExpenses = TableInfo.read(db, "expenses");
        if (!_infoExpenses.equals(_existingExpenses)) {
          return new RoomOpenHelper.ValidationResult(false, "expenses(com.duka.app.data.local.entity.Expense).\n"
                  + " Expected:\n" + _infoExpenses + "\n"
                  + " Found:\n" + _existingExpenses);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "03e6eda41ef40b08ffea553ef46283e6", "321557ca703a55471d4c7c187772f2fa");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "businesses","products","sales","employees","chat_messages","promos","issue_reports","ebm_receipts","tax_profiles","users","purchases","budget_goals","feedback_reports","shop_ratings","wholesalers","restock_requests","stock_adjustments","product_alerts","app_notifications","expenses");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `businesses`");
      _db.execSQL("DELETE FROM `products`");
      _db.execSQL("DELETE FROM `sales`");
      _db.execSQL("DELETE FROM `employees`");
      _db.execSQL("DELETE FROM `chat_messages`");
      _db.execSQL("DELETE FROM `promos`");
      _db.execSQL("DELETE FROM `issue_reports`");
      _db.execSQL("DELETE FROM `ebm_receipts`");
      _db.execSQL("DELETE FROM `tax_profiles`");
      _db.execSQL("DELETE FROM `users`");
      _db.execSQL("DELETE FROM `purchases`");
      _db.execSQL("DELETE FROM `budget_goals`");
      _db.execSQL("DELETE FROM `feedback_reports`");
      _db.execSQL("DELETE FROM `shop_ratings`");
      _db.execSQL("DELETE FROM `wholesalers`");
      _db.execSQL("DELETE FROM `restock_requests`");
      _db.execSQL("DELETE FROM `stock_adjustments`");
      _db.execSQL("DELETE FROM `product_alerts`");
      _db.execSQL("DELETE FROM `app_notifications`");
      _db.execSQL("DELETE FROM `expenses`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(BusinessDao.class, BusinessDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ProductDao.class, ProductDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SaleDao.class, SaleDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(EmployeeDao.class, EmployeeDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ChatMessageDao.class, ChatMessageDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PromoDao.class, PromoDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(IssueReportDao.class, IssueReportDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(EbmReceiptDao.class, EbmReceiptDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TaxProfileDao.class, TaxProfileDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(UserDao.class, UserDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PurchaseDao.class, PurchaseDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(BudgetGoalDao.class, BudgetGoalDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(FeedbackReportDao.class, FeedbackReportDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ShopRatingDao.class, ShopRatingDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(WholesalerDao.class, WholesalerDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RestockRequestDao.class, RestockRequestDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(StockAdjustmentDao.class, StockAdjustmentDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ProductAlertDao.class, ProductAlertDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(AppNotificationDao.class, AppNotificationDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ExpenseDao.class, ExpenseDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public BusinessDao businessDao() {
    if (_businessDao != null) {
      return _businessDao;
    } else {
      synchronized(this) {
        if(_businessDao == null) {
          _businessDao = new BusinessDao_Impl(this);
        }
        return _businessDao;
      }
    }
  }

  @Override
  public ProductDao productDao() {
    if (_productDao != null) {
      return _productDao;
    } else {
      synchronized(this) {
        if(_productDao == null) {
          _productDao = new ProductDao_Impl(this);
        }
        return _productDao;
      }
    }
  }

  @Override
  public SaleDao saleDao() {
    if (_saleDao != null) {
      return _saleDao;
    } else {
      synchronized(this) {
        if(_saleDao == null) {
          _saleDao = new SaleDao_Impl(this);
        }
        return _saleDao;
      }
    }
  }

  @Override
  public EmployeeDao employeeDao() {
    if (_employeeDao != null) {
      return _employeeDao;
    } else {
      synchronized(this) {
        if(_employeeDao == null) {
          _employeeDao = new EmployeeDao_Impl(this);
        }
        return _employeeDao;
      }
    }
  }

  @Override
  public ChatMessageDao chatMessageDao() {
    if (_chatMessageDao != null) {
      return _chatMessageDao;
    } else {
      synchronized(this) {
        if(_chatMessageDao == null) {
          _chatMessageDao = new ChatMessageDao_Impl(this);
        }
        return _chatMessageDao;
      }
    }
  }

  @Override
  public PromoDao promoDao() {
    if (_promoDao != null) {
      return _promoDao;
    } else {
      synchronized(this) {
        if(_promoDao == null) {
          _promoDao = new PromoDao_Impl(this);
        }
        return _promoDao;
      }
    }
  }

  @Override
  public IssueReportDao issueReportDao() {
    if (_issueReportDao != null) {
      return _issueReportDao;
    } else {
      synchronized(this) {
        if(_issueReportDao == null) {
          _issueReportDao = new IssueReportDao_Impl(this);
        }
        return _issueReportDao;
      }
    }
  }

  @Override
  public EbmReceiptDao ebmReceiptDao() {
    if (_ebmReceiptDao != null) {
      return _ebmReceiptDao;
    } else {
      synchronized(this) {
        if(_ebmReceiptDao == null) {
          _ebmReceiptDao = new EbmReceiptDao_Impl(this);
        }
        return _ebmReceiptDao;
      }
    }
  }

  @Override
  public TaxProfileDao taxProfileDao() {
    if (_taxProfileDao != null) {
      return _taxProfileDao;
    } else {
      synchronized(this) {
        if(_taxProfileDao == null) {
          _taxProfileDao = new TaxProfileDao_Impl(this);
        }
        return _taxProfileDao;
      }
    }
  }

  @Override
  public UserDao userDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }

  @Override
  public PurchaseDao purchaseDao() {
    if (_purchaseDao != null) {
      return _purchaseDao;
    } else {
      synchronized(this) {
        if(_purchaseDao == null) {
          _purchaseDao = new PurchaseDao_Impl(this);
        }
        return _purchaseDao;
      }
    }
  }

  @Override
  public BudgetGoalDao budgetGoalDao() {
    if (_budgetGoalDao != null) {
      return _budgetGoalDao;
    } else {
      synchronized(this) {
        if(_budgetGoalDao == null) {
          _budgetGoalDao = new BudgetGoalDao_Impl(this);
        }
        return _budgetGoalDao;
      }
    }
  }

  @Override
  public FeedbackReportDao feedbackReportDao() {
    if (_feedbackReportDao != null) {
      return _feedbackReportDao;
    } else {
      synchronized(this) {
        if(_feedbackReportDao == null) {
          _feedbackReportDao = new FeedbackReportDao_Impl(this);
        }
        return _feedbackReportDao;
      }
    }
  }

  @Override
  public ShopRatingDao shopRatingDao() {
    if (_shopRatingDao != null) {
      return _shopRatingDao;
    } else {
      synchronized(this) {
        if(_shopRatingDao == null) {
          _shopRatingDao = new ShopRatingDao_Impl(this);
        }
        return _shopRatingDao;
      }
    }
  }

  @Override
  public WholesalerDao wholesalerDao() {
    if (_wholesalerDao != null) {
      return _wholesalerDao;
    } else {
      synchronized(this) {
        if(_wholesalerDao == null) {
          _wholesalerDao = new WholesalerDao_Impl(this);
        }
        return _wholesalerDao;
      }
    }
  }

  @Override
  public RestockRequestDao restockRequestDao() {
    if (_restockRequestDao != null) {
      return _restockRequestDao;
    } else {
      synchronized(this) {
        if(_restockRequestDao == null) {
          _restockRequestDao = new RestockRequestDao_Impl(this);
        }
        return _restockRequestDao;
      }
    }
  }

  @Override
  public StockAdjustmentDao stockAdjustmentDao() {
    if (_stockAdjustmentDao != null) {
      return _stockAdjustmentDao;
    } else {
      synchronized(this) {
        if(_stockAdjustmentDao == null) {
          _stockAdjustmentDao = new StockAdjustmentDao_Impl(this);
        }
        return _stockAdjustmentDao;
      }
    }
  }

  @Override
  public ProductAlertDao productAlertDao() {
    if (_productAlertDao != null) {
      return _productAlertDao;
    } else {
      synchronized(this) {
        if(_productAlertDao == null) {
          _productAlertDao = new ProductAlertDao_Impl(this);
        }
        return _productAlertDao;
      }
    }
  }

  @Override
  public AppNotificationDao appNotificationDao() {
    if (_appNotificationDao != null) {
      return _appNotificationDao;
    } else {
      synchronized(this) {
        if(_appNotificationDao == null) {
          _appNotificationDao = new AppNotificationDao_Impl(this);
        }
        return _appNotificationDao;
      }
    }
  }

  @Override
  public ExpenseDao expenseDao() {
    if (_expenseDao != null) {
      return _expenseDao;
    } else {
      synchronized(this) {
        if(_expenseDao == null) {
          _expenseDao = new ExpenseDao_Impl(this);
        }
        return _expenseDao;
      }
    }
  }
}
