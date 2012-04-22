package com.magicbox.sample;
//HELLO
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class DatabaseHelper
{
	private static final String DATABASE_NAME = "qrcars.db";
	private static final int DATABASE_VERSION = 1;
	private SQLiteDatabase mDB;
	
	public DatabaseHelper(Context context)
	{
		SDataHelper dbh = new SDataHelper(context);
		mDB = dbh.getWritableDatabase();
	}
	
	public int setOption(String val1)
	{
		int ret = -1;
		final String INSERT = "insert into OPTIONVALUES([VALUETXT]) values (?)";
		try
		{
			SQLiteStatement insertStmt = mDB.compileStatement(INSERT);
			insertStmt.bindString(1, val1);
			
			ret = (int)insertStmt.executeInsert();
		}
		
		catch(SQLException ex)
		{
			Log.e(ex.getMessage(), "ApplicantInfo insertion falied");
		}
		return ret;
	}
	
	public void DeleteOption(String val)
	{
		try
		{
			String WHERE = "VALUETXT=\"" + val + "\"";
			mDB.delete("OPTIONVALUES", WHERE, null);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void DeleteAllOption()
	{
		try
		{
			mDB.delete("OPTIONVALUES", null, null);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void updateOption(String oldVal, String newVal)
	{
		ContentValues values = new ContentValues(1);
		values.put("VALUETXT", newVal);
		final String whereClause = "[VALUETXT]=\"" + oldVal + "\"";
		mDB.update("OPTIONVALUES", values, whereClause, null);
	}
	
	public ArrayList<String> getOptionSet()
	{

		ArrayList<String> qSet = null;
		//CREATE TABLE IF NOT EXISTS OPTIONVALUES(VALUETXT VARCHAR(100))
		
		final String FETCH = "select VALUETXT from OPTIONVALUES";
		try
		{
			Cursor curr = mDB.rawQuery(FETCH, null);
			qSet = new ArrayList<String>();
			if(curr.moveToFirst())
			{
				qSet.add(curr.getString(0));
				while(curr.moveToNext())
				{
					qSet.add(curr.getString(0));
					
				}
			}
		}
		catch(SQLException ex)
		{
			Log.e(ex.getMessage(), "Question Fetch failed");
		}
		
		return qSet;
	}
	
		
	
	
	
	
	public static class SDataHelper extends SQLiteOpenHelper {
		public SDataHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			
			//Queries qr = new Queries();
			try
			{
				db.execSQL("CREATE TABLE IF NOT EXISTS OPTIONVALUES(VALUETXT VARCHAR(100))");
				
			}
			catch(SQLException ex)
			{
				Log.e(ex.getMessage(), "Table Creation failed");
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		}
	}
}

