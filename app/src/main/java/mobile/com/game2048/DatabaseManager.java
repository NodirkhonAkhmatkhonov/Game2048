package mobile.com.game2048;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseManager extends SQLiteOpenHelper {
    private static DatabaseManager dbHelper;

    public static DatabaseManager getInstance(Context context) {
        if (dbHelper == null)
            dbHelper = new DatabaseManager(context, "game", null, 1);
        return dbHelper;
    }

    public DatabaseManager(@Nullable Context context,
                           @Nullable String name,
                           @Nullable SQLiteDatabase.CursorFactory factory,
                           int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table scoreTable(_id integer primary key autoincrement, score integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
}
