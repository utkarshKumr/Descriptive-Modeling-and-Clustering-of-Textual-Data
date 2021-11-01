package com.company;
import com.company.Process;
import com.company.TFIDF;
import com.company.KMeans;
import com.company.KMeansPP;
import java.util.*;
import java.io.FileWriter;
import java.io.IOException;


public class Main {
    public static HashMap<Integer, Double> sortByValue(HashMap<Integer, Double> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<Integer, Double> > list =
                new LinkedList<Map.Entry<Integer, Double> >(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<Integer, Double> >() {
            public int compare(Map.Entry<Integer, Double> o1,
                               Map.Entry<Integer, Double> o2)
            {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<Integer, Double> temp = new LinkedHashMap<Integer, Double>();
        Collections.reverse(list);
        for (Map.Entry<Integer, Double> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }



    public static void printKeywords(List<ArrayList<Double>> matrix, List<String> allTokens, int numberOfClassifications) {
        int numberOfTokens = allTokens.size();
        HashMap<Integer, Double> keywordsSortingHash = new HashMap<Integer, Double>();
        int docsPerClass=matrix.size()/numberOfClassifications;
        for (int k=0;k<numberOfClassifications;k++) {
            for (int j = 0; j < numberOfTokens; j++) {
                double sum = 0;
                for (int i = k*docsPerClass; i < Math.min((k+1)*docsPerClass,matrix.size()); i++) {
                    sum += matrix.get(i).get(j);
                }
                keywordsSortingHash.put(j, sum);
            }
            Map<Integer, Double> hm1 = sortByValue(keywordsSortingHash);
            Set<String> hash_Set = new HashSet<String>();
            for (Map.Entry<Integer, Double> en :
                    hm1.entrySet()) {
                hash_Set.add(allTokens.get(en.getKey()));
                if (hash_Set.size()>5){
                    break;
                }
            }
            System.out.println("Category"+(k+1)+" keywords:");
            System.out.println(hash_Set);
            System.out.println("\n");
        }
    }

    public static void generateTextFile(List<ArrayList<Double>> newMatrix) {
        try {
            FileWriter writer = new FileWriter("output.txt", true);
            writer.write("[");
            for (int i=0;i<newMatrix.size();i++){
                writer.write("[");
               for (int j=0;j<newMatrix.get(i).size();j++){
                    writer.write(newMatrix.get(i).get(j).toString());
                    if (j!=newMatrix.get(i).size()-1){
                        writer.write(",");
                    }
                }
                writer.write("],");
            }
            writer.write("]");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            Process processInputs = new Process("src/com/company/paths.txt");
            List<String> l1 = processInputs.readPaths("src/com/company/paths.txt");
            List<String> allTokens = new ArrayList<String>();
            List<ArrayList<String>> tokensInEachDoc = new ArrayList<ArrayList<String>>();
            System.out.println("Enter 'c' for Cosine or 'e' for Eucledian");
            Scanner myObj = new Scanner(System.in);
            char mode = myObj.next().charAt(0);
            Set<String> allTokensSet = new HashSet<String>();

            if (mode=='e'){
                System.out.println("Using Eucledian Similarity\n");
            } else {
                System.out.println("Using Cosine Similarity\n");
            }
            System.out.println("Enter the number of classifications (K)");
            int K = myObj.nextInt();

            System.out.println("Enter 1 for KMeans or 2 for KMeans++ ");
            int kMeansType = myObj.nextInt();

            if (kMeansType==1){
                System.out.println("Using KMeans algorithm\n");
            } else {
                System.out.println("Using KMeans++ algorithm\n");
            }

            for (int i = 0; i < l1.size(); i++) {
                ArrayList<String> tokens = new ArrayList<String>();
                List<String> fileTexts = processInputs.readFiles(l1.get(i));
                for (int j=0;j<fileTexts.size();j++){
                    fileTexts.set(j,fileTexts.get(j).replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase());
                }
                tokens = processInputs.processing(fileTexts);
                for (String t : tokens) {
                    allTokensSet.add(t);
                }
                tokensInEachDoc.add(new ArrayList<>(tokens));
                System.out.println("Processing document "+(i+1));
            }
            TFIDF tfIdfObj=new TFIDF();
            for (String x : allTokensSet)
                allTokens.add(x);
            tfIdfObj.findIDFs(tokensInEachDoc,allTokens);

            double[][] tfIDFMatrix = new double[l1.size()][allTokens.size()];
            for (int i=0;i<tokensInEachDoc.size();i++){
                for (int j=0;j<allTokens.size();j++){
                    tfIDFMatrix[i][j]=tfIdfObj.findTFIDF(tokensInEachDoc.get(i),allTokens.get(j),j);
                }
            }
            List<ArrayList<Double>> newMatrix = new ArrayList<ArrayList<Double>>();
            for(int i=0;i<l1.size();i++){
                newMatrix.add(new ArrayList<Double>());
                for (int j=0;j<allTokens.size();j++){
                    newMatrix.get(i).add(tfIDFMatrix[i][j]);
                }
            }
            printKeywords(newMatrix,allTokens, K);
            if (kMeansType==1){
                KMeansPP kmp=new KMeansPP(l1.size(),allTokens.size(), newMatrix, mode,K);
                kmp.process();
            }
            else {
                KMeansPP kmp = new KMeansPP(l1.size(), allTokens.size(), newMatrix, mode, K);
                kmp.process();
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
