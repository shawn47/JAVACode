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
public class ParentsRelationExtractor implements RelationExtractor {
    public static String relationCategory = "parent relation";
    public String prIDFile = Util.USER_DICT_FOLDER + "id.parent,relation";
    public String prEntityFile = Util.ENTITY_FOLDER + "entity.parent.relation";
    public String prSegmentedFile = Util.PARTICIPLE_FOLDER + "segmented.parent.relation";
    public String relationFile = Util.RELATION_FOLDER + ParentsRelationExtractor.relationCategory;
    protected HashMap<String, ArrayList<String>> hmPRID = new HashMap<String, ArrayList<String>>();
    
    private ArrayList<Pattern> patterns = new ArrayList<Pattern>();
    private int resultNum;
    
    public ParentsRelationExtractor(){
        Pattern p11 = Pattern.compile("(母亲|妈妈)/n [^//s]{2,20}(/nr|/pr|/si|/spr|/sir|/x)");
	patterns.add(p11);
        Pattern p12 = Pattern.compile("(父亲|爸爸)/n [^//s]{2,20}(/nr|/pr|/si|/spr|/sir|/x)");
	patterns.add(p12);
	Pattern p21 = Pattern.compile("(父亲|爸爸)/n (，/wd)\\s[^//s]{2,20}(/nr|/pr|/si|/spr|/sir|/x)");
	patterns.add(p21);
        Pattern p22 = Pattern.compile("(母亲|妈妈)/n (，/wd)\\s[^//s]{2,20}(/nr|/pr|/si|/spr|/sir|/x)");
	patterns.add(p22);
	Pattern p31 = Pattern.compile("(父亲|爸爸)/n (是/vshi|名叫/v|叫/v|为/p) ([^//s]{1,20}/[a-z] )*[^//s]{2,10}(/nr|/pr|/si|/sir|/spr|/x)");
	patterns.add(p31);
        Pattern p32 = Pattern.compile("(母亲|妈妈)/n (是/vshi|名叫/v|叫/v|为/p) ([^//s]{1,20}/[a-z] )*[^//s]{2,10}(/nr|/pr|/si|/sir|/spr|/x)");
	patterns.add(p32);
        Pattern p41 = Pattern.compile("[^//s]{1,20}(/nr[a-z]*|/pr|/si|/sir|/spr|/x) 的/ude1(母亲|妈妈)/n");
        patterns.add(p41);
        Pattern p42 = Pattern.compile("[^//s]{1,20}(/nr[a-z]*|/pr|/si|/sir|/spr|/x) 的/ude1(父亲|爸爸)/n");
        patterns.add(p42);
        Pattern p5 = Pattern.compile("[^//s]{1,20}(/nr|/pr|/si|/sir|/n|/spr|/x) 之/uzhi 女/ng");
        patterns.add(p5);
        Pattern p6 = Pattern.compile("[^//s]{1,20}(/nr|/pr|/si|/sir|/n|/spr|/x) 之/uzhi 子/ng");
        patterns.add(p6);
    }
    
    @Override
    public String extract(int storeName) {
        try {
            File prIDF = new File(this.prIDFile);
            File prEntityF = new File(Util.ENTITY_FOLDER + "entity.parent.relation");
            File prSegmentedF = new File(this.prSegmentedFile);
            if(!prIDF.exists()) {
                return Util.systemTime() + "  File \"" + this.prIDFile + "\" doesn't exist!\n" + "Please generate user dict first!\n";
            }
            if(!prEntityF.exists()) {
                return Util.systemTime() + "  File \"" + this.prEntityFile + "\" doesn't exist!\n";
            }
            if(!prSegmentedF.exists()) {
                return Util.systemTime() + "  File \"" + this.prSegmentedFile + "\" doesn't exist!\n" + "Please participle entity first!\n";
            }
            this.loadID();
            BufferedReader entityReader = new BufferedReader(new FileReader(Util.ENTITY_FOLDER + "entity.parent.relation"));
            BufferedReader segReader = new BufferedReader(new FileReader(this.prSegmentedFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.relationFile));
            String line = "";
            while((line = segReader.readLine()) != null) {
                String[] p = line.split("\t");
                //String[] p2 = segReader.readLine().split("\t");
                String[] result = this.relation(p[3], p[3]);
                for(String r : result) {
                    if(storeName == 1) {
                        writer.write(p[1] + "-parent-" + r);
                        writer.newLine();
                    } else {
                        if(this.hmPRID.containsKey(r)) {
                            for(String a : this.hmPRID.get(r)) {
                                writer.write(p[0] + "-parent-" + a);
                                System.out.println(p[0] + "-parent-" + a);
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
            Logger.getLogger(ParentsRelationExtractor.class.getName()).log(Level.SEVERE, null, ex);
            return ex.toString();
        }
    }

    @Override
    public void loadID() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.prIDFile));
            String line = "";
            while((line = reader.readLine()) != null) {
                String[] p = line.split("\t");
                if(this.hmPRID.containsKey(p[1])) {
                    this.hmPRID.get(p[1]).add(p[0]);
                } else {
                    this.hmPRID.put(p[1], new ArrayList<String>());
                    this.hmPRID.get(p[1]).add(p[0]);
                }
            }
            reader.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ParentsRelationExtractor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ParentsRelationExtractor.class.getName()).log(Level.SEVERE, null, ex);
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
                Pattern p = Pattern.compile("\\s[^//s]{2,20}(/nr|/pr|/si|/sir|/spr|/x)");
                Matcher mm = p.matcher(matchString);
                if(mm.find()){
                    String nameString = mm.group();
                    nameString = nameString.trim();
                    String[] nameArray;
                    if (nameString.contains("/nr")) {
                        nameArray = nameString.split("/nr");
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
//                if(isFind)
//                  break;
            }
//            if(isFind)
//		break;
	}
        String[] results = new String[result.size()];
        int i = 0;
        for(String r : result) {
            results[i++] = r;
        }
        return results;
    }
}
