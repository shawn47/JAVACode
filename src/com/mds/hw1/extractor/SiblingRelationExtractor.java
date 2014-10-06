/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mds.hw1.extractor;

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
public class SiblingRelationExtractor implements RelationExtractor{
    public static String relationCategory = "sibling relation";
    public String sprIDFile = Util.USER_DICT_FOLDER + "id.sibling,relation";
    public String sprEntityFile = Util.ENTITY_FOLDER + "entity.sibling.relation";
    public String sprSegmentedFile = Util.PARTICIPLE_FOLDER + "segmented.sibling.relation";
    public String relationFile = Util.RELATION_FOLDER + SiblingRelationExtractor.relationCategory;
    protected HashMap<String, ArrayList<String>> hmSRID = new HashMap<String, ArrayList<String>>();
    
    private ArrayList<Pattern> patterns = new ArrayList<Pattern>();
    private int resultNum;
    
    public SiblingRelationExtractor(){
        Pattern p11 = Pattern.compile("(哥哥|表哥|哥|兄|兄长)/n [^//s]{2,10}(/nr|/pr|/si|/sir|/x|/spr)");
	patterns.add(p11);
        Pattern p12 = Pattern.compile("(姐姐|表姐|姐|姊姊|姊)/n [^//s]{2,10}(/nr|/pr|/si|/sir|/x|/spr)");
	patterns.add(p12);
        Pattern p21 = Pattern.compile("(弟弟|表弟|弟|堂弟)/n [^//s]{2,10}(/nr|/pr|/si|/sir|/x|/spr)");
	patterns.add(p21);
        Pattern p22 = Pattern.compile("(妹妹|表妹|堂妹|妹)/n [^//s]{2,10}(/nr|/pr|/si|/sir|/x|/spr)");
	patterns.add(p22);
	Pattern p31 = Pattern.compile("(哥哥|表哥|哥|兄)/n (是/vshi|名叫/v|叫/v|为/p) ([^//s]{1,10}/[a-z] )*[^//s]{2,10}(/nr|/pr|/si|/sir|/x|/spr)");
	patterns.add(p31);
        Pattern p32 = Pattern.compile("(姐姐|表姐|姐|姊姊|姊)/n (是/vshi|名叫/v|叫/v|为/p) ([^//s]{1,10}/[a-z] )*[^//s]{2,10}(/nr|/pr|/si|/sir|/x|/spr)");
	patterns.add(p32);
        Pattern p33 = Pattern.compile("(弟弟|表弟|弟|堂弟)/n (是/vshi|名叫/v|叫/v|为/p) ([^//s]{1,10}/[a-z] )*[^//s]{2,10}(/nr|/pr|/si|/sir|/x|/spr)");
	patterns.add(p33);
        Pattern p34 = Pattern.compile("(妹妹|表妹|堂妹|妹)/n (是/vshi|名叫/v|叫/v|为/p) ([^//s]{1,10}/[a-z] )*[^//s]{2,10}(/nr|/pr|/si|/sir|/x|/spr)");
	patterns.add(p34);
        Pattern p41 = Pattern.compile("[^//s]{1,10}(/nr[a-z]*|/pr|/si|/sir|/x|/spr) 的/ude1 (哥哥|表哥|哥|兄)/n");
        patterns.add(p41);
        Pattern p42 = Pattern.compile("[^//s]{1,10}(/nr[a-z]*|/pr|/si|/sir|/x|/spr) 的/ude1 (姐姐|表姐|姐|姊姊|姊)/n");
        patterns.add(p42);
        Pattern p43 = Pattern.compile("[^//s]{1,10}(/nr[a-z]*|/pr|/si|/sir|/x|/spr) 的/ude1 (弟弟|表弟|弟|堂弟)/n");
        patterns.add(p43);
        Pattern p44 = Pattern.compile("[^//s]{1,10}(/nr[a-z]*|/pr|/si|/sir|/x|/spr) 的/ude1 (妹妹|表妹|堂妹|妹)/n");
        patterns.add(p44);
        Pattern p5 = Pattern.compile("[^//s]{1,10}(/nr|/pr|/si|/n|/sir|/x|/spr) 之/uzhi 兄/ng");
        patterns.add(p5);
        Pattern p6 = Pattern.compile("[^//s]{1,10}(/nr|/pr|/si|/n|/sir|/x|/spr) 之/uzhi 姐/ng");
        patterns.add(p6);
        Pattern p7 = Pattern.compile("[^//s]{1,10}(/nr|/pr|/si|/n|/sir|/x|/spr) 之/uzhi 弟/ng");
        patterns.add(p7);
        Pattern p8 = Pattern.compile("[^//s]{1,10}(/nr|/pr|/si|/n|/sir|/x|/spr) 之/uzhi 妹/ng");
        patterns.add(p8);
    }
    
    @Override
    public String extract(int storeName) {
        try {
            File prIDF = new File(this.sprIDFile);
            File prEntityF = new File(Util.ENTITY_FOLDER + "entity.sibling.relation");
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
            BufferedReader entityReader = new BufferedReader(new FileReader(Util.ENTITY_FOLDER + "entity.sibling.relation"));
            BufferedReader segReader = new BufferedReader(new FileReader(this.sprSegmentedFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.relationFile));
            String line = "";
            while((line = segReader.readLine()) != null) {
                String[] p = line.split("\t");
                //String[] p2 = segReader.readLine().split("\t");
                String[] result = this.relation(p[3], p[3]);
                for(String r : result) {
                    if(storeName == 1) {
                        writer.write(p[1] + "-sibling-" + r);
                        writer.newLine();
                    } else {
                        if(this.hmSRID.containsKey(r)) {
                            for(String a : this.hmSRID.get(r)) {
                                writer.write(p[0] + "-sibling-" + a);
                                System.out.println(p[0] + "-sibling-" + a);
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
            Logger.getLogger(SiblingRelationExtractor.class.getName()).log(Level.SEVERE, null, ex);
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
                if(this.hmSRID.containsKey(p[1])) {
                    this.hmSRID.get(p[1]).add(p[0]);
                } else {
                    this.hmSRID.put(p[1], new ArrayList<String>());
                    this.hmSRID.get(p[1]).add(p[0]);
                }
            }
            reader.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SiblingRelationExtractor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SiblingRelationExtractor.class.getName()).log(Level.SEVERE, null, ex);
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
