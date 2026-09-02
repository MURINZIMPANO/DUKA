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
import com.duka.app.data.local.entity.EbmReceipt;
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
public final class EbmReceiptDao_Impl implements EbmReceiptDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<EbmReceipt> __insertionAdapterOfEbmReceipt;

  public EbmReceiptDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfEbmReceipt = new EntityInsertionAdapter<EbmReceipt>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `ebm_receipts` (`id`,`saleId`,`receiptNumber`,`status`,`sentAt`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final EbmReceipt entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getSaleId());
        statement.bindString(3, entity.getReceiptNumber());
        statement.bindString(4, entity.getStatus());
        statement.bindLong(5, entity.getSentAt());
      }
    };
  }

  @Override
  public Object insert(final EbmReceipt receipt, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfEbmReceipt.insertAndReturnId(receipt);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getBySaleId(final long saleId, final Continuation<? super EbmReceipt> $completion) {
    final String _sql = "SELECT * FROM ebm_receipts WHERE saleId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, saleId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<EbmReceipt>() {
      @Override
      @Nullable
      public EbmReceipt call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSaleId = CursorUtil.getColumnIndexOrThrow(_cursor, "saleId");
          final int _cursorIndexOfReceiptNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "receiptNumber");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfSentAt = CursorUtil.getColumnIndexOrThrow(_cursor, "sentAt");
          final EbmReceipt _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpSaleId;
            _tmpSaleId = _cursor.getLong(_cursorIndexOfSaleId);
            final String _tmpReceiptNumber;
            _tmpReceiptNumber = _cursor.getString(_cursorIndexOfReceiptNumber);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final long _tmpSentAt;
            _tmpSentAt = _cursor.getLong(_cursorIndexOfSentAt);
            _result = new EbmReceipt(_tmpId,_tmpSaleId,_tmpReceiptNumber,_tmpStatus,_tmpSentAt);
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
  public Flow<List<EbmReceipt>> getReceiptsByBusiness(final long businessId) {
    final String _sql = "\n"
            + "        SELECT e.* FROM ebm_receipts e \n"
            + "        JOIN sales s ON e.saleId = s.id \n"
            + "        WHERE s.businessId = ? \n"
            + "        ORDER BY e.sentAt DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, businessId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"ebm_receipts",
        "sales"}, new Callable<List<EbmReceipt>>() {
      @Override
      @NonNull
      public List<EbmReceipt> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSaleId = CursorUtil.getColumnIndexOrThrow(_cursor, "saleId");
          final int _cursorIndexOfReceiptNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "receiptNumber");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfSentAt = CursorUtil.getColumnIndexOrThrow(_cursor, "sentAt");
          final List<EbmReceipt> _result = new ArrayList<EbmReceipt>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final EbmReceipt _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpSaleId;
            _tmpSaleId = _cursor.getLong(_cursorIndexOfSaleId);
            final String _tmpReceiptNumber;
            _tmpReceiptNumber = _cursor.getString(_cursorIndexOfReceiptNumber);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final long _tmpSentAt;
            _tmpSentAt = _cursor.getLong(_cursorIndexOfSentAt);
            _item = new EbmReceipt(_tmpId,_tmpSaleId,_tmpReceiptNumber,_tmpStatus,_tmpSentAt);
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
