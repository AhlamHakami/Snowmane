package com.task;

import android.util.Log;
import android.widget.Toast;

import com.app.MyApp;
import com.car.CommunicationService;
import com.common.Constant;
import com.db.model.Command;

import java.util.List;

public class TaskManager {
    public static String TAG = TaskManager.class.getSimpleName();
    private static List<Command> list = null;
    private static boolean running = false;
    private static Thread thread = null;

    private static synchronized List<Command> getList() {
        return list;
    }

    private static synchronized void setList(List<Command> list) {
        TaskManager.list = list;
    }

    private static synchronized boolean getRunning() {
        return running;
    }

    private static synchronized void setRunning(boolean isRunning) {
        TaskManager.running = isRunning;
    }

    private static synchronized Thread getThread() {
        return thread;
    }

    private static synchronized void setThread(Thread thread) {
        TaskManager.thread = thread;
    }

    public static boolean executeCommands(List<Command> list){
        Log.d(TAG, "+executeCommands");
        if(list == null || list.size() == 0 || getRunning()) {
            Toast.makeText(MyApp.getInstances().getApplicationContext(), "command running", Toast.LENGTH_SHORT).show();
            return false;
        }
        setRunning(true);
        setList(list);
        setThread(new Thread(new Runnable() {
            @Override
            public void run() {
                for (Command command: getList()) {
                    long time = Long.parseLong(command.getTime()) * 1000;
                    long start = System.currentTimeMillis();
                    while(getRunning()){
                        long end = System.currentTimeMillis();
                        long delta = end - start;
                        if (delta < time){
                            try {
                                // send event
                                CommunicationService.sendCommand(command);
                                Thread.sleep(time - delta);
                                break;
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            // send event
                            CommunicationService.sendCommand(command);
                            break;
                        }
                    }
                }
                getList().clear();
                setList(null);
                setThread(null);
                setRunning(false);
            }
        }));
        getThread().start();
        Log.d(TAG, "-executeCommands");
        return true;
    }

    public static boolean executeCommand(Command command){
        Log.d(TAG, "+executeCommand");
        if(getRunning()) {
            Toast.makeText(MyApp.getInstances().getApplicationContext(), "command running", Toast.LENGTH_SHORT).show();
            return false;
        }
        setRunning(true);
        CommunicationService.sendCommand(command);
        setRunning(false);
        Log.d(TAG, "-executeCommand");
        return true;
    }

    public static void stop(){
        Log.d(TAG, "+stop");
        if(!getRunning())
            return;
        setRunning(false);
        Log.d(TAG, "-stop");
    }
}
