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
import com.duka.app.data.local.entity.BudgetGoal;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class BudgetGoalDao_Impl implements BudgetGoalDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<BudgetGoal> __insertionAdapterOfBudgetGoal;

  public BudgetGoalDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBudgetGoal = new EntityInsertionAdapter<BudgetGoal>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `budget_goals` (`id`,`clientUserId`,`weeklyLimit`,`createdAt`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BudgetGoal entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getClientUserId());
        statement.bindDouble(3, entity.getWeeklyLimit());
        statement.bindLong(4, entity.getCreatedAt());
      }
    };
  }

  @Override
  public Object upsert(final BudgetGoal budgetGoal, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBudgetGoal.insert(budgetGoal);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<BudgetGoal> getForClient(final long clientUserId) {
    final String _sql = "SELECT * FROM budget_goals WHERE clientUserId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, clientUserId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"budget_goals"}, new Callable<BudgetGoal>() {
      @Override
      @Nullable
      public BudgetGoal call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfClientUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "clientUserId");
          final int _cursorIndexOfWeeklyLimit = CursorUtil.getColumnIndexOrThrow(_cursor, "weeklyLimit");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final BudgetGoal _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpClientUserId;
            _tmpClientUserId = _cursor.getLong(_cursorIndexOfClientUserId);
            final double _tmpWeeklyLimit;
            _tmpWeeklyLimit = _cursor.getDouble(_cursorIndexOfWeeklyLimit);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new BudgetGoal(_tmpId,_tmpClientUserId,_tmpWeeklyLimit,_tmpCreatedAt);
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
  public Object getForClientOnce(final long clientUserId,
      final Continuation<? super BudgetGoal> $completion) {
    final String _sql = "SELECT * FROM budget_goals WHERE clientUserId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, clientUserId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<BudgetGoal>() {
      @Override
      @Nullable
      public BudgetGoal call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfClientUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "clientUserId");
          final int _cursorIndexOfWeeklyLimit = CursorUtil.getColumnIndexOrThrow(_cursor, "weeklyLimit");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final BudgetGoal _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpClientUserId;
            _tmpClientUserId = _cursor.getLong(_cursorIndexOfClientUserId);
            final double _tmpWeeklyLimit;
            _tmpWeeklyLimit = _cursor.getDouble(_cursorIndexOfWeeklyLimit);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new BudgetGoal(_tmpId,_tmpClientUserId,_tmpWeeklyLimit,_tmpCreatedAt);
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
