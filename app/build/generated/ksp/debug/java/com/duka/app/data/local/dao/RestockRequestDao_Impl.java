package com.duka.app.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.duka.app.data.local.entity.RestockRequest;
import java.lang.Class;
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
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class RestockRequestDao_Impl implements RestockRequestDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RestockRequest> __insertionAdapterOfRestockRequest;

  private final SharedSQLiteStatement __preparedStmtOfUpdateStatus;

  public RestockRequestDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRestockRequest = new EntityInsertionAdapter<RestockRequest>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `restock_requests` (`id`,`businessId`,`productId`,`wholesalerId`,`quantity`,`status`,`requestedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RestockRequest entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getBusinessId());
        statement.bindLong(3, entity.getProductId());
        statement.bindLong(4, entity.getWholesalerId());
        statement.bindLong(5, entity.getQuantity());
        statement.bindString(6, entity.getStatus());
        statement.bindLong(7, entity.getRequestedAt());
      }
    };
    this.__preparedStmtOfUpdateStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE restock_requests SET status = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final RestockRequest request, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfRestockRequest.insertAndReturnId(request);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateStatus(final long requestId, final String status,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateStatus.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, status);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, requestId);
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
          __preparedStmtOfUpdateStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<RestockRequest>> getByBusiness(final long businessId) {
    final String _sql = "SELECT * FROM restock_requests WHERE businessId = ? ORDER BY requestedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, businessId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"restock_requests"}, new Callable<List<RestockRequest>>() {
      @Override
      @NonNull
      public List<RestockRequest> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBusinessId = CursorUtil.getColumnIndexOrThrow(_cursor, "businessId");
          final int _cursorIndexOfProductId = CursorUtil.getColumnIndexOrThrow(_cursor, "productId");
          final int _cursorIndexOfWholesalerId = CursorUtil.getColumnIndexOrThrow(_cursor, "wholesalerId");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfRequestedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "requestedAt");
          final List<RestockRequest> _result = new ArrayList<RestockRequest>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RestockRequest _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpBusinessId;
            _tmpBusinessId = _cursor.getLong(_cursorIndexOfBusinessId);
            final long _tmpProductId;
            _tmpProductId = _cursor.getLong(_cursorIndexOfProductId);
            final long _tmpWholesalerId;
            _tmpWholesalerId = _cursor.getLong(_cursorIndexOfWholesalerId);
            final int _tmpQuantity;
            _tmpQuantity = _cursor.getInt(_cursorIndexOfQuantity);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final long _tmpRequestedAt;
            _tmpRequestedAt = _cursor.getLong(_cursorIndexOfRequestedAt);
            _item = new RestockRequest(_tmpId,_tmpBusinessId,_tmpProductId,_tmpWholesalerId,_tmpQuantity,_tmpStatus,_tmpRequestedAt);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
