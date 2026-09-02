package com.duka.app.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.duka.app.data.local.entity.Product;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ProductDao_Impl implements ProductDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Product> __insertionAdapterOfProduct;

  private final EntityDeletionOrUpdateAdapter<Product> __updateAdapterOfProduct;

  private final SharedSQLiteStatement __preparedStmtOfDecrementStock;

  private final SharedSQLiteStatement __preparedStmtOfIncrementStock;

  private final SharedSQLiteStatement __preparedStmtOfSoftDelete;

  public ProductDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfProduct = new EntityInsertionAdapter<Product>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `products` (`id`,`businessId`,`name`,`price`,`stockQuantity`,`category`,`createdAt`,`isDeleted`,`costPrice`,`lowStockThreshold`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Product entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getBusinessId());
        statement.bindString(3, entity.getName());
        statement.bindDouble(4, entity.getPrice());
        statement.bindLong(5, entity.getStockQuantity());
        statement.bindString(6, entity.getCategory());
        statement.bindLong(7, entity.getCreatedAt());
        final int _tmp = entity.isDeleted() ? 1 : 0;
        statement.bindLong(8, _tmp);
        statement.bindLong(9, entity.getCostPrice());
        statement.bindLong(10, entity.getLowStockThreshold());
      }
    };
    this.__updateAdapterOfProduct = new EntityDeletionOrUpdateAdapter<Product>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `products` SET `id` = ?,`businessId` = ?,`name` = ?,`price` = ?,`stockQuantity` = ?,`category` = ?,`createdAt` = ?,`isDeleted` = ?,`costPrice` = ?,`lowStockThreshold` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Product entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getBusinessId());
        statement.bindString(3, entity.getName());
        statement.bindDouble(4, entity.getPrice());
        statement.bindLong(5, entity.getStockQuantity());
        statement.bindString(6, entity.getCategory());
        statement.bindLong(7, entity.getCreatedAt());
        final int _tmp = entity.isDeleted() ? 1 : 0;
        statement.bindLong(8, _tmp);
        statement.bindLong(9, entity.getCostPrice());
        statement.bindLong(10, entity.getLowStockThreshold());
        statement.bindLong(11, entity.getId());
      }
    };
    this.__preparedStmtOfDecrementStock = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE products SET stockQuantity = stockQuantity - 1 WHERE id = ? AND stockQuantity > 0";
        return _query;
      }
    };
    this.__preparedStmtOfIncrementStock = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE products SET stockQuantity = stockQuantity + ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSoftDelete = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE products SET isDeleted = 1 WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final Product product, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfProduct.insertAndReturnId(product);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final Product product, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfProduct.handle(product);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object decrementStock(final long productId,
      final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDecrementStock.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, productId);
        try {
          __db.beginTransaction();
          try {
            final Integer _result = _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDecrementStock.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object incrementStock(final long productId, final int amount,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfIncrementStock.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, amount);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, productId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfIncrementStock.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object softDelete(final long productId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSoftDelete.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, productId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfSoftDelete.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Product>> getProductsByBusiness(final long businessId) {
    final String _sql = "SELECT * FROM products WHERE businessId = ? AND isDeleted = 0 ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, businessId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"products"}, new Callable<List<Product>>() {
      @Override
      @NonNull
      public List<Product> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBusinessId = CursorUtil.getColumnIndexOrThrow(_cursor, "businessId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "price");
          final int _cursorIndexOfStockQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "stockQuantity");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final int _cursorIndexOfCostPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "costPrice");
          final int _cursorIndexOfLowStockThreshold = CursorUtil.getColumnIndexOrThrow(_cursor, "lowStockThreshold");
          final List<Product> _result = new ArrayList<Product>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Product _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpBusinessId;
            _tmpBusinessId = _cursor.getLong(_cursorIndexOfBusinessId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final double _tmpPrice;
            _tmpPrice = _cursor.getDouble(_cursorIndexOfPrice);
            final int _tmpStockQuantity;
            _tmpStockQuantity = _cursor.getInt(_cursorIndexOfStockQuantity);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final boolean _tmpIsDeleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp != 0;
            final long _tmpCostPrice;
            _tmpCostPrice = _cursor.getLong(_cursorIndexOfCostPrice);
            final int _tmpLowStockThreshold;
            _tmpLowStockThreshold = _cursor.getInt(_cursorIndexOfLowStockThreshold);
            _item = new Product(_tmpId,_tmpBusinessId,_tmpName,_tmpPrice,_tmpStockQuantity,_tmpCategory,_tmpCreatedAt,_tmpIsDeleted,_tmpCostPrice,_tmpLowStockThreshold);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<Product>> getRecentProducts(final long businessId) {
    final String _sql = "SELECT * FROM products WHERE businessId = ? AND isDeleted = 0 ORDER BY createdAt DESC LIMIT 5";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, businessId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"products"}, new Callable<List<Product>>() {
      @Override
      @NonNull
      public List<Product> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBusinessId = CursorUtil.getColumnIndexOrThrow(_cursor, "businessId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "price");
          final int _cursorIndexOfStockQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "stockQuantity");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final int _cursorIndexOfCostPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "costPrice");
          final int _cursorIndexOfLowStockThreshold = CursorUtil.getColumnIndexOrThrow(_cursor, "lowStockThreshold");
          final List<Product> _result = new ArrayList<Product>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Product _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpBusinessId;
            _tmpBusinessId = _cursor.getLong(_cursorIndexOfBusinessId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final double _tmpPrice;
            _tmpPrice = _cursor.getDouble(_cursorIndexOfPrice);
            final int _tmpStockQuantity;
            _tmpStockQuantity = _cursor.getInt(_cursorIndexOfStockQuantity);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final boolean _tmpIsDeleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp != 0;
            final long _tmpCostPrice;
            _tmpCostPrice = _cursor.getLong(_cursorIndexOfCostPrice);
            final int _tmpLowStockThreshold;
            _tmpLowStockThreshold = _cursor.getInt(_cursorIndexOfLowStockThreshold);
            _item = new Product(_tmpId,_tmpBusinessId,_tmpName,_tmpPrice,_tmpStockQuantity,_tmpCategory,_tmpCreatedAt,_tmpIsDeleted,_tmpCostPrice,_tmpLowStockThreshold);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<Product>> searchProducts(final long businessId, final String query) {
    final String _sql = "SELECT * FROM products WHERE businessId = ? AND isDeleted = 0 AND name LIKE '%' || ? || '%'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, businessId);
    _argIndex = 2;
    _statement.bindString(_argIndex, query);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"products"}, new Callable<List<Product>>() {
      @Override
      @NonNull
      public List<Product> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBusinessId = CursorUtil.getColumnIndexOrThrow(_cursor, "businessId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "price");
          final int _cursorIndexOfStockQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "stockQuantity");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final int _cursorIndexOfCostPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "costPrice");
          final int _cursorIndexOfLowStockThreshold = CursorUtil.getColumnIndexOrThrow(_cursor, "lowStockThreshold");
          final List<Product> _result = new ArrayList<Product>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Product _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpBusinessId;
            _tmpBusinessId = _cursor.getLong(_cursorIndexOfBusinessId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final double _tmpPrice;
            _tmpPrice = _cursor.getDouble(_cursorIndexOfPrice);
            final int _tmpStockQuantity;
            _tmpStockQuantity = _cursor.getInt(_cursorIndexOfStockQuantity);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final boolean _tmpIsDeleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp != 0;
            final long _tmpCostPrice;
            _tmpCostPrice = _cursor.getLong(_cursorIndexOfCostPrice);
            final int _tmpLowStockThreshold;
            _tmpLowStockThreshold = _cursor.getInt(_cursorIndexOfLowStockThreshold);
            _item = new Product(_tmpId,_tmpBusinessId,_tmpName,_tmpPrice,_tmpStockQuantity,_tmpCategory,_tmpCreatedAt,_tmpIsDeleted,_tmpCostPrice,_tmpLowStockThreshold);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getProductById(final long id, final Continuation<? super Product> $completion) {
    final String _sql = "SELECT * FROM products WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Product>() {
      @Override
      @Nullable
      public Product call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBusinessId = CursorUtil.getColumnIndexOrThrow(_cursor, "businessId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "price");
          final int _cursorIndexOfStockQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "stockQuantity");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final int _cursorIndexOfCostPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "costPrice");
          final int _cursorIndexOfLowStockThreshold = CursorUtil.getColumnIndexOrThrow(_cursor, "lowStockThreshold");
          final Product _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpBusinessId;
            _tmpBusinessId = _cursor.getLong(_cursorIndexOfBusinessId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final double _tmpPrice;
            _tmpPrice = _cursor.getDouble(_cursorIndexOfPrice);
            final int _tmpStockQuantity;
            _tmpStockQuantity = _cursor.getInt(_cursorIndexOfStockQuantity);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final boolean _tmpIsDeleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp != 0;
            final long _tmpCostPrice;
            _tmpCostPrice = _cursor.getLong(_cursorIndexOfCostPrice);
            final int _tmpLowStockThreshold;
            _tmpLowStockThreshold = _cursor.getInt(_cursorIndexOfLowStockThreshold);
            _result = new Product(_tmpId,_tmpBusinessId,_tmpName,_tmpPrice,_tmpStockQuantity,_tmpCategory,_tmpCreatedAt,_tmpIsDeleted,_tmpCostPrice,_tmpLowStockThreshold);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getProductsByIds(final List<Long> ids,
      final Continuation<? super List<Product>> $completion) {
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT * FROM products WHERE id IN (");
    final int _inputSize = ids.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (long _item : ids) {
      _statement.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Product>>() {
      @Override
      @NonNull
      public List<Product> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBusinessId = CursorUtil.getColumnIndexOrThrow(_cursor, "businessId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "price");
          final int _cursorIndexOfStockQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "stockQuantity");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final int _cursorIndexOfCostPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "costPrice");
          final int _cursorIndexOfLowStockThreshold = CursorUtil.getColumnIndexOrThrow(_cursor, "lowStockThreshold");
          final List<Product> _result = new ArrayList<Product>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Product _item_1;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpBusinessId;
            _tmpBusinessId = _cursor.getLong(_cursorIndexOfBusinessId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final double _tmpPrice;
            _tmpPrice = _cursor.getDouble(_cursorIndexOfPrice);
            final int _tmpStockQuantity;
            _tmpStockQuantity = _cursor.getInt(_cursorIndexOfStockQuantity);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final boolean _tmpIsDeleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp != 0;
            final long _tmpCostPrice;
            _tmpCostPrice = _cursor.getLong(_cursorIndexOfCostPrice);
            final int _tmpLowStockThreshold;
            _tmpLowStockThreshold = _cursor.getInt(_cursorIndexOfLowStockThreshold);
            _item_1 = new Product(_tmpId,_tmpBusinessId,_tmpName,_tmpPrice,_tmpStockQuantity,_tmpCategory,_tmpCreatedAt,_tmpIsDeleted,_tmpCostPrice,_tmpLowStockThreshold);
            _result.add(_item_1);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<String>> getCategoriesByBusiness(final long businessId) {
    final String _sql = "SELECT DISTINCT category FROM products WHERE businessId = ? AND isDeleted = 0 ORDER BY category";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, businessId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"products"}, new Callable<List<String>>() {
      @Override
      @NonNull
      public List<String> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<String> _result = new ArrayList<String>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final String _item;
            _item = _cursor.getString(0);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getAllActiveByBusiness(final long businessId,
      final Continuation<? super List<Product>> $completion) {
    final String _sql = "SELECT * FROM products WHERE businessId = ? AND isDeleted = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, businessId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Product>>() {
      @Override
      @NonNull
      public List<Product> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBusinessId = CursorUtil.getColumnIndexOrThrow(_cursor, "businessId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "price");
          final int _cursorIndexOfStockQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "stockQuantity");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final int _cursorIndexOfCostPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "costPrice");
          final int _cursorIndexOfLowStockThreshold = CursorUtil.getColumnIndexOrThrow(_cursor, "lowStockThreshold");
          final List<Product> _result = new ArrayList<Product>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Product _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpBusinessId;
            _tmpBusinessId = _cursor.getLong(_cursorIndexOfBusinessId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final double _tmpPrice;
            _tmpPrice = _cursor.getDouble(_cursorIndexOfPrice);
            final int _tmpStockQuantity;
            _tmpStockQuantity = _cursor.getInt(_cursorIndexOfStockQuantity);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final boolean _tmpIsDeleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp != 0;
            final long _tmpCostPrice;
            _tmpCostPrice = _cursor.getLong(_cursorIndexOfCostPrice);
            final int _tmpLowStockThreshold;
            _tmpLowStockThreshold = _cursor.getInt(_cursorIndexOfLowStockThreshold);
            _item = new Product(_tmpId,_tmpBusinessId,_tmpName,_tmpPrice,_tmpStockQuantity,_tmpCategory,_tmpCreatedAt,_tmpIsDeleted,_tmpCostPrice,_tmpLowStockThreshold);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
