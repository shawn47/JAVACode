/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mds.hw1.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author picry
 */
public class Util {
    public static String ENTITY_FOLDER = "./Data/Entity/";
    public static String USER_DICT_FOLDER = "./Data/UserDict/";
    public static String RELATION_FOLDER = "./Data/Relation/";
    public static String PARTICIPLE_FOLDER = "./Data/Participle/";
    public static String WORD_CODE_SPLITTER = "@@";
    
    public static void initialise() {
        File entityDir = new File(Util.ENTITY_FOLDER);
        File dictDir = new File(Util.USER_DICT_FOLDER);
        File relationDir = new File(Util.RELATION_FOLDER);
        File participleDir = new File(Util.PARTICIPLE_FOLDER);
        if(!entityDir.exists() || !entityDir.isDirectory()) {
            entityDir.mkdir();
        }
        if(!dictDir.exists() || !dictDir.isDirectory()) {
            dictDir.mkdir();
        }
        if(!relationDir.exists() || !relationDir.isDirectory()) {
            relationDir.mkdir();
        }
        if(!participleDir.exists() || !participleDir.isDirectory()) {
            participleDir.mkdir();
        }
    }
    
    public static String systemTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date d = new Date();
        return sdf.format(d);
    }
}
