package com.duka.app.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.duka.app.data.local.entity.Sale;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SaleDao_Impl implements SaleDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Sale> __insertionAdapterOfSale;

  public SaleDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSale = new EntityInsertionAdapter<Sale>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `sales` (`id`,`productId`,`businessId`,`amount`,`timestamp`,`source`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Sale entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getProductId());
        statement.bindLong(3, entity.getBusinessId());
        statement.bindDouble(4, entity.getAmount());
        statement.bindLong(5, entity.getTimestamp());
        statement.bindString(6, entity.getSource());
      }
    };
  }

  @Override
  public Object insert(final Sale sale, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfSale.insertAndReturnId(sale);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getSaleById(final long saleId, final Continuation<? super Sale> $completion) {
    final String _sql = "SELECT * FROM sales WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, saleId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Sale>() {
      @Override
      @Nullable
      public Sale call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProductId = CursorUtil.getColumnIndexOrThrow(_cursor, "productId");
          final int _cursorIndexOfBusinessId = CursorUtil.getColumnIndexOrThrow(_cursor, "businessId");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfSource = CursorUtil.getColumnIndexOrThrow(_cursor, "source");
          final Sale _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpProductId;
            _tmpProductId = _cursor.getLong(_cursorIndexOfProductId);
            final long _tmpBusinessId;
            _tmpBusinessId = _cursor.getLong(_cursorIndexOfBusinessId);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpSource;
            _tmpSource = _cursor.getString(_cursorIndexOfSource);
            _result = new Sale(_tmpId,_tmpProductId,_tmpBusinessId,_tmpAmount,_tmpTimestamp,_tmpSource);
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
  public Flow<List<Sale>> getSalesInRange(final long businessId, final long startTime,
      final long endTime) {
    final String _sql = "SELECT * FROM sales WHERE businessId = ? AND timestamp >= ? AND timestamp < ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, businessId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, startTime);
    _argIndex = 3;
    _statement.bindLong(_argIndex, endTime);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sales"}, new Callable<List<Sale>>() {
      @Override
      @NonNull
      public List<Sale> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProductId = CursorUtil.getColumnIndexOrThrow(_cursor, "productId");
          final int _cursorIndexOfBusinessId = CursorUtil.getColumnIndexOrThrow(_cursor, "businessId");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfSource = CursorUtil.getColumnIndexOrThrow(_cursor, "source");
          final List<Sale> _result = new ArrayList<Sale>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Sale _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpProductId;
            _tmpProductId = _cursor.getLong(_cursorIndexOfProductId);
            final long _tmpBusinessId;
            _tmpBusinessId = _cursor.getLong(_cursorIndexOfBusinessId);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpSource;
            _tmpSource = _cursor.getString(_cursorIndexOfSource);
            _item = new Sale(_tmpId,_tmpProductId,_tmpBusinessId,_tmpAmount,_tmpTimestamp,_tmpSource);
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
  public Object getSalesInRangeOnce(final long businessId, final long startTime, final long endTime,
      final Continuation<? super List<Sale>> $completion) {
    final String _sql = "SELECT * FROM sales WHERE businessId = ? AND timestamp >= ? AND timestamp < ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, businessId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, startTime);
    _argIndex = 3;
    _statement.bindLong(_argIndex, endTime);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Sale>>() {
      @Override
      @NonNull
      public List<Sale> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProductId = CursorUtil.getColumnIndexOrThrow(_cursor, "productId");
          final int _cursorIndexOfBusinessId = CursorUtil.getColumnIndexOrThrow(_cursor, "businessId");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfSource = CursorUtil.getColumnIndexOrThrow(_cursor, "source");
          final List<Sale> _result = new ArrayList<Sale>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Sale _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpProductId;
            _tmpProductId = _cursor.getLong(_cursorIndexOfProductId);
            final long _tmpBusinessId;
            _tmpBusinessId = _cursor.getLong(_cursorIndexOfBusinessId);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpSource;
            _tmpSource = _cursor.getString(_cursorIndexOfSource);
            _item = new Sale(_tmpId,_tmpProductId,_tmpBusinessId,_tmpAmount,_tmpTimestamp,_tmpSource);
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

  @Override
  public Flow<Double> getTotalInRange(final long businessId, final long startTime,
      final long endTime) {
    final String _sql = "SELECT SUM(amount) FROM sales WHERE businessId = ? AND timestamp >= ? AND timestamp < ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, businessId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, startTime);
    _argIndex = 3;
    _statement.bindLong(_argIndex, endTime);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sales"}, new Callable<Double>() {
      @Override
      @Nullable
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final Double _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getDouble(0);
            }
            _result = _tmp;
          } else {
            _result = null;
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
  public Object getTotalInRangeOnce(final long businessId, final long startTime, final long endTime,
      final Continuation<? super Double> $completion) {
    final String _sql = "SELECT SUM(amount) FROM sales WHERE businessId = ? AND timestamp >= ? AND timestamp < ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, businessId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, startTime);
    _argIndex = 3;
    _statement.bindLong(_argIndex, endTime);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Double>() {
      @Override
      @Nullable
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final Double _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getDouble(0);
            }
            _result = _tmp;
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
  public Flow<Integer> getSalesCountInRange(final long businessId, final long startTime,
      final long endTime) {
    final String _sql = "SELECT COUNT(*) FROM sales WHERE businessId = ? AND timestamp >= ? AND timestamp < ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, businessId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, startTime);
    _argIndex = 3;
    _statement.bindLong(_argIndex, endTime);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sales"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
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
  public Flow<List<ProductCount>> getTopProducts(final long businessId, final long startTime,
      final long endTime) {
    final String _sql = "\n"
            + "        SELECT productId, COUNT(*) as cnt FROM sales \n"
            + "        WHERE businessId = ? AND timestamp >= ? AND timestamp < ?\n"
            + "        GROUP BY productId ORDER BY cnt DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, businessId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, startTime);
    _argIndex = 3;
    _statement.bindLong(_argIndex, endTime);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sales"}, new Callable<List<ProductCount>>() {
      @Override
      @NonNull
      public List<ProductCount> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfProductId = 0;
          final int _cursorIndexOfCnt = 1;
          final List<ProductCount> _result = new ArrayList<ProductCount>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ProductCount _item;
            final long _tmpProductId;
            _tmpProductId = _cursor.getLong(_cursorIndexOfProductId);
            final int _tmpCnt;
            _tmpCnt = _cursor.getInt(_cursorIndexOfCnt);
            _item = new ProductCount(_tmpProductId,_tmpCnt);
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
  public Object getTotalAllTime(final long businessId,
      final Continuation<? super Double> $completion) {
    final String _sql = "SELECT SUM(amount) FROM sales WHERE businessId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, businessId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Double>() {
      @Override
      @Nullable
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final Double _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getDouble(0);
            }
            _result = _tmp;
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
  public Object getLatestSale(final long businessId, final Continuation<? super Sale> $completion) {
    final String _sql = "SELECT * FROM sales WHERE businessId = ? ORDER BY timestamp DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, businessId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Sale>() {
      @Override
      @Nullable
      public Sale call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProductId = CursorUtil.getColumnIndexOrThrow(_cursor, "productId");
          final int _cursorIndexOfBusinessId = CursorUtil.getColumnIndexOrThrow(_cursor, "businessId");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfSource = CursorUtil.getColumnIndexOrThrow(_cursor, "source");
          final Sale _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpProductId;
            _tmpProductId = _cursor.getLong(_cursorIndexOfProductId);
            final long _tmpBusinessId;
            _tmpBusinessId = _cursor.getLong(_cursorIndexOfBusinessId);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpSource;
            _tmpSource = _cursor.getString(_cursorIndexOfSource);
            _result = new Sale(_tmpId,_tmpProductId,_tmpBusinessId,_tmpAmount,_tmpTimestamp,_tmpSource);
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
  public Object getAllByBusiness(final long businessId,
      final Continuation<? super List<Sale>> $completion) {
    final String _sql = "SELECT * FROM sales WHERE businessId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, businessId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Sale>>() {
      @Override
      @NonNull
      public List<Sale> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProductId = CursorUtil.getColumnIndexOrThrow(_cursor, "productId");
          final int _cursorIndexOfBusinessId = CursorUtil.getColumnIndexOrThrow(_cursor, "businessId");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfSource = CursorUtil.getColumnIndexOrThrow(_cursor, "source");
          final List<Sale> _result = new ArrayList<Sale>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Sale _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpProductId;
            _tmpProductId = _cursor.getLong(_cursorIndexOfProductId);
            final long _tmpBusinessId;
            _tmpBusinessId = _cursor.getLong(_cursorIndexOfBusinessId);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpSource;
            _tmpSource = _cursor.getString(_cursorIndexOfSource);
            _item = new Sale(_tmpId,_tmpProductId,_tmpBusinessId,_tmpAmount,_tmpTimestamp,_tmpSource);
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

  @Override
  public Object getByProductInRange(final long productId, final long startTime, final long endTime,
      final Continuation<? super List<Sale>> $completion) {
    final String _sql = "SELECT * FROM sales WHERE productId = ? AND timestamp >= ? AND timestamp < ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, productId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, startTime);
    _argIndex = 3;
    _statement.bindLong(_argIndex, endTime);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Sale>>() {
      @Override
      @NonNull
      public List<Sale> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProductId = CursorUtil.getColumnIndexOrThrow(_cursor, "productId");
          final int _cursorIndexOfBusinessId = CursorUtil.getColumnIndexOrThrow(_cursor, "businessId");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfSource = CursorUtil.getColumnIndexOrThrow(_cursor, "source");
          final List<Sale> _result = new ArrayList<Sale>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Sale _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpProductId;
            _tmpProductId = _cursor.getLong(_cursorIndexOfProductId);
            final long _tmpBusinessId;
            _tmpBusinessId = _cursor.getLong(_cursorIndexOfBusinessId);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpSource;
            _tmpSource = _cursor.getString(_cursorIndexOfSource);
            _item = new Sale(_tmpId,_tmpProductId,_tmpBusinessId,_tmpAmount,_tmpTimestamp,_tmpSource);
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

  @Override
  public Object getAllTimeTopProducts(final long businessId,
      final Continuation<? super List<ProductCount>> $completion) {
    final String _sql = "SELECT productId, COUNT(*) as cnt FROM sales WHERE businessId = ? GROUP BY productId ORDER BY cnt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, businessId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ProductCount>>() {
      @Override
      @NonNull
      public List<ProductCount> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfProductId = 0;
          final int _cursorIndexOfCnt = 1;
          final List<ProductCount> _result = new ArrayList<ProductCount>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ProductCount _item;
            final long _tmpProductId;
            _tmpProductId = _cursor.getLong(_cursorIndexOfProductId);
            final int _tmpCnt;
            _tmpCnt = _cursor.getInt(_cursorIndexOfCnt);
            _item = new ProductCount(_tmpProductId,_tmpCnt);
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

  @Override
  public Object revenueByCategory(final long businessId, final long from, final long to,
      final Continuation<? super List<CategoryRevenue>> $completion) {
    final String _sql = "\n"
            + "        SELECT p.category, SUM(s.amount) as total\n"
            + "        FROM sales s\n"
            + "        INNER JOIN products p ON s.productId = p.id\n"
            + "        WHERE s.businessId = ?\n"
            + "          AND s.timestamp BETWEEN ? AND ?\n"
            + "        GROUP BY p.category\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, businessId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, from);
    _argIndex = 3;
    _statement.bindLong(_argIndex, to);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<CategoryRevenue>>() {
      @Override
      @NonNull
      public List<CategoryRevenue> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfCategory = 0;
          final int _cursorIndexOfTotal = 1;
          final List<CategoryRevenue> _result = new ArrayList<CategoryRevenue>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CategoryRevenue _item;
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final double _tmpTotal;
            _tmpTotal = _cursor.getDouble(_cursorIndexOfTotal);
            _item = new CategoryRevenue(_tmpCategory,_tmpTotal);
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

  @Override
  public Object revenueBySource(final long businessId, final long from, final long to,
      final Continuation<? super List<SourceRevenue>> $completion) {
    final String _sql = "\n"
            + "        SELECT source, SUM(amount) as total\n"
            + "        FROM sales\n"
            + "        WHERE businessId = ?\n"
            + "          AND timestamp BETWEEN ? AND ?\n"
            + "        GROUP BY source\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, businessId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, from);
    _argIndex = 3;
    _statement.bindLong(_argIndex, to);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<SourceRevenue>>() {
      @Override
      @NonNull
      public List<SourceRevenue> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfSource = 0;
          final int _cursorIndexOfTotal = 1;
          final List<SourceRevenue> _result = new ArrayList<SourceRevenue>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SourceRevenue _item;
            final String _tmpSource;
            _tmpSource = _cursor.getString(_cursorIndexOfSource);
            final double _tmpTotal;
            _tmpTotal = _cursor.getDouble(_cursorIndexOfTotal);
            _item = new SourceRevenue(_tmpSource,_tmpTotal);
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
