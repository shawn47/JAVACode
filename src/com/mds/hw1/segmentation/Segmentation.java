/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mds.hw1.segmentation;

import ICTCLAS.I3S.AC.ICTCLAS50;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author picry
 */
public class Segmentation {
    protected ICTCLAS50 segmentation;
    protected boolean init;
    
    public Segmentation() {
        this.segmentation = new ICTCLAS50();
        this.initialise();
    }
    
    public void initialise() {
        try {
            this.segmentation = new ICTCLAS50();
            String argu = ".";
            if(this.segmentation.ICTCLAS_Init(argu.getBytes("UTF-8")) == false) {
                this.init = false;
                System.out.println("Init fails!");
            } else {
                this.init = true;
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Segmentation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void clear() {
        this.segmentation.ICTCLAS_Exit();
    }
    
    public void loadUserDict(String dict) {
        if(this.init == false) {
            return;
        }
        byte[] usrdirb = dict.getBytes();
        int n = this.segmentation.ICTCLAS_ImportUserDictFile(usrdirb, 3);
        System.out.print("用户词典导入个数：");
        System.out.println(n);
    }
    
    public String segmentation(String sentence) {
        try {
            if(this.init == false) {
                return sentence;
            }
            byte[] nativeBytes = this.segmentation.ICTCLAS_ParagraphProcess(sentence.getBytes("UTF-8"), 3, 1);
            String result = new String(nativeBytes, 0, nativeBytes.length, "UTF-8");
            return result;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Segmentation.class.getName()).log(Level.SEVERE, null, ex);
            return sentence;
        }
    }
}