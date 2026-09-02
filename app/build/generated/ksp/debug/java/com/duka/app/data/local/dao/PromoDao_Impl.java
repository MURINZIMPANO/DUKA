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
import com.duka.app.data.local.entity.Promo;
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
public final class PromoDao_Impl implements PromoDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Promo> __insertionAdapterOfPromo;

  public PromoDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPromo = new EntityInsertionAdapter<Promo>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `promos` (`id`,`businessId`,`title`,`discountPercent`,`startDate`,`endDate`,`isLive`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Promo entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getBusinessId());
        statement.bindString(3, entity.getTitle());
        statement.bindLong(4, entity.getDiscountPercent());
        statement.bindLong(5, entity.getStartDate());
        statement.bindLong(6, entity.getEndDate());
        final int _tmp = entity.isLive() ? 1 : 0;
        statement.bindLong(7, _tmp);
      }
    };
  }

  @Override
  public Object insert(final Promo promo, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfPromo.insertAndReturnId(promo);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Promo>> getPromosByBusiness(final long businessId) {
    final String _sql = "SELECT * FROM promos WHERE businessId = ? ORDER BY startDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, businessId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"promos"}, new Callable<List<Promo>>() {
      @Override
      @NonNull
      public List<Promo> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBusinessId = CursorUtil.getColumnIndexOrThrow(_cursor, "businessId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDiscountPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "discountPercent");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfIsLive = CursorUtil.getColumnIndexOrThrow(_cursor, "isLive");
          final List<Promo> _result = new ArrayList<Promo>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Promo _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpBusinessId;
            _tmpBusinessId = _cursor.getLong(_cursorIndexOfBusinessId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final int _tmpDiscountPercent;
            _tmpDiscountPercent = _cursor.getInt(_cursorIndexOfDiscountPercent);
            final long _tmpStartDate;
            _tmpStartDate = _cursor.getLong(_cursorIndexOfStartDate);
            final long _tmpEndDate;
            _tmpEndDate = _cursor.getLong(_cursorIndexOfEndDate);
            final boolean _tmpIsLive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsLive);
            _tmpIsLive = _tmp != 0;
            _item = new Promo(_tmpId,_tmpBusinessId,_tmpTitle,_tmpDiscountPercent,_tmpStartDate,_tmpEndDate,_tmpIsLive);
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
  public Flow<List<Promo>> getLivePromos(final long businessId) {
    final String _sql = "SELECT * FROM promos WHERE businessId = ? AND isLive = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, businessId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"promos"}, new Callable<List<Promo>>() {
      @Override
      @NonNull
      public List<Promo> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBusinessId = CursorUtil.getColumnIndexOrThrow(_cursor, "businessId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDiscountPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "discountPercent");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfIsLive = CursorUtil.getColumnIndexOrThrow(_cursor, "isLive");
          final List<Promo> _result = new ArrayList<Promo>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Promo _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpBusinessId;
            _tmpBusinessId = _cursor.getLong(_cursorIndexOfBusinessId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final int _tmpDiscountPercent;
            _tmpDiscountPercent = _cursor.getInt(_cursorIndexOfDiscountPercent);
            final long _tmpStartDate;
            _tmpStartDate = _cursor.getLong(_cursorIndexOfStartDate);
            final long _tmpEndDate;
            _tmpEndDate = _cursor.getLong(_cursorIndexOfEndDate);
            final boolean _tmpIsLive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsLive);
            _tmpIsLive = _tmp != 0;
            _item = new Promo(_tmpId,_tmpBusinessId,_tmpTitle,_tmpDiscountPercent,_tmpStartDate,_tmpEndDate,_tmpIsLive);
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
