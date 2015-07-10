package bigramModel;



import util.PriorityQueue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class bigram {

    Map<String, Double> bigrams;
    Map<String, Double> unigrams;
    Map<String, Double> probs;
    util.PriorityQueue<String> final_probs;
    double uniCount;


    public bigram() {
        bigrams = new HashMap<String, Double>();
        unigrams = new HashMap<String, Double>();
        probs = new HashMap<String, Double>();
        final_probs = new util.PriorityQueue<String>();
        uniCount = 0;

    }

    public String getToken1(String key) {
        String token;
        String[] parts = key.split("\t");
        token = parts[0];
        //System.out.println( "Token: " +token);

        return token;
    }

    public String getToken2(String key) {
        String token;
        String[] parts = key.split("\t");
        token = parts[1];
        //System.out.println( "Token: " +token);

        return token;
    }

    /*
	public PriorityQueue<String, Double> asPriorityQueue(){

		PriorityQueue<String, Double> pq = new PriorityQueue<String, Double>(bigrams.size());
		for(Map.Entry<String, Double> m : bigrams.entrySet()){
			pq.
		}

		return pq;
	}
	*/
    public void train(List<String> passwords) {
        countUnigrams(passwords);
        countBigrams(passwords);
        calculateProbs();

    }

    public void calculateProbs() {

        //bigramCount / first token's count
        for (String s : bigrams.keySet()) {

            String uni = getToken1(s);

            if (unigrams.containsKey(uni)) {
                double prob = bigrams.get(s) / unigrams.get(uni);
                probs.put(s, prob);
            }
        }
        //System.out.println("Bigram Count: " + biCount);
    }


    public void countUnigrams(List<String> passwords) {
        int rank = 1;
        Double demon = 0.0;
        for (int ii = 1; ii < 32000001; ii++) {
            demon = +1 / (Math.pow(ii, .8265));
        }
        for (String s : passwords) {
            Double count = ((1 / Math.pow(rank, .8265)) / demon) * 32000000;
            for (int ii = 0; ii < s.length(); ii++) {
                char c = s.charAt(ii);
                String character = String.valueOf(c);

                if (!unigrams.containsKey(character)) {
                    unigrams.put(character, count);
                } else {
                    double old = unigrams.get(character);
                    unigrams.put(character, old + count);
                }
                rank++;
                uniCount++;
            }
        }
    }


    public void countBigrams(List<String> passwords) {
        int rank = 1;
        Double demon = 0.0;
        for (int ii = 1; ii < 32000001; ii++) {
            demon = +1 / (Math.pow(ii, .8265));
        }

        for (String s : passwords) {
            Double count = ((1 / Math.pow(rank, .8265)) / demon) * 32000000;
            for (int ii = 1; ii < s.length(); ii++) {

                char c1 = s.charAt(ii - 1);
                char c2 = s.charAt(ii);
                String character = String.valueOf(c1);
                String prev = String.valueOf(c2);

                String fullBigram = prev + "\t" + character;
                if (!bigrams.containsKey(fullBigram)) {
                    bigrams.put(fullBigram, count);
                } else {
                    double old = bigrams.get(fullBigram);
                    bigrams.put(fullBigram, old + count);
                }
            }
            rank++;
        }

    }

    public List<String> readBigrams(InputStream file){
        List<String> bigrams_1 = new ArrayList<String>();
        //System.out.println("Here");

        try {
            BufferedReader read = new BufferedReader(new InputStreamReader(file, "UTF-8"));
            String line;

            while ((line = read.readLine()) != null) {

                bigrams_1.add(line);

            }
        read.close();
        } catch (FileNotFoundException e) {
            System.err.println(e);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bigrams_1;
    }


    public List<String> readPasswords(File file) {
        List<String> passwords = new ArrayList<String>();
        //System.out.println("Here");

        try {
            BufferedReader read = new BufferedReader(new FileReader(file));
            String line;
            while ((line = read.readLine()) != null) {

                passwords.add(line);
                //System.out.println(line);
                //passwords.add(pass);

            }
            read.close();
        } catch (FileNotFoundException e) {
            System.err.println(e);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return passwords;
    }

    public void writeFile(String path) throws IOException {
        FileWriter fw = new FileWriter(path);
        Set keys = probs.keySet();
        for (Iterator ii = keys.iterator(); ii.hasNext(); ) {
            String key = (String) ii.next();
            Double value = probs.get(key);
            fw.write(key + ":" + value + "\n");
        }


        fw.close();
    }

    public String[] getBadKeys(String value, List<String> bi_probs) {
        //System.out.println(bi_probs);
        for (Iterator ii = bi_probs.listIterator(); ii.hasNext(); ) {
            String s = (String) ii.next();
            String[] pair = s.split("\t");
            //System.out.println(pair[0] + "\t" + pair[1]);
            String firstToken = pair[0];
            String secondToken = pair[1];
            if (value.equals(firstToken) && secondToken != null) {
                final_probs.add(secondToken, Double.parseDouble(pair[2]));
            }

        }
       // System.out.println(final_probs);
        String[] badKeys = new String[3];
        badKeys = final_probs.next3Unchanged();
        System.out.println(badKeys[0] + badKeys[1] + badKeys[2]);
        return badKeys;
    }

    public ArrayList<ArrayList<String>> getKeyColorList(String value, List<String> bi_probs){
        ArrayList<String> bad = new ArrayList<>();
        ArrayList<String> bad_avg = new ArrayList<>();
        ArrayList<String> avg = new ArrayList<>();
        ArrayList<String> avg_good = new ArrayList<>();
        ArrayList<String> good = new ArrayList<>();


        for (Iterator ii = bi_probs.listIterator(); ii.hasNext(); ) {
            String s = (String) ii.next();
            String[] pair = s.split("\t");
            //System.out.println(pair[0] + "\t" + pair[1]);
            String firstToken = pair[0];
            String secondToken = pair[1];
            if (value.equals(firstToken) && secondToken != null) {
                if(Double.parseDouble(pair[2]) > .2){
                    bad.add(secondToken);
                }
                else if ((Double.parseDouble(pair[2]) <= .2) && (Double.parseDouble(pair[2]) > .02))
                {
                    bad_avg.add(secondToken);
                }
                else if ((Double.parseDouble(pair[2]) <= .02) && (Double.parseDouble(pair[2]) > .002)){
                    avg.add(secondToken);
                }
                else if ((Double.parseDouble(pair[2]) <= .002) && (Double.parseDouble(pair[2]) > .0002)){
                    avg_good.add(secondToken);
                }
                else {
                    good.add(secondToken);
                }

            }

        }
        ArrayList<ArrayList<String>> master = new ArrayList<>();
        master.add(bad);
        master.add(bad_avg);
        master.add(avg);
        master.add(avg_good);
        master.add(good);
        //System.out.println(master.get(0));

    return master;
    }

    public static void main(String args[]) {

        bigram b = new bigram();
        System.out.println(System.getProperty("user.dir"));
        File rockyou = new File("rockyou.txt");
        List<String> passwords = b.readPasswords(rockyou);
        System.out.println("Done Reading....");
        //System.out.println(passwords);

        b.train(passwords);

        System.out.println("Done Training...");

        //Printout of Bigram probs, still need to sort.
    }
}