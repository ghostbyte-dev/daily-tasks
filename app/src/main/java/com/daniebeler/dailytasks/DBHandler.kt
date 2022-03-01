package com.daniebeler.dailytasks

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import kotlin.collections.ArrayList

class DBHandler(context: Context):SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION)  {
    override fun onCreate(db: SQLiteDatabase) {

        val createToDoItemTable = "CREATE TABLE $TABLE_TODO_ITEM (" +
                "$COL_ID integer PRIMARY KEY AUTOINCREMENT," +
                "$COL_NAME varchar," +
                "$COL_CREATED_AT date," +
                "$COL_IS_COMPLETED integer);"

        db.execSQL(createToDoItemTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun updateToDo(position: Int, date: String) {
        val db:SQLiteDatabase = writableDatabase
        val mutableList:MutableList<ToDoItem> = getToDos(date)

        val data = ContentValues()
        data.put(COL_IS_COMPLETED, mutableList[position].isCompleted.not()).toString()

        db.update(TABLE_TODO_ITEM, data, "$COL_ID=?", arrayOf(mutableList[position].id.toString()))
    }

    fun addToDo(toDo: ToDoItem) : Boolean{
        val db:SQLiteDatabase = writableDatabase
        val cv = ContentValues()
        cv.put(COL_NAME, toDo.name)
        cv.put(COL_CREATED_AT, toDo.date)
        cv.put(COL_IS_COMPLETED, false)
        val result : Long = db.insert(TABLE_TODO_ITEM, null, cv)
        return result != (-1).toLong()
    }

    fun deleteToDo(position: Int, date: String){
        val db:SQLiteDatabase = writableDatabase
        val mutableList:MutableList<ToDoItem> = getToDos(date)
        db.delete(TABLE_TODO_ITEM, "$COL_ID=?", arrayOf(mutableList[position].id.toString()))
    }

    fun getToDos(date:String):MutableList<ToDoItem>{
        val result:MutableList<ToDoItem> = ArrayList()
        val db = readableDatabase
        val queryResult:Cursor
        if(date == "today"){
            queryResult = db.rawQuery("SELECT * FROM $TABLE_TODO_ITEM WHERE $COL_CREATED_AT = date('now', 'localtime')", null)
        }
        else{
            queryResult = db.rawQuery("SELECT * FROM $TABLE_TODO_ITEM WHERE $COL_CREATED_AT = date('now', '+1 day', 'localtime')", null)
        }

        if(queryResult.moveToFirst()){
            do{
                val todo = ToDoItem()
                todo.id = queryResult.getLong(queryResult.getColumnIndex(COL_ID))
                todo.name = queryResult.getString(queryResult.getColumnIndex(COL_NAME))
                todo.isCompleted = queryResult.getInt(queryResult.getColumnIndex(COL_IS_COMPLETED)) > 0
                result.add(todo)
            }while (queryResult.moveToNext())
        }
        queryResult.close()
        return result
    }
}