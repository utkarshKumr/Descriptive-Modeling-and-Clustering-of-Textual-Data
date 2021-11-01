package com.company;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.*;

public class PrecisionRecall {



    public void findPrecisionRecall(List<HashSet<Integer>> classifications){
        List<HashSet<Integer>> originals = new ArrayList<>();
        Integer arr[] = { 0,1,2,3,4,5,6,7};
        originals.add(new HashSet<Integer>(Arrays.asList(arr)));
        Integer arr2[] = { 8,9,10,11,12,13,14,15};
        originals.add(new HashSet<Integer>(Arrays.asList(arr2)));
        Integer arr3[] = { 16,17,18,19,20,21,22,23};
        originals.add(new HashSet<Integer>(Arrays.asList(arr3)));

        double[][] confusionMat=new double[classifications.size()][originals.size()];
        double sumRows[] = new double[originals.size()];
        double sumCols[] = new double[classifications.size()];
        System.out.println("Confusion Matrix");
        for (int i=0;i<classifications.size();i++){
            for (int j=0; j<originals.size();j++){
                HashSet<Integer> intersection = new HashSet<Integer>(classifications.get(i)); // use the copy constructor
                intersection.retainAll(originals.get(j));
                confusionMat[i][j]=intersection.size();
                sumRows[i]+=confusionMat[i][j];
                sumCols[j]+=confusionMat[i][j];
                System.out.print(confusionMat[i][j]+" , ");
            }
            System.out.println();
        }

        double precision=0;
        double recall=0;

        for (int i=0;i<classifications.size();i++) {
            precision+=confusionMat[i][i]/sumRows[i];
            recall+=confusionMat[i][i]/sumCols[i];
        }
        precision=precision/classifications.size();
        recall=recall/classifications.size();

        double fMeasure=2*(precision*recall)/(precision+recall);

        System.out.println("Precision = "+precision);
        System.out.println("Recall = "+recall);
        System.out.println("F-measure = "+fMeasure);
    }
}
