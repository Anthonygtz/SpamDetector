package com.spamdetector.util;

import com.spamdetector.domain.TestFile;

import java.io.*;
import java.util.*;

/**
 * TODO: This class will be implemented by you
 * You may create more methods to help you organize you strategy and make you code more readable
 */
public class SpamDetector {

    public List<TestFile> trainAndTest(File mainDirectory) throws IOException {
//        TODO: main method of loading the directories and files, training and testing the model

        File ham1 = new File(mainDirectory, "/train/ham"); //create a file that points to the training ham folder
        File ham2 = new File(mainDirectory, "/train/ham2"); //create a file that points to the 2nd training ham folder
        File spamDir = new File(mainDirectory, "/train/spam"); //Create a file that points to the training spam folder

        int numHam1 = ham1.listFiles().length; //integer variable used to hold the number of ham files in the folder
        int numHam2 = ham2.listFiles().length; //integer variable used to hold the number of ham files in the second folder
        int numSpamFiles = spamDir.listFiles().length; //integer variable used to hold the number of spam files in the folder

        Map<String, Integer> trainHam1 = wordFrequencyDir(ham1); //Create a map and determine the frequency of certain words in the ham folder
        Map<String, Integer> trainHam2 = wordFrequencyDir(ham2); //Create a map and determine the frequency of certain words in the second ham folder
        Map<String, Integer> trainSpamFreq = wordFrequencyDir(spamDir); //Create a map and determine the frequency of certain words in the spam folder

        Map<String, Integer> trainHamFreq = wordFrequencyDir(ham1); //Create a map and determine the frequency of certain words in the ham folder

        // Pr(W|H) (probability calculation that a certain word appears in a ham file)
        Map<String, Double> PrWH = new TreeMap<>(); //Create a map that will hold the probability  and word
        Set<String> hamKeys = trainHamFreq.keySet(); //Create a set of keys for the map
        Iterator<String> hamKeyIterator = hamKeys.iterator(); //Create an iterator for the keys
        while(hamKeyIterator.hasNext())
        { //iterate through the keys
            String word = hamKeyIterator.next(); //Get the word
            int freqHam = trainHamFreq.get(word); //Get the frequency that the word had appeared
            double pr = (double) freqHam/numHam1; //Calculate the probability
            PrWH.put(word, pr); //put the word and probability in the map
        }

        // Pr(W|S) (probability that a certain word appears in a spam file)
        Map<String, Double> PrWS = new TreeMap<>(); //Create a map that will hold the probability and word
        Set<String> spamKeys = trainSpamFreq.keySet(); //Create a set of keys for the map
        Iterator<String> spamKeyIterator = spamKeys.iterator(); //Create an iterator for the keys
        while(spamKeyIterator.hasNext())
        { //iterate through the keys
            String word = spamKeyIterator.next(); //Get the word
            int freqSpam = trainSpamFreq.get(word); //Get the frequency that the word had appeared
            double pr = (double) freqSpam/numSpamFiles; //Calculate the probability
            PrWS.put(word, pr); //put the word and probability into the map
        }

        // Pr(S|W) (probability calculation that a file is spam, given it contains a certain word)
        Map<String, Double> PrSW = new TreeMap<>(); //Create a map that will hold the probability and word
        Set<String> PrWSKeys = PrWS.keySet(); //Create a set of keys for the map
        Iterator<String> prWSIterator = PrWSKeys.iterator(); //Create an iterator for the keys
        while(prWSIterator.hasNext())
        { //iterate through the keys
            String word = prWSIterator.next();  //Get the word
            double prWS = PrWS.get(word); //get the probability of the word appearing in a spam file
            double prWH = PrWH.containsKey(word) ? PrWH.get(word) : 0.0; //Get the probability of the word appearing in a ham file
            double pr = prWS / (prWS + prWH); //Calculate the probability that the file is spam given it contains a certain word
            PrSW.put(word, pr); //put the word and probability into the map
        }

        //Testing phase of the program
        File testHamDir = new File(mainDirectory, "/test/ham"); //Create a file with directories heading to the testing ham folder
        File testSpamDir = new File(mainDirectory, "/test/spam"); //Create a file with directories heading to the testing spam folder

        File[] testHam = testHamDir.listFiles(); //Create an array to hold all ham files
        File[] testSpam = testSpamDir.listFiles(); //Create an array to hold all the spam files

        int testHamFiles = testHam.length; //integer variable used to hold the total number of ham files in the array
        int testSpamFiles = testSpam.length; //integer variable used to hold the total number of spam files in the array

        List<TestFile> testFiles = new ArrayList<>(); //Creates an arraylist of testfiles

        for (File file : testHam)
        { //For each file in the ham folder
            double sumHam = 0.0; //Create a double to hold the total sum of ham files

            Map<String, Boolean> wordMap = countWordFile(file); //Create a word map

            Set<String> keys = wordMap.keySet(); //Create a set of keys for the wordmap
            Iterator<String> keyIterator = keys.iterator(); //Create an iterator for the keys

            while (keyIterator.hasNext())
            { //Iterate through the keys
                String word = keyIterator.next(); //String variable used to hold the current for in the file

                if (PrSW.containsKey(word))
                { //if the word is in the probability map
                    double prSW = PrSW.get(word); //Get the word
                    sumHam += Math.log(1 - prSW) - Math.log(prSW); //Add it to the summation calculation
                }
            }

            double PrSF = 1/(1+Math.pow(Math.E, sumHam)); //Calculate the true probability

            String actualClass = "Ham"; //String variable used to show that the message is ham

            TestFile testFileHam = new TestFile(Math.round(PrSF * 100000d) / 100000d, file.getName(), PrSF, actualClass); //Create a testfile object with the rounded probability, filename, probability, and actual class
            testFiles.add(testFileHam); //Add the testfile to the arraylist
        }

        for (File file : testSpam)
        { //for each file in the spam folder
            double sumSpam = 0.0; //Create a double to hold the total sum of spam files

            Map<String, Boolean> wordMap = countWordFile(file); //Create a word map

            Set<String> keys = wordMap.keySet(); //Create a set of keys for the wordmap
            Iterator<String> keyIterator = keys.iterator(); //Create an iterator for the keys

            while (keyIterator.hasNext())
            { //Iterate through the keys
                String word = keyIterator.next(); //String variable used to hold the current for in the file

                if (PrSW.containsKey(word))
                { //if the word is in the probability map
                    double prSW = PrSW.get(word); //Get the word
                    sumSpam += Math.log(1 - prSW) - Math.log(prSW); //Add it to the summation calculation
                }
            }

            double PrSF = 1/(1+Math.pow(Math.E, sumSpam)); //calculate the true probability

            String actualClass = "Spam"; //String variable used to show that the message is spam

            TestFile testFileSpam = new TestFile(Math.round(PrSF * 100000d) / 100000d, file.getName(), PrSF, actualClass); //Create a testfile object with the rounded probability, filename, probability, and actual class
            testFiles.add(testFileSpam); //Add the testfile to the arraylist
        }

        return testFiles; //return the arraylist
    }

    public Map<String, Integer> wordFrequencyDir(File dir) throws IOException
    {
        Map<String, Integer> frequencies = new TreeMap<>();

        File[] filesInDir = dir.listFiles();
        int numFiles = filesInDir.length;

        for (int i = 0; i < numFiles; i++)
        {
            Map<String, Boolean> wordMap = countWordFile(filesInDir[i]);

            // increment the count for each word that appears in the file
            Set<String> keys = wordMap.keySet();
            Iterator<String> keyIterator = keys.iterator();

            while (keyIterator.hasNext())
            {
                String word = keyIterator.next();
                //if (wordMap.get(word)) {
                if (frequencies.containsKey(word))
                {
                    int oldCount = frequencies.get(word);
                    frequencies.put(word, oldCount + 1);
                }
                else
                {
                    frequencies.put(word, 1);
                }

            }
        }
        return frequencies;
    }

    private Map<String, Boolean> countWordFile(File file) throws IOException
    {
        Map<String, Boolean> wordMap = new TreeMap<>();
        if (file.exists())
        {
            // load all the data and process it into words
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext())
            {
                // ignore the casing for words
                String word = (scanner.next()).toLowerCase();
                if (isWord(word))
                {
                    wordMap.put(word, true);
                }
            }
        }
        return wordMap;
    }

    private Boolean isWord(String word)
    {
        //this code defines a private method called isword, it checks if it is null
        if (word == null)
        {
            return false;
        }
        //this method checks whether the word argument matches the pattern using the string class, if it is valid then returns true if not returns false
        String pattern = "^[a-zA-Z]*$";
        if(word.matches(pattern))
        {
            return true;
        }
        return false;
    }
    //This method calculates the accuracy of a spam detection model on a list of test files
    public Map<String, Double> accuracy(List<TestFile> testFiles)
    {
        //Initializes the counters for the number of true positives, true negatives, and total files
        int TruePositives = 0;
        int TrueNegatives = 0;
        int numFiles = 0;
        //int numFiles = testFiles.size();

        //Loops through each test file within the list
        for(TestFile test : testFiles)
        {
            //Checks in the for loop if the actual class of the test file is "spam"
            if (test.getActualClass() == "Spam")
            {
                //If the spam probability of the test file is greater than 0.7, it is a true positive
                if (test.getSpamProbability() > 0.7)
                {
                    TruePositives = TruePositives + 1;

                    //if the spam probability of the test file is less than 0.7 or equal to 0.7, it is a false negative
                } else {
                    TrueNegatives = TrueNegatives + 1;
                }
            }
            //increment the counter for the total number of files.
            numFiles++;
        }
        //calculate the accuracy as the proportion of correctly classified files to total files.
        double acc = (double) (TruePositives + TrueNegatives) / numFiles;
        Map<String, Double> accuracy = new TreeMap<>();
        accuracy.put("accuracy", acc);

        return accuracy;
    }

    public Map<String, Double> precision(List<TestFile> testFiles)
    {
        //This initializes counters for true positives and false positives
        int TruePositives = 0;
        int FalsePositives = 0;
        //This initializes counter for the total number of files
        int numFiles = 0;
        //int numFiles = testFiles.size();

        //Loops over all TestFiles within the list
        for(TestFile test : testFiles)
        {
            //this checks to see if the actual class of the file is "Spam"
            if (test.getActualClass() == "Spam")
            {
                //this checks if the model classifies the file as "Spam"
                if (test.getSpamProbability() > 0.7)
                {
                    //Increments the true positives counter
                    TruePositives = TruePositives + 1;

                } else {
                    //else increment the false positives counter
                    FalsePositives = FalsePositives + 1;
                }
            }
            //increment the total number of files counter
            numFiles++;
        }
        //Calculate the precision as the number of true positives divided by the total sum of true positives and false positives
        double pre = (double) TruePositives / (FalsePositives + TruePositives);
        Map<String, Double> precision = new TreeMap<>();
        precision.put("precision", pre);
        return precision;
    }
}