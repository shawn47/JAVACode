/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mds.hw1.extractor;

/**
 *
 * @author picry
 */
public interface RelationExtractor {
    public String extract(int storeName);
    public void loadID();
    public String[] relation(String sentence, String partiSentence);
}
