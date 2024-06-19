package com.example.finalapplication

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) :SQLiteOpenHelper(context, "mymenudb", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table menu_tb (_id integer primary key autoincrement, menu not null)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}