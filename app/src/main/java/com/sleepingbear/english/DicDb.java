package com.sleepingbear.english;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class DicDb {

    /**
     * 단어장에 등록한다.
     * @param db
     * @param entryId
     * @param kind
     */
    public static void insDicVoc(SQLiteDatabase db, String entryId, String kind) {
        StringBuffer sql = new StringBuffer();
        sql.append("DELETE FROM DIC_VOC " + CommConstants.sqlCR);
        sql.append(" WHERE KIND = '" + kind + "'" + CommConstants.sqlCR);
        sql.append("   AND ENTRY_ID = '" + entryId + "'" + CommConstants.sqlCR);
        DicUtils.dicSqlLog(sql.toString());
        db.execSQL(sql.toString());

        sql.setLength(0);
        sql.append("INSERT INTO DIC_VOC (KIND, ENTRY_ID, MEMORIZATION,RANDOM_SEQ, INS_DATE) " + CommConstants.sqlCR);
        sql.append("SELECT '" + kind + "', ENTRY_ID, 'N', RANDOM(), '" + DicUtils.getDelimiterDate(DicUtils.getCurrentDate(), ".")  + "' " + CommConstants.sqlCR);
        sql.append("  FROM DIC " + CommConstants.sqlCR);
        sql.append(" WHERE ENTRY_ID = '" + entryId + "'" + CommConstants.sqlCR);
        DicUtils.dicSqlLog(sql.toString());
        db.execSQL(sql.toString());
    }

    /**
     * 단어장에서 삭제한다.
     * @param db
     * @param entryId
     */
    public static void delDicVocAll(SQLiteDatabase db, String entryId) {
        StringBuffer sql = new StringBuffer();
        sql.append("DELETE FROM DIC_VOC " + CommConstants.sqlCR);
        sql.append(" WHERE ENTRY_ID = '" + entryId + "'" + CommConstants.sqlCR);
        DicUtils.dicLog(sql.toString());
        db.execSQL(sql.toString());
    }

    /**
     * 나의 예문으로 등록했는지 여부
     * @param db
     * @param sentence
     * @return
     */
    public static boolean isExistMySample(SQLiteDatabase db, String sentence) {
        boolean rtn = false;

        if ( sentence != null ) {
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT COUNT(*) CNT  " + CommConstants.sqlCR);
            sql.append("  FROM DIC_MY_SAMPLE " + CommConstants.sqlCR);
            sql.append(" WHERE SENTENCE1 = '" + sentence.replaceAll("'", "''") + "'" + CommConstants.sqlCR);
            DicUtils.dicSqlLog(sql.toString());

            Cursor cursor = db.rawQuery(sql.toString(), null);
            if (cursor.moveToNext()) {
                if (cursor.getInt(cursor.getColumnIndexOrThrow("CNT")) > 0) {
                    rtn = true;
                }
            }
            cursor.close();
        }

        return rtn;
    }

    /**
     * 나의 예제를 지운다.
     * @param db
     * @param sentence
     */
    public static void delDicMySample(SQLiteDatabase db, String sentence) {
        StringBuffer sql = new StringBuffer();
        sql.append("DELETE FROM DIC_MY_SAMPLE " + CommConstants.sqlCR);
        sql.append(" WHERE SENTENCE1 = '" + sentence.replaceAll("'" ,"''") + "'" + CommConstants.sqlCR);
        DicUtils.dicLog(sql.toString());
        db.execSQL(sql.toString());
    }

    /**
     * 나의 예제로 등록한다.
     * @param db
     * @param sentence1
     * @param sentence2
     * @param today
     */
    public static void insDicMySample(SQLiteDatabase db, String sentence1, String sentence2, String today) {
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO DIC_MY_SAMPLE (TODAY, SENTENCE1, SENTENCE2)" + CommConstants.sqlCR);
        sql.append("VALUES( '" + today + "', '" + sentence1.replaceAll("'" ,"''") + "', '" + sentence2.replaceAll("'" ,"''") + "')" +  CommConstants.sqlCR);
        DicUtils.dicLog(sql.toString());
        db.execSQL(sql.toString());
    }

    /**
     * 뜻을 가져온다.
     * @param db
     * @param word
     * @return
     */
    public static HashMap getMean(SQLiteDatabase db, String word) {
        HashMap rtn = new HashMap();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT SPELLING, MEAN, ENTRY_ID  " + CommConstants.sqlCR);
        sql.append("  FROM DIC " + CommConstants.sqlCR);
        sql.append(" WHERE WORD = '" + word.toLowerCase().replaceAll("'", " ") + "' OR TENSE LIKE '% " + word.toLowerCase().replaceAll("'", " ") + " %'" + CommConstants.sqlCR);
        sql.append("ORDER  BY SPELLING DESC " + CommConstants.sqlCR);
        DicUtils.dicSqlLog(sql.toString());

        Cursor cursor = db.rawQuery(sql.toString(), null);
        if ( cursor.moveToNext() ) {
            rtn.put("SPELLING", cursor.getString(cursor.getColumnIndexOrThrow("SPELLING")));
            rtn.put("MEAN", cursor.getString(cursor.getColumnIndexOrThrow("MEAN")));
            rtn.put("ENTRY_ID", cursor.getString(cursor.getColumnIndexOrThrow("ENTRY_ID")));
        } else {
            rtn = getMeanOther(db, word);
        }
        cursor.close();

        return rtn;
    }

    public static HashMap getMeanOther(SQLiteDatabase db, String word) {
        HashMap rtn = new HashMap();
        String findWord = "";

        if ( "s".indexOf(word.substring(word.length() - 1)) > -1 ) {
            findWord = word.substring(0, word.length() - 1);
        } else if ( word.length() > 2 && "es,ed,ly".indexOf(word.substring(word.length() - 2)) > -1 ) {
            findWord = word.substring(0, word.length() - 2);
        } else if ( word.length() > 3 && "ing".indexOf(word.substring(word.length() - 3))  > -1 ) {
            findWord = word.substring(0, word.length() - 3);
        } else {
            findWord = word;
        }
        DicUtils.dicLog("findWord : " + findWord);

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT SPELLING, MEAN, ENTRY_ID  " + CommConstants.sqlCR);
        sql.append("  FROM DIC " + CommConstants.sqlCR);
        sql.append(" WHERE WORD = '" + findWord.toLowerCase().replaceAll("'", " ") + "'" +  CommConstants.sqlCR);
        sql.append("ORDER  BY SPELLING DESC " + CommConstants.sqlCR);
        DicUtils.dicSqlLog(sql.toString());

        Cursor cursor = db.rawQuery(sql.toString(), null);
        if ( cursor.moveToNext() ) {
            rtn.put("SPELLING", cursor.getString(cursor.getColumnIndexOrThrow("SPELLING")));
            rtn.put("MEAN", cursor.getString(cursor.getColumnIndexOrThrow("MEAN")));
            rtn.put("ENTRY_ID", cursor.getString(cursor.getColumnIndexOrThrow("ENTRY_ID")));
        }
        cursor.close();

        return rtn;
    }

    /**
     * 클릭한 단어를 저장한다.
     * @param db
     * @param entryId
     * @param insDate
     */
    public static void insDicClickWord(SQLiteDatabase db, String entryId, String insDate) {
        if ( "".equals(insDate) ) {
            insDate = DicUtils.getDelimiterDate(DicUtils.getCurrentDate(), ".");
        }

        StringBuffer sql = new StringBuffer();
        sql.append("DELETE FROM DIC_CLICK_WORD " + CommConstants.sqlCR);
        sql.append(" WHERE ENTRY_ID = '" + entryId + "'" + CommConstants.sqlCR);
        DicUtils.dicSqlLog(sql.toString());
        db.execSQL(sql.toString());

        sql.setLength(0);
        sql.append("INSERT INTO DIC_CLICK_WORD (ENTRY_ID, INS_DATE) " + CommConstants.sqlCR);
        sql.append("VALUES ( '" + entryId + "','" + insDate + "') " + CommConstants.sqlCR);

        DicUtils.dicSqlLog(sql.toString());
        db.execSQL(sql.toString());
    }

    public static void delDicClickWord(SQLiteDatabase db, int seq) {
        StringBuffer sql = new StringBuffer();
        sql.append("DELETE FROM DIC_CLICK_WORD " + CommConstants.sqlCR);
        sql.append(" WHERE SEQ = " + seq + "" + CommConstants.sqlCR);
        DicUtils.dicSqlLog(sql.toString());
        db.execSQL(sql.toString());
    }


    public static void insConversationStudy(SQLiteDatabase db, String sampleSeq, String insDate) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT  COUNT(*) CNT" + CommConstants.sqlCR);
        sql.append("FROM    DIC_CODE " + CommConstants.sqlCR);
        sql.append("WHERE   CODE_GROUP = 'C02'" +  CommConstants.sqlCR);
        sql.append("AND     CODE = '" + insDate + "'" + CommConstants.sqlCR);
        Cursor cursor = db.rawQuery(sql.toString(), null);
        if ( cursor.moveToNext() ) {
            if ( cursor.getInt(cursor.getColumnIndexOrThrow("CNT")) == 0 ) {
                sql.setLength(0);
                sql.append("INSERT INTO DIC_CODE(CODE_GROUP, CODE, CODE_NAME) " + CommConstants.sqlCR);
                sql.append("VALUES('C02', '" + insDate + "', '" + insDate + "')" + CommConstants.sqlCR);
                db.execSQL(sql.toString());
            }
        }
        cursor.close();

        sql.setLength(0);
        sql.append("DELETE  FROM DIC_NOTE " + CommConstants.sqlCR);
        sql.append("WHERE   CODE = '" + insDate + "'" + CommConstants.sqlCR);
        sql.append("AND     SAMPLE_SEQ = '" + sampleSeq + "'" + CommConstants.sqlCR);
        db.execSQL(sql.toString());

        sql.setLength(0);
        sql.append("INSERT INTO DIC_NOTE (CODE, SAMPLE_SEQ) " + CommConstants.sqlCR);
        sql.append("VALUES('" + insDate + "', " + sampleSeq + ") " + CommConstants.sqlCR);
        DicUtils.dicSqlLog(sql.toString());
        db.execSQL(sql.toString());

        sql.setLength(0);
        sql.append("UPDATE  DIC_CODE " + CommConstants.sqlCR);
        sql.append("SET     CODE_NAME = CODE || ' - ' || ( SELECT COUNT(*) FROM DIC_NOTE WHERE CODE = DIC_CODE.CODE ) || '개를 학습 하셨습니다.'  " + CommConstants.sqlCR);
        sql.append("WHERE   CODE_GROUP = 'C02' AND CODE = '" + insDate + "'" + CommConstants.sqlCR);
        DicUtils.dicSqlLog(sql.toString());
        db.execSQL(sql.toString());
    }

    public static void insConversationToNote(SQLiteDatabase db, String code, String sampleSeq) {
        StringBuffer sql = new StringBuffer();
        sql.append("DELETE  FROM DIC_NOTE " + CommConstants.sqlCR);
        sql.append("WHERE   CODE = '" + code + "'" + CommConstants.sqlCR);
        sql.append("AND     SAMPLE_SEQ = '" + sampleSeq + "'" + CommConstants.sqlCR);
        db.execSQL(sql.toString());

        sql.setLength(0);
        sql.append("INSERT INTO DIC_NOTE (CODE, SAMPLE_SEQ) " + CommConstants.sqlCR);
        sql.append("VALUES('" + code + "', " + sampleSeq + ") " + CommConstants.sqlCR);
        DicUtils.dicSqlLog(sql.toString());
        db.execSQL(sql.toString());
    }
    
}
