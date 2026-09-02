package com.duka.app.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.duka.app.data.local.entity.User;
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
public final class UserDao_Impl implements UserDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<User> __insertionAdapterOfUser;

  private final SharedSQLiteStatement __preparedStmtOfUpdateName;

  private final SharedSQLiteStatement __preparedStmtOfSoftDelete;

  public UserDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUser = new EntityInsertionAdapter<User>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `users` (`id`,`businessId`,`fullName`,`phoneOrEmail`,`passwordHash`,`role`,`isDeleted`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final User entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getBusinessId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getBusinessId());
        }
        statement.bindString(3, entity.getFullName());
        statement.bindString(4, entity.getPhoneOrEmail());
        statement.bindString(5, entity.getPasswordHash());
        statement.bindString(6, entity.getRole());
        final int _tmp = entity.isDeleted() ? 1 : 0;
        statement.bindLong(7, _tmp);
        statement.bindLong(8, entity.getCreatedAt());
      }
    };
    this.__preparedStmtOfUpdateName = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE users SET fullName = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSoftDelete = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE users SET isDeleted = 1 WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final User user, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfUser.insertAndReturnId(user);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateName(final long userId, final String name,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateName.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, name);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, userId);
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
          __preparedStmtOfUpdateName.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object softDelete(final long userId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSoftDelete.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, userId);
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
  public Object getById(final long id, final Continuation<? super User> $completion) {
    final String _sql = "SELECT * FROM users WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<User>() {
      @Override
      @Nullable
      public User call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBusinessId = CursorUtil.getColumnIndexOrThrow(_cursor, "businessId");
          final int _cursorIndexOfFullName = CursorUtil.getColumnIndexOrThrow(_cursor, "fullName");
          final int _cursorIndexOfPhoneOrEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "phoneOrEmail");
          final int _cursorIndexOfPasswordHash = CursorUtil.getColumnIndexOrThrow(_cursor, "passwordHash");
          final int _cursorIndexOfRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final User _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Long _tmpBusinessId;
            if (_cursor.isNull(_cursorIndexOfBusinessId)) {
              _tmpBusinessId = null;
            } else {
              _tmpBusinessId = _cursor.getLong(_cursorIndexOfBusinessId);
            }
            final String _tmpFullName;
            _tmpFullName = _cursor.getString(_cursorIndexOfFullName);
            final String _tmpPhoneOrEmail;
            _tmpPhoneOrEmail = _cursor.getString(_cursorIndexOfPhoneOrEmail);
            final String _tmpPasswordHash;
            _tmpPasswordHash = _cursor.getString(_cursorIndexOfPasswordHash);
            final String _tmpRole;
            _tmpRole = _cursor.getString(_cursorIndexOfRole);
            final boolean _tmpIsDeleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new User(_tmpId,_tmpBusinessId,_tmpFullName,_tmpPhoneOrEmail,_tmpPasswordHash,_tmpRole,_tmpIsDeleted,_tmpCreatedAt);
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
  public Object getByPhoneOrEmail(final String phoneOrEmail,
      final Continuation<? super User> $completion) {
    final String _sql = "SELECT * FROM users WHERE phoneOrEmail = ? AND isDeleted = 0 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, phoneOrEmail);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<User>() {
      @Override
      @Nullable
      public User call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBusinessId = CursorUtil.getColumnIndexOrThrow(_cursor, "businessId");
          final int _cursorIndexOfFullName = CursorUtil.getColumnIndexOrThrow(_cursor, "fullName");
          final int _cursorIndexOfPhoneOrEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "phoneOrEmail");
          final int _cursorIndexOfPasswordHash = CursorUtil.getColumnIndexOrThrow(_cursor, "passwordHash");
          final int _cursorIndexOfRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final User _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Long _tmpBusinessId;
            if (_cursor.isNull(_cursorIndexOfBusinessId)) {
              _tmpBusinessId = null;
            } else {
              _tmpBusinessId = _cursor.getLong(_cursorIndexOfBusinessId);
            }
            final String _tmpFullName;
            _tmpFullName = _cursor.getString(_cursorIndexOfFullName);
            final String _tmpPhoneOrEmail;
            _tmpPhoneOrEmail = _cursor.getString(_cursorIndexOfPhoneOrEmail);
            final String _tmpPasswordHash;
            _tmpPasswordHash = _cursor.getString(_cursorIndexOfPasswordHash);
            final String _tmpRole;
            _tmpRole = _cursor.getString(_cursorIndexOfRole);
            final boolean _tmpIsDeleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new User(_tmpId,_tmpBusinessId,_tmpFullName,_tmpPhoneOrEmail,_tmpPasswordHash,_tmpRole,_tmpIsDeleted,_tmpCreatedAt);
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
  public Flow<List<User>> getEmployeesByBusiness(final long businessId) {
    final String _sql = "SELECT * FROM users WHERE businessId = ? AND role = 'employee'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, businessId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"users"}, new Callable<List<User>>() {
      @Override
      @NonNull
      public List<User> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBusinessId = CursorUtil.getColumnIndexOrThrow(_cursor, "businessId");
          final int _cursorIndexOfFullName = CursorUtil.getColumnIndexOrThrow(_cursor, "fullName");
          final int _cursorIndexOfPhoneOrEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "phoneOrEmail");
          final int _cursorIndexOfPasswordHash = CursorUtil.getColumnIndexOrThrow(_cursor, "passwordHash");
          final int _cursorIndexOfRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<User> _result = new ArrayList<User>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final User _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Long _tmpBusinessId;
            if (_cursor.isNull(_cursorIndexOfBusinessId)) {
              _tmpBusinessId = null;
            } else {
              _tmpBusinessId = _cursor.getLong(_cursorIndexOfBusinessId);
            }
            final String _tmpFullName;
            _tmpFullName = _cursor.getString(_cursorIndexOfFullName);
            final String _tmpPhoneOrEmail;
            _tmpPhoneOrEmail = _cursor.getString(_cursorIndexOfPhoneOrEmail);
            final String _tmpPasswordHash;
            _tmpPasswordHash = _cursor.getString(_cursorIndexOfPasswordHash);
            final String _tmpRole;
            _tmpRole = _cursor.getString(_cursorIndexOfRole);
            final boolean _tmpIsDeleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new User(_tmpId,_tmpBusinessId,_tmpFullName,_tmpPhoneOrEmail,_tmpPasswordHash,_tmpRole,_tmpIsDeleted,_tmpCreatedAt);
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
  public Object getEmployeesByBusinessOnce(final long businessId,
      final Continuation<? super List<User>> $completion) {
    final String _sql = "SELECT * FROM users WHERE businessId = ? AND role = 'employee'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, businessId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<User>>() {
      @Override
      @NonNull
      public List<User> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBusinessId = CursorUtil.getColumnIndexOrThrow(_cursor, "businessId");
          final int _cursorIndexOfFullName = CursorUtil.getColumnIndexOrThrow(_cursor, "fullName");
          final int _cursorIndexOfPhoneOrEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "phoneOrEmail");
          final int _cursorIndexOfPasswordHash = CursorUtil.getColumnIndexOrThrow(_cursor, "passwordHash");
          final int _cursorIndexOfRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<User> _result = new ArrayList<User>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final User _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Long _tmpBusinessId;
            if (_cursor.isNull(_cursorIndexOfBusinessId)) {
              _tmpBusinessId = null;
            } else {
              _tmpBusinessId = _cursor.getLong(_cursorIndexOfBusinessId);
            }
            final String _tmpFullName;
            _tmpFullName = _cursor.getString(_cursorIndexOfFullName);
            final String _tmpPhoneOrEmail;
            _tmpPhoneOrEmail = _cursor.getString(_cursorIndexOfPhoneOrEmail);
            final String _tmpPasswordHash;
            _tmpPasswordHash = _cursor.getString(_cursorIndexOfPasswordHash);
            final String _tmpRole;
            _tmpRole = _cursor.getString(_cursorIndexOfRole);
            final boolean _tmpIsDeleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new User(_tmpId,_tmpBusinessId,_tmpFullName,_tmpPhoneOrEmail,_tmpPasswordHash,_tmpRole,_tmpIsDeleted,_tmpCreatedAt);
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
  public Object getEmployeeByName(final long businessId, final String name,
      final Continuation<? super User> $completion) {
    final String _sql = "SELECT * FROM users WHERE businessId = ? AND role = 'employee' AND fullName = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, businessId);
    _argIndex = 2;
    _statement.bindString(_argIndex, name);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<User>() {
      @Override
      @Nullable
      public User call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBusinessId = CursorUtil.getColumnIndexOrThrow(_cursor, "businessId");
          final int _cursorIndexOfFullName = CursorUtil.getColumnIndexOrThrow(_cursor, "fullName");
          final int _cursorIndexOfPhoneOrEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "phoneOrEmail");
          final int _cursorIndexOfPasswordHash = CursorUtil.getColumnIndexOrThrow(_cursor, "passwordHash");
          final int _cursorIndexOfRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final User _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Long _tmpBusinessId;
            if (_cursor.isNull(_cursorIndexOfBusinessId)) {
              _tmpBusinessId = null;
            } else {
              _tmpBusinessId = _cursor.getLong(_cursorIndexOfBusinessId);
            }
            final String _tmpFullName;
            _tmpFullName = _cursor.getString(_cursorIndexOfFullName);
            final String _tmpPhoneOrEmail;
            _tmpPhoneOrEmail = _cursor.getString(_cursorIndexOfPhoneOrEmail);
            final String _tmpPasswordHash;
            _tmpPasswordHash = _cursor.getString(_cursorIndexOfPasswordHash);
            final String _tmpRole;
            _tmpRole = _cursor.getString(_cursorIndexOfRole);
            final boolean _tmpIsDeleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new User(_tmpId,_tmpBusinessId,_tmpFullName,_tmpPhoneOrEmail,_tmpPasswordHash,_tmpRole,_tmpIsDeleted,_tmpCreatedAt);
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
