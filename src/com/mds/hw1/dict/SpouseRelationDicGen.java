/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mds.hw1.dict;

import com.mds.hw1.util.Util;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author 永博
 */
public class SpouseRelationDicGen implements UserDictGenerator{
    public static String dictCategory = "spouse relation";
    public String sprDictFile = Util.USER_DICT_FOLDER + "dict.spouse.relation";
    public String sprIDFile = Util.USER_DICT_FOLDER + "id.spouse,relation";
    public String originalEntityFile = Util.ENTITY_FOLDER + "entity.people";
    public String sprEntityFile = Util.ENTITY_FOLDER + "entity.spouse.relation";
    public String sprCode = "spr";
    
    @Override
    public String generate() {
        try {
            File entityFile = new File(this.originalEntityFile);
            if(!entityFile.exists()) {
                return Util.systemTime() + "  File \"" + this.originalEntityFile + "\" doesn't exist!\n";
            }
            BufferedReader reader = new BufferedReader(new FileReader(entityFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.sprDictFile));
            BufferedWriter idWriter = new BufferedWriter(new FileWriter(this.sprIDFile));
            String line;
            while((line = reader.readLine()) != null) {
                String[] p = line.split("\t");
                String word = p[1] + Util.WORD_CODE_SPLITTER + this.sprCode;
                writer.write(word);
                writer.newLine();
                idWriter.write(p[0] +  "\t" + p[1]);
                idWriter.newLine();
            }
            reader.close();
            writer.close();
            idWriter.close();
            
            reader = new BufferedReader(new FileReader(Util.ENTITY_FOLDER + "entity.people"));
            writer = new BufferedWriter(new FileWriter(this.sprEntityFile));
            line = "";
            while((line = reader.readLine()) != null) {
                String[] p = line.split("\t");
                if(this.validate(p[3])) {
                    String newLine = line;
                    writer.write(newLine);
                    writer.newLine();
                    System.out.println(line);
                }
            }
            reader.close();
            writer.close();
            
            return Util.systemTime() + "  Done. User dictionary \"" + this.sprDictFile + "\" has been created.\n";
        } catch (IOException ex) {
            Logger.getLogger(SpouseRelationDicGen.class.getName()).log(Level.SEVERE, null, ex);
            return ex.toString();
        }
    }

    @Override
    public boolean validate(String desc) {
        if(desc.contains("其夫") || desc.contains("其妻") || desc.contains("丈夫") || desc.contains("妻子") || desc.contains("老婆") || desc.contains("老公") || desc.contains("结婚"))
            return true;
        return false;
    }
}
