package com.duka.app.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.duka.app.data.local.entity.TaxProfile;
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
public final class TaxProfileDao_Impl implements TaxProfileDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TaxProfile> __insertionAdapterOfTaxProfile;

  public TaxProfileDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTaxProfile = new EntityInsertionAdapter<TaxProfile>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `tax_profiles` (`businessId`,`quarterlyTurnover`,`tier`,`vatCollected`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TaxProfile entity) {
        statement.bindLong(1, entity.getBusinessId());
        statement.bindDouble(2, entity.getQuarterlyTurnover());
        statement.bindString(3, entity.getTier());
        statement.bindDouble(4, entity.getVatCollected());
      }
    };
  }

  @Override
  public Object upsert(final TaxProfile taxProfile, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTaxProfile.insert(taxProfile);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<TaxProfile> getTaxProfile(final long businessId) {
    final String _sql = "SELECT * FROM tax_profiles WHERE businessId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, businessId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"tax_profiles"}, new Callable<TaxProfile>() {
      @Override
      @Nullable
      public TaxProfile call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfBusinessId = CursorUtil.getColumnIndexOrThrow(_cursor, "businessId");
          final int _cursorIndexOfQuarterlyTurnover = CursorUtil.getColumnIndexOrThrow(_cursor, "quarterlyTurnover");
          final int _cursorIndexOfTier = CursorUtil.getColumnIndexOrThrow(_cursor, "tier");
          final int _cursorIndexOfVatCollected = CursorUtil.getColumnIndexOrThrow(_cursor, "vatCollected");
          final TaxProfile _result;
          if (_cursor.moveToFirst()) {
            final long _tmpBusinessId;
            _tmpBusinessId = _cursor.getLong(_cursorIndexOfBusinessId);
            final double _tmpQuarterlyTurnover;
            _tmpQuarterlyTurnover = _cursor.getDouble(_cursorIndexOfQuarterlyTurnover);
            final String _tmpTier;
            _tmpTier = _cursor.getString(_cursorIndexOfTier);
            final double _tmpVatCollected;
            _tmpVatCollected = _cursor.getDouble(_cursorIndexOfVatCollected);
            _result = new TaxProfile(_tmpBusinessId,_tmpQuarterlyTurnover,_tmpTier,_tmpVatCollected);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
