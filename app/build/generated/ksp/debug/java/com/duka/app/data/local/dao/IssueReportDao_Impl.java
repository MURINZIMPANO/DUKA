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
import com.duka.app.data.local.entity.IssueReport;
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
public final class IssueReportDao_Impl implements IssueReportDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<IssueReport> __insertionAdapterOfIssueReport;

  public IssueReportDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfIssueReport = new EntityInsertionAdapter<IssueReport>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `issue_reports` (`id`,`businessId`,`category`,`message`,`attachBusinessId`,`status`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final IssueReport entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getBusinessId());
        statement.bindString(3, entity.getCategory());
        statement.bindString(4, entity.getMessage());
        final int _tmp = entity.getAttachBusinessId() ? 1 : 0;
        statement.bindLong(5, _tmp);
        statement.bindString(6, entity.getStatus());
        statement.bindLong(7, entity.getCreatedAt());
      }
    };
  }

  @Override
  public Object insert(final IssueReport report, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfIssueReport.insertAndReturnId(report);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<IssueReport>> getReportsByBusiness(final long businessId) {
    final String _sql = "SELECT * FROM issue_reports WHERE businessId = ? ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, businessId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"issue_reports"}, new Callable<List<IssueReport>>() {
      @Override
      @NonNull
      public List<IssueReport> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBusinessId = CursorUtil.getColumnIndexOrThrow(_cursor, "businessId");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "message");
          final int _cursorIndexOfAttachBusinessId = CursorUtil.getColumnIndexOrThrow(_cursor, "attachBusinessId");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<IssueReport> _result = new ArrayList<IssueReport>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final IssueReport _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpBusinessId;
            _tmpBusinessId = _cursor.getLong(_cursorIndexOfBusinessId);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpMessage;
            _tmpMessage = _cursor.getString(_cursorIndexOfMessage);
            final boolean _tmpAttachBusinessId;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfAttachBusinessId);
            _tmpAttachBusinessId = _tmp != 0;
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new IssueReport(_tmpId,_tmpBusinessId,_tmpCategory,_tmpMessage,_tmpAttachBusinessId,_tmpStatus,_tmpCreatedAt);
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
