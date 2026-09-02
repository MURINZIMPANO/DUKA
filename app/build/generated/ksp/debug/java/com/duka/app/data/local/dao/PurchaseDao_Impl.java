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
import com.duka.app.data.local.entity.Purchase;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
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
public final class PurchaseDao_Impl implements PurchaseDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Purchase> __insertionAdapterOfPurchase;

  public PurchaseDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPurchase = new EntityInsertionAdapter<Purchase>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `purchases` (`id`,`clientUserId`,`businessId`,`productId`,`amount`,`timestamp`,`receiptNumber`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Purchase entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getClientUserId());
        statement.bindLong(3, entity.getBusinessId());
        statement.bindLong(4, entity.getProductId());
        statement.bindDouble(5, entity.getAmount());
        statement.bindLong(6, entity.getTimestamp());
        statement.bindString(7, entity.getReceiptNumber());
      }
    };
  }

  @Override
  public Object insert(final Purchase purchase, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfPurchase.insertAndReturnId(purchase);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Purchase>> getByClient(final long clientUserId) {
    final String _sql = "SELECT * FROM purchases WHERE clientUserId = ? ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, clientUserId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"purchases"}, new Callable<List<Purchase>>() {
      @Override
      @NonNull
      public List<Purchase> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfClientUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "clientUserId");
          final int _cursorIndexOfBusinessId = CursorUtil.getColumnIndexOrThrow(_cursor, "businessId");
          final int _cursorIndexOfProductId = CursorUtil.getColumnIndexOrThrow(_cursor, "productId");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfReceiptNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "receiptNumber");
          final List<Purchase> _result = new ArrayList<Purchase>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Purchase _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpClientUserId;
            _tmpClientUserId = _cursor.getLong(_cursorIndexOfClientUserId);
            final long _tmpBusinessId;
            _tmpBusinessId = _cursor.getLong(_cursorIndexOfBusinessId);
            final long _tmpProductId;
            _tmpProductId = _cursor.getLong(_cursorIndexOfProductId);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpReceiptNumber;
            _tmpReceiptNumber = _cursor.getString(_cursorIndexOfReceiptNumber);
            _item = new Purchase(_tmpId,_tmpClientUserId,_tmpBusinessId,_tmpProductId,_tmpAmount,_tmpTimestamp,_tmpReceiptNumber);
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
  public Object getTotalSpentInRange(final long clientUserId, final long startTime,
      final long endTime, final Continuation<? super Double> $completion) {
    final String _sql = "SELECT SUM(amount) FROM purchases WHERE clientUserId = ? AND timestamp >= ? AND timestamp < ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, clientUserId);
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
  public Object getTotalAllTime(final long clientUserId,
      final Continuation<? super Double> $completion) {
    final String _sql = "SELECT SUM(amount) FROM purchases WHERE clientUserId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, clientUserId);
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
  public Object spendingByCategory(final long clientUserId, final long from, final long to,
      final Continuation<? super List<CategoryRevenue>> $completion) {
    final String _sql = "\n"
            + "        SELECT p.category, SUM(pu.amount) as total\n"
            + "        FROM purchases pu\n"
            + "        INNER JOIN products p ON pu.productId = p.id\n"
            + "        WHERE pu.clientUserId = ?\n"
            + "          AND pu.timestamp BETWEEN ? AND ?\n"
            + "        GROUP BY p.category\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, clientUserId);
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
  public Object spendingByBusiness(final long clientUserId, final long from, final long to,
      final Continuation<? super List<BusinessSpend>> $completion) {
    final String _sql = "\n"
            + "        SELECT businessId, SUM(amount) as total\n"
            + "        FROM purchases\n"
            + "        WHERE clientUserId = ?\n"
            + "          AND timestamp BETWEEN ? AND ?\n"
            + "        GROUP BY businessId\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, clientUserId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, from);
    _argIndex = 3;
    _statement.bindLong(_argIndex, to);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<BusinessSpend>>() {
      @Override
      @NonNull
      public List<BusinessSpend> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfBusinessId = 0;
          final int _cursorIndexOfTotal = 1;
          final List<BusinessSpend> _result = new ArrayList<BusinessSpend>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BusinessSpend _item;
            final long _tmpBusinessId;
            _tmpBusinessId = _cursor.getLong(_cursorIndexOfBusinessId);
            final double _tmpTotal;
            _tmpTotal = _cursor.getDouble(_cursorIndexOfTotal);
            _item = new BusinessSpend(_tmpBusinessId,_tmpTotal);
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
