package com.gcm.haoye.goodclassmate;

/**
 * Created by kancheng on 2017/12/6.
 */


import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class CommentsDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {
            MySQLiteHelper.COLUMN_LIST_ID,
            MySQLiteHelper.COLUMN_NAME,
            MySQLiteHelper.COLUMN_MONEY,
            MySQLiteHelper.COLUMN_DATAEND,
            MySQLiteHelper.COLUMN_DATASTART,
            MySQLiteHelper.COLUMN_WEDT
    };

    public CommentsDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insertComment(Comment newComment) {

        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_NAME, newComment.getName());
        values.put(MySQLiteHelper.COLUMN_MONEY,newComment.getMoney());
        values.put(MySQLiteHelper.COLUMN_DATAEND,newComment.getDataend());
        values.put(MySQLiteHelper.COLUMN_DATASTART,newComment.getDatestart());
        values.put(MySQLiteHelper.COLUMN_WEDT,newComment.getWedt());
        for(String str:values.keySet()){
            System.out.println(values.get(str).toString());
        }
        System.out.println(MySQLiteHelper.TABLE_COMMENTS);

        long insertId = database.insert(MySQLiteHelper.TABLE_COMMENTS, null,values);
        return insertId;
    }


    public void deleteComment(long id) {
        System.out.println("Comment deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_COMMENTS, MySQLiteHelper.COLUMN_LIST_ID
                + " = " + id, null);
    }

    public List<Comment> getAllComments() {
        List<Comment> comments = new ArrayList<Comment>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,
                allColumns, null, null, null, null, null);
        if(cursor.getColumnCount() == 0)
            return null;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Comment comment = cursorToComment(cursor);
            comments.add(comment);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return comments;
    }

    private Comment cursorToComment(Cursor cursor) {
        Comment comment = new Comment();
        comment.setList_id(cursor.getLong(0));
        comment.setName(cursor.getString(1));
        comment.setMoney(cursor.getInt(2));
        comment.setDataend(cursor.getString(3));
        comment.setDatestart(cursor.getString(4));
        comment.setWedt(cursor.getString(5));
        return comment;
    }
}