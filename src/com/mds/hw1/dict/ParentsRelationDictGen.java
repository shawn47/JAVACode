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
public class ParentsRelationDictGen implements UserDictGenerator{

    public static String dictCategory = "parent relation";
    public String prDictFile = Util.USER_DICT_FOLDER + "dict.parent.relation";
    public String prIDFile = Util.USER_DICT_FOLDER + "id.parent,relation";
    public String originalEntityFile = Util.ENTITY_FOLDER + "entity.people";
    public String prEntityFile = Util.ENTITY_FOLDER + "entity.parent.relation";
    public String prCode = "pr";
    
    @Override
    public String generate() {
        try {
            File entityFile = new File(this.originalEntityFile);
            if(!entityFile.exists()) {
                return Util.systemTime() + "  File \"" + this.originalEntityFile + "\" doesn't exist!\n";
            }
            BufferedReader reader = new BufferedReader(new FileReader(entityFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.prDictFile));
            BufferedWriter idWriter = new BufferedWriter(new FileWriter(this.prIDFile));
            String line;
            while((line = reader.readLine()) != null) {
                String[] p = line.split("\t");
                // remove the "·" from name
                p[1] = p[1].replace("·", "").replace(" ", "");
                String word = p[1] + Util.WORD_CODE_SPLITTER + this.prCode;
                writer.write(word);
                writer.newLine();
                idWriter.write(p[0] +  "\t" + p[1]);
                idWriter.newLine();
            }
            reader.close();
            writer.close();
            idWriter.close();
            
            reader = new BufferedReader(new FileReader(Util.ENTITY_FOLDER + "entity.people"));
            writer = new BufferedWriter(new FileWriter(this.prEntityFile));
            line = "";
            while((line = reader.readLine()) != null) {
                String[] p = line.split("\t");
                if(this.validate(p[3])) {
                    String newLine = line.replace("·", "").replace(" ", "");
                    //String newLine = line;
                    writer.write(newLine);
                    writer.newLine();
                    System.out.println(line);
                }
            }
            reader.close();
            writer.close();
            
            return Util.systemTime() + "  Done. User dictionary \"" + this.prDictFile + "\" has been created.\n";
        } catch (IOException ex) {
            Logger.getLogger(ParentsRelationDictGen.class.getName()).log(Level.SEVERE, null, ex);
            return ex.toString();
        }
    }

    @Override
    public boolean validate(String desc) {
        if(desc.contains("其父") || desc.contains("其母") || desc.contains("父亲") || desc.contains("母亲") || desc.contains("爸爸") || desc.contains("妈妈") || desc.contains("之子") || desc.contains("之女"))
            return true;
        return false;
    }
}
