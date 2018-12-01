package com.common;

import android.content.Context;

import com.app.MyApp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

//use enum to enables variables to be a set of predefined constants
public class Constant {
    public enum Direction {
        DIRECTION_FORWARD("F", 0),
        DIRECTION_BACKWORD("B", 1),
        DIRECTION_LEFT("G", 2),
        DIRECTION_RIGHT("I", 3),
        DIRECTION_STOP("S", 4);

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        private String name;
        private int index;

        Direction(String name, int index) {
            this.name = name;
            this.index = index;
        }

        @Override
        public String toString() {
            return this.index + "_" + this.name;
        }
    }

    public static Direction getDirection(int index){
        switch(index){
            case 0:
                return Direction.DIRECTION_FORWARD;
            case 1:
                return Direction.DIRECTION_BACKWORD;
            case 2:
                return Direction.DIRECTION_LEFT;
            case 3:
                return Direction.DIRECTION_RIGHT;
            case 4:
                return Direction.DIRECTION_STOP;
        }
        return Direction.DIRECTION_STOP;
    }

    public static HashMap<String, String> map = new HashMap<String, String>();

    public static String getProperty(String key) {
        return map.get(key);
    }

    static {
        Context c = MyApp.getInstances().getApplicationContext();
        Properties props = new Properties();
        try {
            InputStream in = c.getAssets().open("appConfig.properties");
            props.load(in);

            Enumeration<?> e = props.propertyNames();
            if (e.hasMoreElements()) {
                while (e.hasMoreElements()) {
                    String s = (String) e.nextElement();
                    map.put(s, props.getProperty(s));
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
}
