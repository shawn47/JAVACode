/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mds.hw1.segmentation;

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
public class SpouseRelationSegmentation implements EntitySegmentation{
    public static String segmentationCategory = "spouse relation";
    public String sprDictFile = Util.USER_DICT_FOLDER + "dict.spouse.relation";
    public String sprEntityFile = Util.ENTITY_FOLDER + "entity.spouse.relation";
    public String segmentationFile = Util.PARTICIPLE_FOLDER + "segmented.spouse.relation";
    
    @Override
    public String segmentation() {
        try {
            File prDictF = new File(this.sprDictFile);
            if(!prDictF.exists()) {
                return Util.systemTime() + "  File \"" + this.sprDictFile + "\" doesn't exist!\n" + "Please generate user dict first!\n";
            }
            Segmentation parti = new Segmentation();
            parti.loadUserDict(this.sprDictFile);
            
            File prEntityF = new File(Util.ENTITY_FOLDER + "entity.spouse.relation");
            if(!prEntityF.exists()) {
                return Util.systemTime() + "  File \"" + this.sprEntityFile + "\" doesn't exist!\n";
            }
            BufferedReader reader = new BufferedReader(new FileReader(Util.ENTITY_FOLDER + "entity.spouse.relation"));
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.segmentationFile));
            String line = "";
            while((line = reader.readLine()) != null) {
                String[] p = line.split("\t");
                //if(this.validate(p[1], p[3])) {
                String segSentence = parti.segmentation(p[3]);
                String newLine = p[0] + "\t" + p[1] + "\t" + p[2] + "\t" + segSentence;
                writer.write(newLine);
                writer.newLine();
                System.out.println(segSentence);
                //}
            }
            reader.close();
            writer.close();
            parti.clear();
            return Util.systemTime() + "  Done. Segmentation text \"" + this.segmentationFile + "\" has been created.\n";
        } catch (IOException ex) {
            Logger.getLogger(SpouseRelationSegmentation.class.getName()).log(Level.SEVERE, null, ex);
            return ex.toString();
        }
    }
    public boolean validate(String name, String desc) {
        if(desc.contains("其夫") || desc.contains("其妻") || desc.contains("丈夫") || desc.contains("妻子") || desc.contains("老婆") || desc.contains("老公") || desc.contains("结婚"))
            return true;
        return false;
    }
}
