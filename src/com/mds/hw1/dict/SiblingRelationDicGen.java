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
public class SiblingRelationDicGen implements UserDictGenerator {
    public static String dictCategory = "sibling relation";
    public String sirDictFile = Util.USER_DICT_FOLDER + "dict.sibling.relation";
    public String sirIDFile = Util.USER_DICT_FOLDER + "id.sibling,relation";
    public String originalEntityFile = Util.ENTITY_FOLDER + "entity.people";
    public String sirEntityFile = Util.ENTITY_FOLDER + "entity.sibling.relation";
    public String sirCode = "sir";
    
    @Override
    public String generate() {
        try {
            File entityFile = new File(this.originalEntityFile);
            if(!entityFile.exists()) {
                return Util.systemTime() + "  File \"" + this.originalEntityFile + "\" doesn't exist!\n";
            }
            BufferedReader reader = new BufferedReader(new FileReader(entityFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.sirDictFile));
            BufferedWriter idWriter = new BufferedWriter(new FileWriter(this.sirIDFile));
            String line;
            while((line = reader.readLine()) != null) {
                String[] p = line.split("\t");
                String word = p[1] + Util.WORD_CODE_SPLITTER + this.sirCode;
                writer.write(word);
                writer.newLine();
                idWriter.write(p[0] +  "\t" + p[1]);
                idWriter.newLine();
            }
            reader.close();
            writer.close();
            idWriter.close();
            
            reader = new BufferedReader(new FileReader(Util.ENTITY_FOLDER + "entity.people"));
            writer = new BufferedWriter(new FileWriter(this.sirEntityFile));
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
            
            return Util.systemTime() + "  Done. User dictionary \"" + this.sirDictFile + "\" has been created.\n";
        } catch (IOException ex) {
            Logger.getLogger(SiblingRelationDicGen.class.getName()).log(Level.SEVERE, null, ex);
            return ex.toString();
        }
    }

    @Override
    public boolean validate(String desc) {
        if(desc.contains("其姐") || desc.contains("其弟") || desc.contains("其兄") || desc.contains("其妹") || desc.contains("哥哥") || desc.contains("姐姐") || desc.contains("妹妹") || desc.contains("弟弟") || desc.contains("堂弟") || desc.contains("堂妹") || desc.contains("表哥") || desc.contains("表姐") || desc.contains("表妹") || desc.contains("表弟"))
            return true;
        return false;
    }
}
