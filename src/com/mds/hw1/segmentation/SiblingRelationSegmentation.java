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
public class SiblingRelationSegmentation  implements EntitySegmentation {
    public static String segmentationCategory = "sibling relation";
    public String sirDictFile = Util.USER_DICT_FOLDER + "dict.sibling.relation";
    public String sirEntityFile = Util.ENTITY_FOLDER + "entity.sibling.relation";
    public String segmentationFile = Util.PARTICIPLE_FOLDER + "segmented.sibling.relation";
    
    @Override
    public String segmentation() {
        try {
            File prDictF = new File(this.sirDictFile);
            if(!prDictF.exists()) {
                return Util.systemTime() + "  File \"" + this.sirDictFile + "\" doesn't exist!\n" + "Please generate user dict first!\n";
            }
            Segmentation parti = new Segmentation();
            parti.loadUserDict(this.sirDictFile);
            
            File prEntityF = new File(Util.ENTITY_FOLDER + "entity.sibling.relation");
            if(!prEntityF.exists()) {
                return Util.systemTime() + "  File \"" + this.sirEntityFile + "\" doesn't exist!\n";
            }
            BufferedReader reader = new BufferedReader(new FileReader(Util.ENTITY_FOLDER + "entity.sibling.relation"));
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
            Logger.getLogger(SiblingRelationSegmentation.class.getName()).log(Level.SEVERE, null, ex);
            return ex.toString();
        }
    }
    public boolean validate(String name, String desc) {
        if(desc.contains("其姐") || desc.contains("其弟") || desc.contains("其兄") || desc.contains("其妹") || desc.contains("哥哥") || desc.contains("姐姐") || desc.contains("妹妹") || desc.contains("弟弟") || desc.contains("堂弟") || desc.contains("堂妹") || desc.contains("表哥") || desc.contains("表姐") || desc.contains("表妹") || desc.contains("表弟") )
            return true;
        return false;
    }
}
