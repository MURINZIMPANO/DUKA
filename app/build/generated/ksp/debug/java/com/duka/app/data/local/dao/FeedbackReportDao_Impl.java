package com.duka.app.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.duka.app.data.local.entity.FeedbackReport;
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
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class FeedbackReportDao_Impl implements FeedbackReportDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<FeedbackReport> __insertionAdapterOfFeedbackReport;

  public FeedbackReportDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFeedbackReport = new EntityInsertionAdapter<FeedbackReport>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `feedback_reports` (`id`,`clientUserId`,`targetBusinessId`,`category`,`message`,`isAnonymous`,`status`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FeedbackReport entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getClientUserId());
        statement.bindLong(3, entity.getTargetBusinessId());
        statement.bindString(4, entity.getCategory());
        statement.bindString(5, entity.getMessage());
        final int _tmp = entity.isAnonymous() ? 1 : 0;
        statement.bindLong(6, _tmp);
        statement.bindString(7, entity.getStatus());
        statement.bindLong(8, entity.getCreatedAt());
      }
    };
  }

  @Override
  public Object insert(final FeedbackReport report, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfFeedbackReport.insertAndReturnId(report);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<FeedbackReport>> getByClient(final long clientUserId) {
    final String _sql = "SELECT * FROM feedback_reports WHERE clientUserId = ? ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, clientUserId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"feedback_reports"}, new Callable<List<FeedbackReport>>() {
      @Override
      @NonNull
      public List<FeedbackReport> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfClientUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "clientUserId");
          final int _cursorIndexOfTargetBusinessId = CursorUtil.getColumnIndexOrThrow(_cursor, "targetBusinessId");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "message");
          final int _cursorIndexOfIsAnonymous = CursorUtil.getColumnIndexOrThrow(_cursor, "isAnonymous");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<FeedbackReport> _result = new ArrayList<FeedbackReport>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FeedbackReport _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpClientUserId;
            _tmpClientUserId = _cursor.getLong(_cursorIndexOfClientUserId);
            final long _tmpTargetBusinessId;
            _tmpTargetBusinessId = _cursor.getLong(_cursorIndexOfTargetBusinessId);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpMessage;
            _tmpMessage = _cursor.getString(_cursorIndexOfMessage);
            final boolean _tmpIsAnonymous;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsAnonymous);
            _tmpIsAnonymous = _tmp != 0;
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new FeedbackReport(_tmpId,_tmpClientUserId,_tmpTargetBusinessId,_tmpCategory,_tmpMessage,_tmpIsAnonymous,_tmpStatus,_tmpCreatedAt);
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
  public Flow<List<FeedbackReport>> getAll() {
    final String _sql = "SELECT * FROM feedback_reports ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"feedback_reports"}, new Callable<List<FeedbackReport>>() {
      @Override
      @NonNull
      public List<FeedbackReport> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfClientUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "clientUserId");
          final int _cursorIndexOfTargetBusinessId = CursorUtil.getColumnIndexOrThrow(_cursor, "targetBusinessId");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "message");
          final int _cursorIndexOfIsAnonymous = CursorUtil.getColumnIndexOrThrow(_cursor, "isAnonymous");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<FeedbackReport> _result = new ArrayList<FeedbackReport>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FeedbackReport _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpClientUserId;
            _tmpClientUserId = _cursor.getLong(_cursorIndexOfClientUserId);
            final long _tmpTargetBusinessId;
            _tmpTargetBusinessId = _cursor.getLong(_cursorIndexOfTargetBusinessId);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpMessage;
            _tmpMessage = _cursor.getString(_cursorIndexOfMessage);
            final boolean _tmpIsAnonymous;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsAnonymous);
            _tmpIsAnonymous = _tmp != 0;
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new FeedbackReport(_tmpId,_tmpClientUserId,_tmpTargetBusinessId,_tmpCategory,_tmpMessage,_tmpIsAnonymous,_tmpStatus,_tmpCreatedAt);
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
