package com.grumpycat.tetrisgame.tools;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.Nullable;

import com.grumpycat.tetrisgame.GameConfig;
import com.grumpycat.tetrisgame.core.StatisticInfo;

import java.util.ArrayList;
import java.util.List;

public class DBHelper {
    private static SQLiteDatabase db;
    public static void init(Context context){
        OpenHelper openHelper = new OpenHelper(context, "tetris_rank.db", 1);
        db = openHelper.getWritableDatabase();
    }
    private static final String INSERT_SQL = "INSERT INTO ranks(" +
            "score," +
            "lines," +
            "lvl," +
            "lastTime," +
            "timeStamp) VALUES(?,?,?,?,?)";

    private static SQLiteStatement insertStm;
    public static void insert(StatisticInfo info){
        if(insertStm == null){
            insertStm = db.compileStatement(INSERT_SQL);
        }
        insertStm.clearBindings();
        insertStm.bindLong(1, info.getScore());
        insertStm.bindLong(2, info.getLines());
        insertStm.bindLong(3, info.getLvl());
        insertStm.bindLong(4, info.getLastTime());
        insertStm.bindLong(5, info.getEndTime());
        insertStm.executeInsert();
    }

    public static void limitToMax(){
        Cursor cursor = db.rawQuery("SELECT id FROM ranks ORDER BY score ASC", null);
        int count = cursor.getCount();
        if(count > GameConfig.MAX_RANKS){
            int deleteNum = count -  GameConfig.MAX_RANKS;
            db.beginTransaction();
            while(cursor.moveToNext() && deleteNum > 0){
                int id = cursor.getInt(0);
                db.execSQL("DELETE FROM ranks WHERE id="+id);
                deleteNum--;
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        }
        cursor.close();
    }

    public static List<StatisticInfo> queryAll(){
        List<StatisticInfo> ret = null;
        Cursor cursor = db.rawQuery("SELECT * FROM ranks ORDER BY score DESC", null);
        if(cursor != null && cursor.getCount() > 0) {
            ret = new ArrayList<>(cursor.getCount());
            while (cursor.moveToNext()) {
                StatisticInfo info = new StatisticInfo();
                info.setScore(cursor.getInt(1));
                info.setLines(cursor.getInt(2));
                info.setLvl(cursor.getInt(3));
                info.setLastTime(cursor.getInt(4));
                info.setEndTime(cursor.getLong(5));
                ret.add(info);
            }
        }
        cursor.close();
        return ret;
    }

    private static class OpenHelper extends SQLiteOpenHelper{

        public OpenHelper(@Nullable Context context,
                          @Nullable String name,
                          int version) {
            super(context, name, null, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE ranks ("
                    + "id Integer PRIMARY KEY AUTOINCREMENT,"
                    + "score Integer,"
                    + "lines Integer,"
                    + "lvl Integer, "
                    + "lastTime Integer, "
                    + "timeStamp Long)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
