package com.company;
import java.io.*;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.*;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.pipeline.*;

public class Process {
    static List<String> tokens = new ArrayList<>();
    static List<String> lemma = new ArrayList<>();
    static List<String> stopwords = new ArrayList<>();
    static List<String> ner = new ArrayList<>();
    static List<String> cleanedlemma = new ArrayList<>();
    static List<String> cleanedtokens= new ArrayList<>();
    static List<String> cleanedner= new ArrayList<>();
    static List<String> compounds = new ArrayList<>();
    static Map<String, Integer> bigramfreq = new HashMap<String, Integer>();
    static List<String> FilteredCompounds = new ArrayList<String>();
    static ArrayList<String> FinalTokens = new ArrayList<String>();

    Path path_;

    Process(String fPath){
        path_= Paths.get(fPath);
    }

    public List<String> readPaths(String fPath){
        List<String> l1 = new ArrayList<String>();
        try {
            File myObj = new File(fPath);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                l1.add(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return l1;
    }

    public List<String> readFiles(String path) {
        List<String> lineList = new ArrayList<String>();
        try {
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                lineList.add(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return lineList;
    }

    public static void biGrams()
    {

        for(int i =0; i<cleanedlemma.size()-1;i++)
        {
            String temp = cleanedlemma.get(i)+" "+cleanedlemma.get(i+1);
            compounds.add(temp);
        }

    }

    public static void bigramFilter(Map<String, Integer> m)
    {



        for (Map.Entry<String, Integer> val : m.entrySet())
        {
            if(val.getValue()>9)
            {
                FilteredCompounds.add(val.getKey());
            }

        }

    }

    public static void RemoveStopwords()
    {
        for(int i =0; i<tokens.size();i++)
        {
            if(!stopwords.contains(tokens.get(i)))
            {
                cleanedtokens.add(tokens.get(i));
//                cleanedlemma.add(lemma.get(i));
//                cleanedner.add(ner.get(i));
            }

        }
    }

    public static void loadStopwords() throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader("src/com/company/stopwords.txt"));

        try{String s;
            while((s = br.readLine()) != null)
            {
                stopwords.add(s);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        //return stopwords;
    }

    public static Map<String, Integer> countFrequencies(List<String> list)
    {
        // hashmap to store the frequency of element
        Map<String, Integer> hm = new HashMap<String, Integer>();

        for (String i : list) {
            Integer j = hm.get(i);
            hm.put(i, (j == null) ? 1 : j + 1);
        }

        return(hm);
    }

    public ArrayList<String> processing(List <String> texts) throws IOException
    {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        String article="";

        for (int i=0;i<texts.size();i++){
            article+=texts.get(i)+" ";
        }
        tokens.clear();
        cleanedtokens.clear();
        cleanedlemma.clear();
        cleanedner.clear();
        FinalTokens.clear();
        compounds.clear();
        FilteredCompounds.clear();

        loadStopwords();

        CoreDocument document = pipeline.processToCoreDocument(article);

            for (CoreLabel tok: document.tokens()){

                (tokens).add(tok.word());
            }
            RemoveStopwords();
            String cleanedArticle="";
            for (int i=0;i<cleanedtokens.size();i++){
                cleanedArticle+=cleanedtokens.get(i)+" ";
            }
            StanfordCoreNLP pipeline2 = new StanfordCoreNLP(props);
            CoreDocument document2 = pipeline2.processToCoreDocument(cleanedArticle);
            for (CoreLabel tok : document2.tokens())
            {
                cleanedlemma.add(tok.lemma());
                cleanedner.add(tok.ner());
            }

        //Compounds creation
        biGrams();
        bigramfreq = countFrequencies(compounds);
        bigramFilter(bigramfreq);

        FinalTokens.addAll(FilteredCompounds);
        FinalTokens.addAll(cleanedlemma);

        return(FinalTokens);
    }
}
