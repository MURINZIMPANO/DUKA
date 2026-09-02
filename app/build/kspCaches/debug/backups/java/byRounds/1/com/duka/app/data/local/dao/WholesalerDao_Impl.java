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
import com.duka.app.data.local.entity.Wholesaler;
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
public final class WholesalerDao_Impl implements WholesalerDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Wholesaler> __insertionAdapterOfWholesaler;

  public WholesalerDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfWholesaler = new EntityInsertionAdapter<Wholesaler>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `wholesalers` (`id`,`name`,`specialty`,`rating`,`deliveryEstimate`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Wholesaler entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getSpecialty());
        statement.bindDouble(4, entity.getRating());
        statement.bindString(5, entity.getDeliveryEstimate());
      }
    };
  }

  @Override
  public Object insert(final Wholesaler wholesaler, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfWholesaler.insertAndReturnId(wholesaler);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Wholesaler>> getAll() {
    final String _sql = "SELECT * FROM wholesalers ORDER BY rating DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"wholesalers"}, new Callable<List<Wholesaler>>() {
      @Override
      @NonNull
      public List<Wholesaler> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfSpecialty = CursorUtil.getColumnIndexOrThrow(_cursor, "specialty");
          final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
          final int _cursorIndexOfDeliveryEstimate = CursorUtil.getColumnIndexOrThrow(_cursor, "deliveryEstimate");
          final List<Wholesaler> _result = new ArrayList<Wholesaler>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Wholesaler _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpSpecialty;
            _tmpSpecialty = _cursor.getString(_cursorIndexOfSpecialty);
            final double _tmpRating;
            _tmpRating = _cursor.getDouble(_cursorIndexOfRating);
            final String _tmpDeliveryEstimate;
            _tmpDeliveryEstimate = _cursor.getString(_cursorIndexOfDeliveryEstimate);
            _item = new Wholesaler(_tmpId,_tmpName,_tmpSpecialty,_tmpRating,_tmpDeliveryEstimate);
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
  public Flow<List<Wholesaler>> search(final String query) {
    final String _sql = "SELECT * FROM wholesalers WHERE name LIKE '%' || ? || '%' OR specialty LIKE '%' || ? || '%'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, query);
    _argIndex = 2;
    _statement.bindString(_argIndex, query);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"wholesalers"}, new Callable<List<Wholesaler>>() {
      @Override
      @NonNull
      public List<Wholesaler> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfSpecialty = CursorUtil.getColumnIndexOrThrow(_cursor, "specialty");
          final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
          final int _cursorIndexOfDeliveryEstimate = CursorUtil.getColumnIndexOrThrow(_cursor, "deliveryEstimate");
          final List<Wholesaler> _result = new ArrayList<Wholesaler>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Wholesaler _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpSpecialty;
            _tmpSpecialty = _cursor.getString(_cursorIndexOfSpecialty);
            final double _tmpRating;
            _tmpRating = _cursor.getDouble(_cursorIndexOfRating);
            final String _tmpDeliveryEstimate;
            _tmpDeliveryEstimate = _cursor.getString(_cursorIndexOfDeliveryEstimate);
            _item = new Wholesaler(_tmpId,_tmpName,_tmpSpecialty,_tmpRating,_tmpDeliveryEstimate);
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
  public Object getById(final long id, final Continuation<? super Wholesaler> $completion) {
    final String _sql = "SELECT * FROM wholesalers WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Wholesaler>() {
      @Override
      @Nullable
      public Wholesaler call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfSpecialty = CursorUtil.getColumnIndexOrThrow(_cursor, "specialty");
          final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
          final int _cursorIndexOfDeliveryEstimate = CursorUtil.getColumnIndexOrThrow(_cursor, "deliveryEstimate");
          final Wholesaler _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpSpecialty;
            _tmpSpecialty = _cursor.getString(_cursorIndexOfSpecialty);
            final double _tmpRating;
            _tmpRating = _cursor.getDouble(_cursorIndexOfRating);
            final String _tmpDeliveryEstimate;
            _tmpDeliveryEstimate = _cursor.getString(_cursorIndexOfDeliveryEstimate);
            _result = new Wholesaler(_tmpId,_tmpName,_tmpSpecialty,_tmpRating,_tmpDeliveryEstimate);
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
