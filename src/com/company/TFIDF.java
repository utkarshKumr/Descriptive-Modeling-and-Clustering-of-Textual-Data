package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TFIDF {
    static List<Double> IDFMap = new ArrayList<Double>();

    public void findIDFs(List<ArrayList<String>> docs, List<String> allTokens) {
        double size=docs.size();
        for (String token : allTokens) {
            double n=0;
            for (List<String> doc: docs) {
                for (String word: doc){
                    if (token.equalsIgnoreCase(word)){
                        n++;
                        break;
                    }
                }
            }
            IDFMap.add(Math.log(size/n));
        }

    }

    public double findTFIDF(List<String> doc, String term, Integer index){
        double count=0;
        for (String word: doc){
            if (word.equalsIgnoreCase(term)){
                count++;
            }
        }
        double tf=count/doc.size();
        double idf=IDFMap.get(index);
        return tf*idf;
    }



}
