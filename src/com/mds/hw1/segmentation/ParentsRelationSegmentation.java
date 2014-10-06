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
public class ParentsRelationSegmentation implements EntitySegmentation{

    public static String segmentationCategory = "parent relation";
    public String prDictFile = Util.USER_DICT_FOLDER + "dict.parent.relation";
    public String prEntityFile = Util.ENTITY_FOLDER + "entity.parent.relation";
    public String segmentationFile = Util.PARTICIPLE_FOLDER + "segmented.parent.relation";
    
    @Override
    public String segmentation() {
        try {
            File prDictF = new File(this.prDictFile);
            if(!prDictF.exists()) {
                return Util.systemTime() + "  File \"" + this.prDictFile + "\" doesn't exist!\n" + "Please generate user dict first!\n";
            }
            Segmentation parti = new Segmentation();
            parti.loadUserDict(this.prDictFile);
            
            File prEntityF = new File(Util.ENTITY_FOLDER + "entity.parent.relation");
            if(!prEntityF.exists()) {
                return Util.systemTime() + "  File \"" + this.prEntityFile + "\" doesn't exist!\n";
            }
            BufferedReader reader = new BufferedReader(new FileReader(Util.ENTITY_FOLDER + "entity.parent.relation"));
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
            Logger.getLogger(ParentsRelationSegmentation.class.getName()).log(Level.SEVERE, null, ex);
            return ex.toString();
        }
    }
    public boolean validate(String name, String desc) {
        if(desc.contains("其父") || desc.contains("其母") || desc.contains("父亲") || desc.contains("母亲") || desc.contains("爸爸") || desc.contains("妈妈") || desc.contains("之子") || desc.contains("之女"))
            return true;
        return false;
    }
}
