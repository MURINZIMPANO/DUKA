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
import com.duka.app.data.local.entity.ShopRating;
import java.lang.Class;
import java.lang.Exception;
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
public final class ShopRatingDao_Impl implements ShopRatingDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ShopRating> __insertionAdapterOfShopRating;

  public ShopRatingDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfShopRating = new EntityInsertionAdapter<ShopRating>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `shop_ratings` (`id`,`businessId`,`averageRating`,`ratingCount`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ShopRating entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getBusinessId());
        statement.bindDouble(3, entity.getAverageRating());
        statement.bindLong(4, entity.getRatingCount());
      }
    };
  }

  @Override
  public Object upsert(final ShopRating rating, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfShopRating.insert(rating);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getByBusiness(final long businessId,
      final Continuation<? super ShopRating> $completion) {
    final String _sql = "SELECT * FROM shop_ratings WHERE businessId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, businessId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ShopRating>() {
      @Override
      @Nullable
      public ShopRating call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBusinessId = CursorUtil.getColumnIndexOrThrow(_cursor, "businessId");
          final int _cursorIndexOfAverageRating = CursorUtil.getColumnIndexOrThrow(_cursor, "averageRating");
          final int _cursorIndexOfRatingCount = CursorUtil.getColumnIndexOrThrow(_cursor, "ratingCount");
          final ShopRating _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpBusinessId;
            _tmpBusinessId = _cursor.getLong(_cursorIndexOfBusinessId);
            final double _tmpAverageRating;
            _tmpAverageRating = _cursor.getDouble(_cursorIndexOfAverageRating);
            final int _tmpRatingCount;
            _tmpRatingCount = _cursor.getInt(_cursorIndexOfRatingCount);
            _result = new ShopRating(_tmpId,_tmpBusinessId,_tmpAverageRating,_tmpRatingCount);
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
  public Flow<List<ShopRating>> getAllRanked() {
    final String _sql = "SELECT * FROM shop_ratings ORDER BY averageRating DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"shop_ratings"}, new Callable<List<ShopRating>>() {
      @Override
      @NonNull
      public List<ShopRating> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBusinessId = CursorUtil.getColumnIndexOrThrow(_cursor, "businessId");
          final int _cursorIndexOfAverageRating = CursorUtil.getColumnIndexOrThrow(_cursor, "averageRating");
          final int _cursorIndexOfRatingCount = CursorUtil.getColumnIndexOrThrow(_cursor, "ratingCount");
          final List<ShopRating> _result = new ArrayList<ShopRating>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ShopRating _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpBusinessId;
            _tmpBusinessId = _cursor.getLong(_cursorIndexOfBusinessId);
            final double _tmpAverageRating;
            _tmpAverageRating = _cursor.getDouble(_cursorIndexOfAverageRating);
            final int _tmpRatingCount;
            _tmpRatingCount = _cursor.getInt(_cursorIndexOfRatingCount);
            _item = new ShopRating(_tmpId,_tmpBusinessId,_tmpAverageRating,_tmpRatingCount);
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
