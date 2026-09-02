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
import com.duka.app.data.local.entity.StockAdjustment;
import java.lang.Class;
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
public final class StockAdjustmentDao_Impl implements StockAdjustmentDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<StockAdjustment> __insertionAdapterOfStockAdjustment;

  public StockAdjustmentDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfStockAdjustment = new EntityInsertionAdapter<StockAdjustment>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `stock_adjustments` (`id`,`productId`,`businessId`,`delta`,`reason`,`timestamp`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final StockAdjustment entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getProductId());
        statement.bindLong(3, entity.getBusinessId());
        statement.bindLong(4, entity.getDelta());
        statement.bindString(5, entity.getReason());
        statement.bindLong(6, entity.getTimestamp());
      }
    };
  }

  @Override
  public Object insert(final StockAdjustment adjustment,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfStockAdjustment.insertAndReturnId(adjustment);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<StockAdjustment>> getByBusiness(final long businessId) {
    final String _sql = "SELECT * FROM stock_adjustments WHERE businessId = ? ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, businessId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"stock_adjustments"}, new Callable<List<StockAdjustment>>() {
      @Override
      @NonNull
      public List<StockAdjustment> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProductId = CursorUtil.getColumnIndexOrThrow(_cursor, "productId");
          final int _cursorIndexOfBusinessId = CursorUtil.getColumnIndexOrThrow(_cursor, "businessId");
          final int _cursorIndexOfDelta = CursorUtil.getColumnIndexOrThrow(_cursor, "delta");
          final int _cursorIndexOfReason = CursorUtil.getColumnIndexOrThrow(_cursor, "reason");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<StockAdjustment> _result = new ArrayList<StockAdjustment>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final StockAdjustment _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpProductId;
            _tmpProductId = _cursor.getLong(_cursorIndexOfProductId);
            final long _tmpBusinessId;
            _tmpBusinessId = _cursor.getLong(_cursorIndexOfBusinessId);
            final int _tmpDelta;
            _tmpDelta = _cursor.getInt(_cursorIndexOfDelta);
            final String _tmpReason;
            _tmpReason = _cursor.getString(_cursorIndexOfReason);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new StockAdjustment(_tmpId,_tmpProductId,_tmpBusinessId,_tmpDelta,_tmpReason,_tmpTimestamp);
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
  public Flow<List<StockAdjustment>> getByProduct(final long productId) {
    final String _sql = "SELECT * FROM stock_adjustments WHERE productId = ? ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, productId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"stock_adjustments"}, new Callable<List<StockAdjustment>>() {
      @Override
      @NonNull
      public List<StockAdjustment> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProductId = CursorUtil.getColumnIndexOrThrow(_cursor, "productId");
          final int _cursorIndexOfBusinessId = CursorUtil.getColumnIndexOrThrow(_cursor, "businessId");
          final int _cursorIndexOfDelta = CursorUtil.getColumnIndexOrThrow(_cursor, "delta");
          final int _cursorIndexOfReason = CursorUtil.getColumnIndexOrThrow(_cursor, "reason");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<StockAdjustment> _result = new ArrayList<StockAdjustment>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final StockAdjustment _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpProductId;
            _tmpProductId = _cursor.getLong(_cursorIndexOfProductId);
            final long _tmpBusinessId;
            _tmpBusinessId = _cursor.getLong(_cursorIndexOfBusinessId);
            final int _tmpDelta;
            _tmpDelta = _cursor.getInt(_cursorIndexOfDelta);
            final String _tmpReason;
            _tmpReason = _cursor.getString(_cursorIndexOfReason);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new StockAdjustment(_tmpId,_tmpProductId,_tmpBusinessId,_tmpDelta,_tmpReason,_tmpTimestamp);
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
  public Object getByBusinessInRange(final long businessId, final long startTime,
      final long endTime, final Continuation<? super List<StockAdjustment>> $completion) {
    final String _sql = "SELECT * FROM stock_adjustments WHERE businessId = ? AND timestamp >= ? AND timestamp < ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, businessId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, startTime);
    _argIndex = 3;
    _statement.bindLong(_argIndex, endTime);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<StockAdjustment>>() {
      @Override
      @NonNull
      public List<StockAdjustment> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProductId = CursorUtil.getColumnIndexOrThrow(_cursor, "productId");
          final int _cursorIndexOfBusinessId = CursorUtil.getColumnIndexOrThrow(_cursor, "businessId");
          final int _cursorIndexOfDelta = CursorUtil.getColumnIndexOrThrow(_cursor, "delta");
          final int _cursorIndexOfReason = CursorUtil.getColumnIndexOrThrow(_cursor, "reason");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<StockAdjustment> _result = new ArrayList<StockAdjustment>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final StockAdjustment _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpProductId;
            _tmpProductId = _cursor.getLong(_cursorIndexOfProductId);
            final long _tmpBusinessId;
            _tmpBusinessId = _cursor.getLong(_cursorIndexOfBusinessId);
            final int _tmpDelta;
            _tmpDelta = _cursor.getInt(_cursorIndexOfDelta);
            final String _tmpReason;
            _tmpReason = _cursor.getString(_cursorIndexOfReason);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new StockAdjustment(_tmpId,_tmpProductId,_tmpBusinessId,_tmpDelta,_tmpReason,_tmpTimestamp);
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
  public Object getUnitsSoldInRange(final long productId, final long startTime, final long endTime,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT SUM(delta) FROM stock_adjustments WHERE productId = ? AND reason = 'sale' AND timestamp >= ? AND timestamp < ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, productId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, startTime);
    _argIndex = 3;
    _statement.bindLong(_argIndex, endTime);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @Nullable
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
