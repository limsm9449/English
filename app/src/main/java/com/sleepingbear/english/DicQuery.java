package com.sleepingbear.english;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DicQuery {

    public static String getWriteData(String kind) {
        StringBuffer sql = new StringBuffer();

        DicUtils.dicLog("kind : "  + kind);
        if ( "C01".equals(kind) || "C02".equals(kind) ) {
            sql.append("SELECT '" + CommConstants.tag_code_ins + "'||':'||A.CODE_GROUP||':'||A.CODE||':'||A.CODE_NAME WRITE_DATA" + CommConstants.sqlCR);
            sql.append("  FROM DIC_CODE A" + CommConstants.sqlCR);
            sql.append(" WHERE CODE_GROUP = '" + kind + "'" + CommConstants.sqlCR);
            sql.append("   AND CODE NOT IN ('C010001')" + CommConstants.sqlCR);

            sql.append("UNION" + CommConstants.sqlCR);
            sql.append("SELECT '" + CommConstants.tag_note_ins + "'||':'||CODE||':'||SAMPLE_SEQ WRITE_DATA " + CommConstants.sqlCR);
            sql.append(" FROM DIC_NOTE" + CommConstants.sqlCR);
            sql.append(" WHERE CODE IN (SELECT CODE FROM DIC_CODE WHERE CODE_GROUP = '" + kind + "')" + CommConstants.sqlCR);
        } else if ( "VOC".equals(kind) ) {
            sql.append("SELECT '" + CommConstants.tag_code_ins + "'||':'||A.CODE_GROUP||':'||A.CODE||':'||A.CODE_NAME WRITE_DATA" + CommConstants.sqlCR);
            sql.append("  FROM DIC_CODE A" + CommConstants.sqlCR);
            sql.append(" WHERE CODE_GROUP IN ('VOC')" + CommConstants.sqlCR);
            sql.append("   AND CODE NOT IN ('VOC0001')" + CommConstants.sqlCR);

            sql.append("UNION" + CommConstants.sqlCR);
            sql.append("SELECT '" + CommConstants.tag_voc_ins + "'||':'||A.KIND||':'||A.ENTRY_ID||':'||A.INS_DATE||':'||A.MEMORIZATION WRITE_DATA " + CommConstants.sqlCR);
            sql.append(" FROM DIC_VOC A, DIC B" + CommConstants.sqlCR);
            sql.append(" WHERE A.ENTRY_ID = B.ENTRY_ID" + CommConstants.sqlCR);
        } else {
            sql.append("SELECT '" + CommConstants.tag_code_ins + "'||':'||A.CODE_GROUP||':'||A.CODE||':'||A.CODE_NAME WRITE_DATA" + CommConstants.sqlCR);
            sql.append("  FROM DIC_CODE A" + CommConstants.sqlCR);
            sql.append(" WHERE CODE_GROUP IN ('VOC','C01','C02')" + CommConstants.sqlCR);
            sql.append("   AND CODE NOT IN ('VOC0001','C010001')" + CommConstants.sqlCR);

            sql.append("UNION" + CommConstants.sqlCR);
            sql.append("SELECT '" + CommConstants.tag_note_ins + "'||':'||CODE||':'||SAMPLE_SEQ WRITE_DATA " + CommConstants.sqlCR);
            sql.append(" FROM DIC_NOTE" + CommConstants.sqlCR);
            sql.append(" WHERE CODE IN (SELECT CODE FROM DIC_CODE WHERE CODE_GROUP IN ('C01','C02') )" + CommConstants.sqlCR);

            sql.append("UNION" + CommConstants.sqlCR);
            sql.append("SELECT '" + CommConstants.tag_voc_ins + "'||':'||A.KIND||':'||A.ENTRY_ID||':'||A.INS_DATE||':'||A.MEMORIZATION WRITE_DATA " + CommConstants.sqlCR);
            sql.append(" FROM DIC_VOC A, DIC B" + CommConstants.sqlCR);
            sql.append(" WHERE A.ENTRY_ID = B.ENTRY_ID" + CommConstants.sqlCR);
        }

        DicUtils.dicSqlLog(sql.toString());

        return sql.toString();
    }

    /**
     * 나의 단어장 목록을 가져온다.
     * @return
     */
    public static String getVocabularyCategory() {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT 1 _id, CODE KIND, CODE_NAME KIND_NAME" + CommConstants.sqlCR);
        sql.append("  FROM DIC_CODE A" + CommConstants.sqlCR);
        sql.append(" WHERE CODE_GROUP = 'MY_VOC'" + CommConstants.sqlCR);
        sql.append(" ORDER BY 1,3" + CommConstants.sqlCR);

        DicUtils.dicSqlLog(sql.toString());

        return sql.toString();
    }

    /**
     * 사전 상세 정보를 가져온다.
     * @param entryId
     * @return
     */
    public static String getVocDetailForEntryId(String entryId) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT SEQ _id, WORD, MEAN, ENTRY_ID, SPELLING, TENSE, TYPE, (SELECT COUNT(*) FROM DIC_VOC WHERE ENTRY_ID = '" + entryId + "') MY_VOC" + CommConstants.sqlCR);
        sql.append("  FROM DIC" + CommConstants.sqlCR);
        sql.append(" WHERE ENTRY_ID = '" + entryId + "'" + CommConstants.sqlCR);

        DicUtils.dicSqlLog(sql.toString());

        return sql.toString();
    }

    /**
     * CONTEXT MENU에 사용할 단어장 목록을 가져온다.
     * @return
     */
    public static String getSentenceViewContextMenu() {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT 2 _id, 2 ORD, CODE KIND, CODE_NAME||' 등록' KIND_NAME" + CommConstants.sqlCR);
        sql.append("  FROM DIC_CODE A" + CommConstants.sqlCR);
        sql.append(" WHERE CODE_GROUP = 'MY_VOC'" + CommConstants.sqlCR);
        sql.append(" ORDER BY 1,4" + CommConstants.sqlCR);

        DicUtils.dicSqlLog(sql.toString());

        return sql.toString();
    }

    /**
     * 단어장의 정렬을 변경한다.
     * @return
     */
    public static String updVocRandom() {
        StringBuffer sql = new StringBuffer();

        sql.append("UPDATE DIC_VOC" + CommConstants.sqlCR);
        sql.append("   SET RANDOM_SEQ = RANDOM()" + CommConstants.sqlCR);

        DicUtils.dicSqlLog(sql.toString());

        return sql.toString();
    }

    /**
     * 4지선다형에 사용할 답을 가져온다.
     * @param vocKind
     * @param answerCnt
     * @return
     */
    public static String getSampleAnswerForStudy(String vocKind, int answerCnt) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT SEQ _id," + CommConstants.sqlCR);
        sql.append("       WORD," + CommConstants.sqlCR);
        sql.append("       MEAN" + CommConstants.sqlCR);
        sql.append("FROM   DIC" + CommConstants.sqlCR);
        sql.append("WHERE  ENTRY_ID NOT IN (SELECT ENTRY_ID FROM DIC_VOC WHERE KIND = '" + vocKind + "')" + CommConstants.sqlCR);
        sql.append("AND    SPELLING != ''" + CommConstants.sqlCR);
        sql.append("ORDER  BY RANDOM()" + CommConstants.sqlCR);
        sql.append("LIMIT  " + answerCnt + CommConstants.sqlCR);

        DicUtils.dicSqlLog(sql.toString());

        return sql.toString();
    }

    /**
     * 단어장 목록을 가져온다.
     * @return
     */
    public static String getVocCategory() {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT 1 _id, CODE KIND, CODE_NAME KIND_NAME," + CommConstants.sqlCR);
        sql.append("            COALESCE((SELECT COUNT(*)" + CommConstants.sqlCR);
        sql.append("                        FROM DIC_VOC" + CommConstants.sqlCR);
        sql.append("                       WHERE KIND = A.CODE" + CommConstants.sqlCR);
        sql.append("                       GROUP BY  KIND),0) CNT" + CommConstants.sqlCR);
        sql.append("  FROM DIC_CODE A" + CommConstants.sqlCR);
        sql.append(" WHERE CODE_GROUP = 'MY_VOC'" + CommConstants.sqlCR);
        sql.append(" ORDER BY 1,3" + CommConstants.sqlCR);

        DicUtils.dicSqlLog(sql.toString());

        return sql.toString();
    }

    /**
     * 단어장에 있는 단어 갯수를 가져온다.
     * @return
     */
    public static String getVocabularyCount(String kind) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT 1 _id, COUNT(*) CNT" + CommConstants.sqlCR);
        sql.append("  FROM DIC_VOC" + CommConstants.sqlCR);
        sql.append(" WHERE KIND = '" + kind + "'" + CommConstants.sqlCR);
        DicUtils.dicSqlLog(sql.toString());

        return sql.toString();
    }

    /**
     * 뉴스에서 클릭한 단어 조회
     * @return
     */
    public static String getNewsClickword() {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT B.SEQ _id, A.SEQ, B.ENTRY_ID, B.WORD, B.MEAN, B.SPELLING, A.INS_DATE" + CommConstants.sqlCR);
        sql.append("FROM   DIC_CLICK_WORD A, DIC B" + CommConstants.sqlCR);
        sql.append("WHERE  A.ENTRY_ID = B.ENTRY_ID " + CommConstants.sqlCR);
        sql.append("ORDER  BY A.INS_DATE DESC, B.WORD" + CommConstants.sqlCR);

        DicUtils.dicSqlLog(sql.toString());

        return sql.toString();
    }

    /**
     * 추가할 단어장 코드를 가져온다.
     * @param mDb
     * @return
     */
    public static String getInsCategoryCode(SQLiteDatabase mDb) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT MAX(CODE) CODE" + CommConstants.sqlCR);
        sql.append("  FROM DIC_CODE" + CommConstants.sqlCR);
        sql.append(" WHERE CODE_GROUP = 'MY_VOC'" + CommConstants.sqlCR);
        DicUtils.dicSqlLog(sql.toString());

        String insCategoryCode = "";
        Cursor maxCategoryCursor = mDb.rawQuery(sql.toString(), null);
        if ( maxCategoryCursor.moveToNext() ) {
            String max = maxCategoryCursor.getString(maxCategoryCursor.getColumnIndexOrThrow("CODE"));
            int maxCategory = Integer.parseInt(max.substring(3,max.length()));
            insCategoryCode = "VOC" + DicUtils.lpadding(Integer.toString(maxCategory + 1), 4, "0");
            DicUtils.dicSqlLog("insCategoryCode : " + insCategoryCode);
        }

        return insCategoryCode;
    }

    /**
     * 코드를 등록한다.
     * @param codeGroup
     * @param code
     * @param codeName
     * @return
     */
    public static String getInsNewCategory(String codeGroup, String code, String codeName) {
        StringBuffer sql = new StringBuffer();

        sql.append("INSERT INTO DIC_CODE(CODE_GROUP, CODE, CODE_NAME)" + CommConstants.sqlCR);
        sql.append("VALUES('" + codeGroup + "', '" + code + "', '" + codeName + "')" + CommConstants.sqlCR);

        DicUtils.dicSqlLog(sql.toString());

        return sql.toString();
    }

    public static String getConversationStudyList(int difficult) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT SEQ _id, SEQ, SENTENCE1, SENTENCE2" + CommConstants.sqlCR);
        sql.append("  FROM DIC_SAMPLE" + CommConstants.sqlCR);
        if ( difficult == 1) {
            sql.append("  WHERE WORD_CNT < 6" + CommConstants.sqlCR);
        } else if ( difficult == 2) {
            sql.append("  WHERE WORD_CNT BETWEEN 6 AND 9" + CommConstants.sqlCR);
        } else {
            sql.append("  WHERE WORD_CNT > 9" + CommConstants.sqlCR);
        }
        sql.append(" ORDER BY RANDOM()" + CommConstants.sqlCR);
        sql.append(" LIMIT 1000" + CommConstants.sqlCR);

        DicUtils.dicSqlLog(sql.toString());

        return sql.toString();
    }

    public static String getPatternList() {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT SEQ _id, SEQ, PATTERN, DESC, SQL_WHERE" + CommConstants.sqlCR);
        sql.append("  FROM DIC_PATTERN" + CommConstants.sqlCR);
        DicUtils.dicSqlLog(sql.toString());

        return sql.toString();
    }

    public static String getPatternSampleList(String pattern) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT  SEQ _id, SEQ, SENTENCE1, SENTENCE2" + CommConstants.sqlCR);
        sql.append("FROM    DIC_SAMPLE" + CommConstants.sqlCR);
        sql.append("WHERE   SENTENCE1 LIKE '" + pattern + "'" + CommConstants.sqlCR);
        sql.append("ORDER   BY ORD" + CommConstants.sqlCR);
        DicUtils.dicSqlLog(sql.toString());

        return sql.toString();
    }

    /**
     * 회화 context Menu
     * @param isStudyAndDetail
     * @return
     */
    public static String getNoteKindContextMenu(boolean isStudyAndDetail) {
        StringBuffer sql = new StringBuffer();

        if ( isStudyAndDetail ) {
            sql.append("SELECT 1 ORD, 'M1' KIND, '회화 학습' KIND_NAME" + CommConstants.sqlCR);
            sql.append("UNION ALL" + CommConstants.sqlCR);
            sql.append("SELECT 2 ORD, 'M2' KIND, '문장 상세' KIND_NAME" + CommConstants.sqlCR);
            sql.append("UNION ALL" + CommConstants.sqlCR);
            sql.append("SELECT 3 ORD, CODE KIND, CODE_NAME||' 회화에 추가' KIND_NAME" + CommConstants.sqlCR);
            sql.append("  FROM DIC_CODE" + CommConstants.sqlCR);
            sql.append(" WHERE CODE_GROUP = 'C01'" + CommConstants.sqlCR);
            sql.append(" ORDER BY ORD, KIND_NAME" + CommConstants.sqlCR);
        } else {
            sql.append("SELECT 3 ORD, CODE KIND, CODE_NAME KIND_NAME" + CommConstants.sqlCR);
            sql.append("  FROM DIC_CODE" + CommConstants.sqlCR);
            sql.append(" WHERE CODE_GROUP = 'C01'" + CommConstants.sqlCR);
            sql.append(" ORDER BY ORD, KIND_NAME" + CommConstants.sqlCR);
        }

        DicUtils.dicSqlLog(sql.toString());

        return sql.toString();
    }

    public static String getSample(String sampleSeq) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT  SEQ _id, SEQ, SENTENCE1, SENTENCE2" + CommConstants.sqlCR);
        sql.append("FROM    DIC_SAMPLE" + CommConstants.sqlCR);
        sql.append("WHERE   SEQ = " + sampleSeq + CommConstants.sqlCR);

        DicUtils.dicSqlLog(sql.toString());

        return sql.toString();
    }

    /**
     * 노트 데이타
     * @param code
     * @return
     */
    public static String getNoteList(String code) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT  B.SEQ _id, B.SEQ, B.SENTENCE1, B.SENTENCE2" + CommConstants.sqlCR);
        sql.append("FROM    DIC_NOTE A" + CommConstants.sqlCR);
        sql.append("        ,DIC_SAMPLE B" + CommConstants.sqlCR);
        sql.append("WHERE   A.SAMPLE_SEQ = B.SEQ " + CommConstants.sqlCR);
        sql.append("AND     A.CODE = '" + code + "'" + CommConstants.sqlCR);
        if ( "C03".equals(code.substring(0, 3)) ) {
            //네이버 회화는 SEQ로...
            sql.append("ORDER   BY A.SEQ" + CommConstants.sqlCR);
        } else {
            sql.append("ORDER   BY B.ORD" + CommConstants.sqlCR);
        }

        DicUtils.dicSqlLog(sql.toString());

        return sql.toString();
    }

    /**
     * 노트 그룹 종류
     * @return
     */
    public static String getNoteGroupKind() {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT SEQ _id,CODE KIND, CODE_NAME KIND_NAME" + CommConstants.sqlCR);
        sql.append("  FROM DIC_CODE" + CommConstants.sqlCR);
        sql.append(" WHERE CODE_GROUP = 'CONVERSATION'" + CommConstants.sqlCR);
        sql.append(" ORDER BY CODE" + CommConstants.sqlCR);

        DicUtils.dicSqlLog(sql.toString());

        return sql.toString();
    }

    /**
     * 노트 코드들..
     * @param groupCode
     * @return
     */
    public static String getNoteKind(String groupCode) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT SEQ _id,CODE KIND, CODE_NAME KIND_NAME" + CommConstants.sqlCR);
        sql.append("  FROM DIC_CODE" + CommConstants.sqlCR);
        sql.append(" WHERE CODE_GROUP = '" + groupCode + "'" + CommConstants.sqlCR);
        if ( "C03".equals(groupCode) ) {
            sql.append(" ORDER BY CODE" + CommConstants.sqlCR);
        } else if ( "C02".equals(groupCode) ) {
            sql.append(" ORDER BY CODE_NAME DESC" + CommConstants.sqlCR);
        } else {
            sql.append(" ORDER BY CODE_NAME" + CommConstants.sqlCR);
        }

        DicUtils.dicSqlLog(sql.toString());

        return sql.toString();
    }

    /**
     * 코드 삭제
     * @param code
     * @return
     */
    public static String getDelCode(String groupCode, String code) {
        StringBuffer sql = new StringBuffer();

        sql.append("DELETE FROM DIC_CODE" + CommConstants.sqlCR);
        sql.append(" WHERE CODE_GROUP = '" + groupCode + "'" + CommConstants.sqlCR);
        sql.append("   AND CODE = '" + code + "'" + CommConstants.sqlCR);

        DicUtils.dicSqlLog(sql.toString());

        return sql.toString();
    }

    /**
     * 노트 전체 내용 삭제
     * @param code
     * @return
     */
    public static String getDelNote(String code) {
        StringBuffer sql = new StringBuffer();

        sql.append("DELETE FROM DIC_NOTE" + CommConstants.sqlCR);
        sql.append(" WHERE CODE = '" + code + "'" + CommConstants.sqlCR);

        DicUtils.dicSqlLog(sql.toString());

        return sql.toString();
    }

    /**
     * 코드명 변경
     * @param code
     * @param codeName
     * @return
     */
    public static String getUpdCode(String groupCode, String code, String codeName) {
        StringBuffer sql = new StringBuffer();

        sql.append("UPDATE DIC_CODE" + CommConstants.sqlCR);
        sql.append("   SET CODE_NAME = '" + codeName + "'" + CommConstants.sqlCR);
        sql.append(" WHERE CODE_GROUP = '" + groupCode + "'" + CommConstants.sqlCR);
        sql.append("   AND CODE = '" + code + "'" + CommConstants.sqlCR);

        DicUtils.dicSqlLog(sql.toString());

        return sql.toString();
    }

}
