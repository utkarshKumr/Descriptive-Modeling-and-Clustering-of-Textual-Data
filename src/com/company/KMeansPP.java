package com.company;

import java.lang.reflect.Array;
import com.company.PrecisionRecall;
import java.util.*;

public class KMeansPP {
    List<ArrayList<Double>> tfIDFMatrix;

    HashMap<String, ArrayList<Double>> prevClusterList = new HashMap();
    HashMap<String, ArrayList<Double>> currClusterList = new HashMap();
    int numberOfClusters;
    Integer M;
    Integer N;
    char mode;
    Map<String, ArrayList<Integer>> classificationHash = new HashMap<String, ArrayList<Integer>>();

    public ArrayList<Double> findCluster(int number) {
        if(number == 0){
            return tfIDFMatrix.get(22);
        }
        double maxD=0;
        int maxi=0;
        ArrayList<Double> sums= new ArrayList<Double>(Collections.nCopies(tfIDFMatrix.size(), 0.0));
        for (int i=0;i<number;i++){
            ArrayList<Double> centroid = currClusterList.get("C"+(i+1));
            for (int j=0;j<tfIDFMatrix.size();j++){
                double d=eucledianDistance(centroid,tfIDFMatrix.get(j));
                sums.set(j,sums.get(j)+d);
            }
        }

        for (int i=0;i<sums.size();i++){
            if (sums.get(i)>maxD){
                    maxD=sums.get(i);
                    maxi=i;
                }
        }
        return tfIDFMatrix.get(maxi);
    }

    KMeansPP(Integer numberOfDocs, Integer numberOfTokens, List<ArrayList<Double>> matrix, char modeVal, int clusters){
        tfIDFMatrix= matrix;
        numberOfClusters=clusters;
        for(int i=0;i<numberOfClusters;i++){
            currClusterList.put("C"+(i+1), findCluster(i));
            classificationHash.put("C"+(i+1),new ArrayList<Integer>());
            prevClusterList.put("C"+(i+1), new ArrayList<Double>());
        }
        M=numberOfDocs;
        N=numberOfTokens;
        mode=modeVal;

    }



    public static double eucledianDistance(ArrayList<Double> point1, ArrayList<Double> point2)
    {
        if (point1.size()!=point2.size()){
            double inf = Double.POSITIVE_INFINITY;
            return inf;
        }
        double sum = 0.0;
        for(int i = 0; i < point1.size(); i++)
        {
            sum += ((point1.get(i) - point2.get(i)) * (point1.get(i) - point2.get(i)));
        }
        return Math.sqrt(sum);
    }


    public static double cosineSimilarityF(ArrayList<Double> vectorA, ArrayList<Double> vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vectorA.size(); i++) {
            dotProduct += vectorA.get(i) * vectorB.get(i);
            normA += Math.pow(vectorA.get(i), 2);
            normB += Math.pow(vectorB.get(i), 2);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    public ArrayList<Double> findMeanVector(ArrayList<Integer> list){
        int number=list.size();
        ArrayList<Double> sum_= new ArrayList<Double>(Collections.nCopies(N, 0.0));

        for (int l:list){
            ArrayList<Double> vector=tfIDFMatrix.get(l);
            for (int i=0;i<sum_.size();i++){
                sum_.set(i, sum_.get(i)+vector.get(i));
            }
        }
        for (int i=0;i<sum_.size();i++){
            sum_.set(i, sum_.get(i)/number);
        }
        return sum_;
    }

    public void eucledianSimilarity(int index){

        double min_= Double.POSITIVE_INFINITY;
        int minIndex = 0;
        int j = 0;
        for (j=0; j<numberOfClusters; j++){
            double d=eucledianDistance(tfIDFMatrix.get(index),currClusterList.get("C"+(j+1)));
            if (d<min_) {
                min_ = d;
                minIndex = j;
            }
        }
        classificationHash.get("C"+(minIndex+1)).add(index);
    }

    public void cosineSimilarity(int index){
        double max_= 0.0;
        int maxIndex = 0;
        int j = 0;
        for (j=0; j<numberOfClusters; j++){
            double d=cosineSimilarityF(tfIDFMatrix.get(index),currClusterList.get("C"+(j+1)));
            if (d>max_) {
                max_ = d;
                maxIndex = j;
            }
        }
        classificationHash.get("C"+(maxIndex+1)).add(index);
    }

    public void process(){
        int c=10;
        while(c>0) {
            for(int i=0;i<numberOfClusters;i++){
                classificationHash.put("C"+(i+1),new ArrayList<Integer>());
            }
            for (int i=0;i<tfIDFMatrix.size();i++) {
                if (mode=='e'){
                    eucledianSimilarity(i);
                } else {
                    cosineSimilarity(i);
                }
            }
            for(int i=0;i<numberOfClusters;i++){
                prevClusterList.put("C"+(i+1), new ArrayList<Double>(currClusterList.get("C"+(i+1))));
                currClusterList.put("C"+(i+1), findMeanVector(classificationHash.get("C"+(i+1))));
            }
            c--;
        }
        System.out.println("Final classifications:");
        List<HashSet<Integer>> classifications = new ArrayList<>();
        for (int i=0;i<numberOfClusters;i++){
            classifications.add(new HashSet<>());
        }
        PrecisionRecall pr=new PrecisionRecall();
        for (String name: classificationHash.keySet()) {
            ArrayList<Integer> value = classificationHash.get(name);
            if (value.contains(0) || value.contains(1) || value.contains(2))
                classifications.set(0,new HashSet(value));
            else if (value.contains(9) || value.contains(10) || value.contains(11)){
                classifications.set(1,new HashSet(value));
            } else{
                classifications.set(2,new HashSet(value));
            }
            System.out.println(value);
        }
        if (numberOfClusters==3 && mode=='c') {
            pr.findPrecisionRecall(classifications);
        }
    }

}