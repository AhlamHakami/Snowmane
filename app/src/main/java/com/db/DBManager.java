package com.db;

import android.util.Log;

import com.app.MyApp;
import com.db.dao.CommandDao;
import com.db.model.Command;

import java.util.ArrayList;
import java.util.List;

public class DBManager {
    public static String TAG = DBManager.class.getSimpleName();
    // get all commands with name
    public static List<Command> getCommands(String name) {
        Log.d(TAG, "getCommands");
        List<Command> list = new ArrayList<>(100);
        Command command = getCommand(name);
        if (command == null){
            return list;
        }

        list.add(command);
        Log.d(TAG, "Id:" + command.getId().toString() + "-> Time:" + command.getTime());
        while (command.getChild().longValue() != 0) {
            command = getCommand(command.getChild());
            list.add(command);
            Log.d(TAG, "Id:" + command.getId().toString() + "-> Time:" + command.getTime());
        }

        return list;
    }

    public static Command getCommand(String name) {
        Log.d(TAG, "getCommand");
        if (name == null | name.isEmpty()){
            return null;
        }
        CommandDao dao = MyApp.getInstances().getDaoSession().getCommandDao();
        List<Command> list = null;
        try {
            list = dao.loadAll(); // should be only one item.
        } catch (android.database.sqlite.SQLiteException sqlE){
            list = null;
        }
        if (list == null || list.size() == 0){
            return null;
        }
        for (Command command : list
             ) {
            if(name.equalsIgnoreCase(command.getName())){
                return command;
            }
        }
        return null;
    }

    public static Command getCommand(Long id) {
        Log.d(TAG, "getCommand");
        CommandDao dao = MyApp.getInstances().getDaoSession().getCommandDao();
        Command command = null;
        try {
            command = dao.loadByRowId(id); // should be only one item.
        } catch (android.database.sqlite.SQLiteException sqlE){
            command = null;
        }
        return command;
    }

    public static void deleteCommand(Long id) {
        Log.d(TAG, "deleteCommand");
        CommandDao dao = MyApp.getInstances().getDaoSession().getCommandDao();
        try {
            dao.deleteByKey(id); // should be only one item.
        } catch (android.database.sqlite.SQLiteException sqlE){
            sqlE.printStackTrace();
        }
    }

    public static void deleteCommands() {
        Log.d(TAG, "deleteCommands");
        CommandDao dao = MyApp.getInstances().getDaoSession().getCommandDao();
        try {
            dao.deleteAll(); // should be only one item.
        } catch (android.database.sqlite.SQLiteException sqlE){
            sqlE.printStackTrace();
        }
    }

    public static void deleteCommands(String name) {
        Log.d(TAG, "deleteCommands");
        List<Command> list = getCommands(name);
        for (Command command: list
             ) {
            deleteCommand(command.getId());
        }
    }

    public static void insertCommand(Command command) {
        Log.d(TAG, "insertCommand");
        CommandDao dao = MyApp.getInstances().getDaoSession().getCommandDao();
        try {
            dao.insert(command);
        } catch (android.database.sqlite.SQLiteException sqlE){
            sqlE.printStackTrace();
        }
    }

    public static void updateCommand(Command command) {
        Log.d(TAG, "updateCommand");
        CommandDao dao = MyApp.getInstances().getDaoSession().getCommandDao();
        try {
            dao.update(command);
        } catch (android.database.sqlite.SQLiteException sqlE){
            sqlE.printStackTrace();
        }
    }

    public static long getCommandLastIndex(){
        Log.d(TAG, "getCommandLastIndex");
        CommandDao dao = MyApp.getInstances().getDaoSession().getCommandDao();
        List<Command> list = null;
        try {
            list = dao.loadAll();
        } catch (android.database.sqlite.SQLiteException sqlE){
            sqlE.printStackTrace();
        }

        return list == null ? 1 : list.size() + 1;
    }
}
