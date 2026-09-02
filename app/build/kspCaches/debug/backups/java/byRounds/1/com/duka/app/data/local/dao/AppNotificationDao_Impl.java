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
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.duka.app.data.local.entity.AppNotification;
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
public final class AppNotificationDao_Impl implements AppNotificationDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AppNotification> __insertionAdapterOfAppNotification;

  private final SharedSQLiteStatement __preparedStmtOfMarkAsRead;

  private final SharedSQLiteStatement __preparedStmtOfMarkAllAsRead;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  public AppNotificationDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAppNotification = new EntityInsertionAdapter<AppNotification>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `app_notifications` (`id`,`userId`,`businessId`,`type`,`title`,`body`,`actionRoute`,`isRead`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AppNotification entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUserId());
        statement.bindLong(3, entity.getBusinessId());
        statement.bindString(4, entity.getType());
        statement.bindString(5, entity.getTitle());
        statement.bindString(6, entity.getBody());
        statement.bindString(7, entity.getActionRoute());
        final int _tmp = entity.isRead() ? 1 : 0;
        statement.bindLong(8, _tmp);
        statement.bindLong(9, entity.getCreatedAt());
      }
    };
    this.__preparedStmtOfMarkAsRead = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE app_notifications SET isRead = 1 WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfMarkAllAsRead = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE app_notifications SET isRead = 1 WHERE userId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM app_notifications WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final AppNotification notification,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfAppNotification.insertAndReturnId(notification);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object markAsRead(final long notificationId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkAsRead.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, notificationId);
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
          __preparedStmtOfMarkAsRead.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object markAllAsRead(final long userId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkAllAsRead.acquire();
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
          __preparedStmtOfMarkAllAsRead.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final long notificationId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, notificationId);
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
          __preparedStmtOfDeleteById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<AppNotification>> getByUser(final long userId) {
    final String _sql = "SELECT * FROM app_notifications WHERE userId = ? ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"app_notifications"}, new Callable<List<AppNotification>>() {
      @Override
      @NonNull
      public List<AppNotification> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfBusinessId = CursorUtil.getColumnIndexOrThrow(_cursor, "businessId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfBody = CursorUtil.getColumnIndexOrThrow(_cursor, "body");
          final int _cursorIndexOfActionRoute = CursorUtil.getColumnIndexOrThrow(_cursor, "actionRoute");
          final int _cursorIndexOfIsRead = CursorUtil.getColumnIndexOrThrow(_cursor, "isRead");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<AppNotification> _result = new ArrayList<AppNotification>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AppNotification _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final long _tmpBusinessId;
            _tmpBusinessId = _cursor.getLong(_cursorIndexOfBusinessId);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpBody;
            _tmpBody = _cursor.getString(_cursorIndexOfBody);
            final String _tmpActionRoute;
            _tmpActionRoute = _cursor.getString(_cursorIndexOfActionRoute);
            final boolean _tmpIsRead;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsRead);
            _tmpIsRead = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new AppNotification(_tmpId,_tmpUserId,_tmpBusinessId,_tmpType,_tmpTitle,_tmpBody,_tmpActionRoute,_tmpIsRead,_tmpCreatedAt);
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
  public Flow<List<AppNotification>> getUnreadByUser(final long userId) {
    final String _sql = "SELECT * FROM app_notifications WHERE userId = ? AND isRead = 0 ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"app_notifications"}, new Callable<List<AppNotification>>() {
      @Override
      @NonNull
      public List<AppNotification> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfBusinessId = CursorUtil.getColumnIndexOrThrow(_cursor, "businessId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfBody = CursorUtil.getColumnIndexOrThrow(_cursor, "body");
          final int _cursorIndexOfActionRoute = CursorUtil.getColumnIndexOrThrow(_cursor, "actionRoute");
          final int _cursorIndexOfIsRead = CursorUtil.getColumnIndexOrThrow(_cursor, "isRead");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<AppNotification> _result = new ArrayList<AppNotification>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AppNotification _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final long _tmpBusinessId;
            _tmpBusinessId = _cursor.getLong(_cursorIndexOfBusinessId);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpBody;
            _tmpBody = _cursor.getString(_cursorIndexOfBody);
            final String _tmpActionRoute;
            _tmpActionRoute = _cursor.getString(_cursorIndexOfActionRoute);
            final boolean _tmpIsRead;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsRead);
            _tmpIsRead = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new AppNotification(_tmpId,_tmpUserId,_tmpBusinessId,_tmpType,_tmpTitle,_tmpBody,_tmpActionRoute,_tmpIsRead,_tmpCreatedAt);
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
  public Flow<Integer> getUnreadCount(final long userId) {
    final String _sql = "SELECT COUNT(*) FROM app_notifications WHERE userId = ? AND isRead = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"app_notifications"}, new Callable<Integer>() {
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
  public Flow<List<AppNotification>> getByUserAndTypes(final long userId,
      final List<String> types) {
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT * FROM app_notifications WHERE userId = ");
    _stringBuilder.append("?");
    _stringBuilder.append(" AND type IN (");
    final int _inputSize = types.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(") ORDER BY createdAt DESC");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 1 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    _argIndex = 2;
    for (String _item : types) {
      _statement.bindString(_argIndex, _item);
      _argIndex++;
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"app_notifications"}, new Callable<List<AppNotification>>() {
      @Override
      @NonNull
      public List<AppNotification> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfBusinessId = CursorUtil.getColumnIndexOrThrow(_cursor, "businessId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfBody = CursorUtil.getColumnIndexOrThrow(_cursor, "body");
          final int _cursorIndexOfActionRoute = CursorUtil.getColumnIndexOrThrow(_cursor, "actionRoute");
          final int _cursorIndexOfIsRead = CursorUtil.getColumnIndexOrThrow(_cursor, "isRead");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<AppNotification> _result = new ArrayList<AppNotification>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AppNotification _item_1;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final long _tmpBusinessId;
            _tmpBusinessId = _cursor.getLong(_cursorIndexOfBusinessId);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpBody;
            _tmpBody = _cursor.getString(_cursorIndexOfBody);
            final String _tmpActionRoute;
            _tmpActionRoute = _cursor.getString(_cursorIndexOfActionRoute);
            final boolean _tmpIsRead;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsRead);
            _tmpIsRead = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item_1 = new AppNotification(_tmpId,_tmpUserId,_tmpBusinessId,_tmpType,_tmpTitle,_tmpBody,_tmpActionRoute,_tmpIsRead,_tmpCreatedAt);
            _result.add(_item_1);
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
