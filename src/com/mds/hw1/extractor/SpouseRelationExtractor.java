/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mds.hw1.extractor;

import com.mds.hw1.segmentation.Segmentation;
import com.mds.hw1.util.Util;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 *
 * @author 永博
 */
public class SpouseRelationExtractor implements RelationExtractor {
    public static String relationCategory = "spouse relation";
    public String sprIDFile = Util.USER_DICT_FOLDER + "id.spouse,relation";
    public String sprEntityFile = Util.ENTITY_FOLDER + "entity.spouse.relation";
    public String sprSegmentedFile = Util.PARTICIPLE_FOLDER + "segmented.spouse.relation";
    public String relationFile = Util.RELATION_FOLDER + SpouseRelationExtractor.relationCategory;
    protected HashMap<String, ArrayList<String>> hmSpRID = new HashMap<String, ArrayList<String>>();
    
    private ArrayList<Pattern> patterns = new ArrayList<Pattern>();
    private int resultNum;
    
    public SpouseRelationExtractor(){
        Pattern p11 = Pattern.compile("(丈夫|老公)/n [^//s]{2,10}(/nr|/pr|/si|/sir|/x|/spr)");
	patterns.add(p11);
        Pattern p12 = Pattern.compile("(妻子|老婆)/n [^//s]{2,10}(/nr|/pr|/si|/sir|/x|/spr)");
	patterns.add(p12);
	Pattern p31 = Pattern.compile("(夫|丈夫|老公)/n (是/vshi|名叫/v|叫/v|为/p) ([^//s]{1,10}/[a-z] )*[^//s]{2,10}(/nr|/pr|/si|/sir|/x|/spr)");
	patterns.add(p31);
        Pattern p32 = Pattern.compile("(妻|妻子|老婆)/n (是/vshi|名叫/v|叫/v|为/p) ([^//s]{1,10}/[a-z] )*[^//s]{2,10}(/nr|/pr|/si|/sir|/x|/spr)");
	patterns.add(p32);
        Pattern p41 = Pattern.compile("[^//s]{1,10}(/nr[a-z]*|/pr|/si|/sir|/x|/spr) 的/ude1 (妻子|老婆)/n");
        patterns.add(p41);
        Pattern p42 = Pattern.compile("[^//s]{1,10}(/nr[a-z]*|/pr|/si|/sir|/x|/spr) 的/ude1 (丈夫|老公)/n");
        patterns.add(p42);
        Pattern p5 = Pattern.compile("[^//s]{1,10}(/nr|/pr|/si|/n|/sir|/x|/spr) 之/uzhi 妻/ng");
        patterns.add(p5);
        Pattern p6 = Pattern.compile("[^//s]{1,10}(/nr|/pr|/si|/n|/sir|/x|/spr) 之/uzhi 夫/ng");
        patterns.add(p6);
        Pattern p7 = Pattern.compile("[^//s]{2,10}(/nr|/pr|/si|/sir|/spr|/x) ([^//s]{1,10}/[a-z] )*结婚");
        patterns.add(p7);
    }
    
    @Override
    public String extract(int storeName) {
        try {
            File prIDF = new File(this.sprIDFile);
            File prEntityF = new File(Util.ENTITY_FOLDER + "entity.spouse.relation");
            File prSegmentedF = new File(this.sprSegmentedFile);
            if(!prIDF.exists()) {
                return Util.systemTime() + "  File \"" + this.sprIDFile + "\" doesn't exist!\n" + "Please generate user dict first!\n";
            }
            if(!prEntityF.exists()) {
                return Util.systemTime() + "  File \"" + this.sprEntityFile + "\" doesn't exist!\n";
            }
            if(!prSegmentedF.exists()) {
                return Util.systemTime() + "  File \"" + this.sprSegmentedFile + "\" doesn't exist!\n" + "Please participle entity first!\n";
            }
            this.loadID();
            BufferedReader entityReader = new BufferedReader(new FileReader(Util.ENTITY_FOLDER + "entity.spouse.relation"));
            BufferedReader segReader = new BufferedReader(new FileReader(this.sprSegmentedFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.relationFile));
            String line = "";
            while((line = segReader.readLine()) != null) {
                String[] p = line.split("\t");
                //String[] p2 = segReader.readLine().split("\t");
                String[] result = this.relation(p[3], p[3]);
                for(String r : result) {
                    if(storeName == 1) {
                        writer.write(p[1] + "-spouse-" + r);
                        writer.newLine();
                    } else {
                        if(this.hmSpRID.containsKey(r)) {
                            for(String a : this.hmSpRID.get(r)) {
                                writer.write(p[0] + "-spouse-" + a);
                                System.out.println(p[0] + "-spouse-" + a);
                                writer.newLine();
                            }
                        }
                    }
                }
            }            System.out.println("The number of result is:" + resultNum);
            entityReader.close();
            segReader.close();
            writer.close();
            return Util.systemTime() + "  Done. Relation file \"" + this.relationFile + "\" has been created.\n";
        } catch (IOException ex) {
            Logger.getLogger(SpouseRelationExtractor.class.getName()).log(Level.SEVERE, null, ex);
            return ex.toString();
        }
    }

    @Override
    public void loadID() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.sprIDFile));
            String line = "";
            while((line = reader.readLine()) != null) {
                String[] p = line.split("\t");
                if(this.hmSpRID.containsKey(p[1])) {
                    this.hmSpRID.get(p[1]).add(p[0]);
                } else {
                    this.hmSpRID.put(p[1], new ArrayList<String>());
                    this.hmSpRID.get(p[1]).add(p[0]);
                }
            }
            reader.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SpouseRelationExtractor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SpouseRelationExtractor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String[] relation(String sentence, String partiSentence) {
        boolean isFind = false;
        ArrayList<String> result = new ArrayList<String>();
        for(int i = 0; i < patterns.size(); i ++){
            Matcher m = patterns.get(i).matcher(partiSentence);
            while(m.find()){
                isFind = true;
                String matchString = m.group();//得到符合规则的String
                Pattern p = Pattern.compile("\\s[^//s]{2,10}(/nr|/pr|/si|/n|/sir|/x|/spr)");
                Matcher mm = p.matcher(matchString);
                if(mm.find()){
                    String nameString = mm.group();
                    nameString = nameString.trim();
                    String[] nameArray;
                    if (nameString.contains("/nr")) {
                        nameArray = nameString.split("/nr");
                    }
                    else if (nameString.contains("/n")){
                        nameArray = nameString.split("/n");
                    }
                    else if (nameString.contains("/pr")) {
                        nameArray = nameString.split("/pr");
                    }
                    else if (nameString.contains("/si")) {
                        nameArray = nameString.split("/si");
                    }
                    else if (nameString.contains("/sir")){
                        nameArray = nameString.split("/sir");
                    }
                    else if (nameString.contains("/spr"))  {
                        nameArray = nameString.split("/spr");
                    }
                    else {
                        nameArray = nameString.split("/x");
                    }
                    for(String s : nameArray){
                        result.add(s.replace("/", "").replace("w", "").replace("n", "").replace("、", "").trim());
                    }
                    resultNum++;
                }
                if(isFind)
                  break;
            }
            if(isFind)
		break;
	}
        String[] results = new String[result.size()];
        int i = 0;
        for(String r : result) {
            results[i++] = r;
        }
        return results;
    }
}
