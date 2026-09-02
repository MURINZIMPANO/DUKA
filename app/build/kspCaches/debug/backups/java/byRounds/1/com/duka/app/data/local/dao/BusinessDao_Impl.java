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
import com.duka.app.data.local.entity.Business;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class BusinessDao_Impl implements BusinessDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Business> __insertionAdapterOfBusiness;

  public BusinessDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBusiness = new EntityInsertionAdapter<Business>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `businesses` (`id`,`name`,`type`,`employeeCount`,`language`,`district`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Business entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getType());
        statement.bindLong(4, entity.getEmployeeCount());
        statement.bindString(5, entity.getLanguage());
        statement.bindString(6, entity.getDistrict());
        statement.bindLong(7, entity.getCreatedAt());
      }
    };
  }

  @Override
  public Object insert(final Business business, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfBusiness.insertAndReturnId(business);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getById(final long id, final Continuation<? super Business> $completion) {
    final String _sql = "SELECT * FROM businesses WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Business>() {
      @Override
      @Nullable
      public Business call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfEmployeeCount = CursorUtil.getColumnIndexOrThrow(_cursor, "employeeCount");
          final int _cursorIndexOfLanguage = CursorUtil.getColumnIndexOrThrow(_cursor, "language");
          final int _cursorIndexOfDistrict = CursorUtil.getColumnIndexOrThrow(_cursor, "district");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final Business _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final int _tmpEmployeeCount;
            _tmpEmployeeCount = _cursor.getInt(_cursorIndexOfEmployeeCount);
            final String _tmpLanguage;
            _tmpLanguage = _cursor.getString(_cursorIndexOfLanguage);
            final String _tmpDistrict;
            _tmpDistrict = _cursor.getString(_cursorIndexOfDistrict);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new Business(_tmpId,_tmpName,_tmpType,_tmpEmployeeCount,_tmpLanguage,_tmpDistrict,_tmpCreatedAt);
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
  public Flow<Business> getActiveBusiness() {
    final String _sql = "SELECT * FROM businesses LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"businesses"}, new Callable<Business>() {
      @Override
      @Nullable
      public Business call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfEmployeeCount = CursorUtil.getColumnIndexOrThrow(_cursor, "employeeCount");
          final int _cursorIndexOfLanguage = CursorUtil.getColumnIndexOrThrow(_cursor, "language");
          final int _cursorIndexOfDistrict = CursorUtil.getColumnIndexOrThrow(_cursor, "district");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final Business _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final int _tmpEmployeeCount;
            _tmpEmployeeCount = _cursor.getInt(_cursorIndexOfEmployeeCount);
            final String _tmpLanguage;
            _tmpLanguage = _cursor.getString(_cursorIndexOfLanguage);
            final String _tmpDistrict;
            _tmpDistrict = _cursor.getString(_cursorIndexOfDistrict);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new Business(_tmpId,_tmpName,_tmpType,_tmpEmployeeCount,_tmpLanguage,_tmpDistrict,_tmpCreatedAt);
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
  public Object getActiveBusinessOnce(final Continuation<? super Business> $completion) {
    final String _sql = "SELECT * FROM businesses LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Business>() {
      @Override
      @Nullable
      public Business call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfEmployeeCount = CursorUtil.getColumnIndexOrThrow(_cursor, "employeeCount");
          final int _cursorIndexOfLanguage = CursorUtil.getColumnIndexOrThrow(_cursor, "language");
          final int _cursorIndexOfDistrict = CursorUtil.getColumnIndexOrThrow(_cursor, "district");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final Business _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final int _tmpEmployeeCount;
            _tmpEmployeeCount = _cursor.getInt(_cursorIndexOfEmployeeCount);
            final String _tmpLanguage;
            _tmpLanguage = _cursor.getString(_cursorIndexOfLanguage);
            final String _tmpDistrict;
            _tmpDistrict = _cursor.getString(_cursorIndexOfDistrict);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new Business(_tmpId,_tmpName,_tmpType,_tmpEmployeeCount,_tmpLanguage,_tmpDistrict,_tmpCreatedAt);
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
