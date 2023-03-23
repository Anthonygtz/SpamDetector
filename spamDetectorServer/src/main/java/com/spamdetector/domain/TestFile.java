package com.spamdetector.domain;

import java.text.DecimalFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class represents a file from the testing data
 * Includes the actual or real class and the predicted class according to the classifier
 * @author CSCI2020U *
 */
public class TestFile {

    @JsonProperty("spamProbRounded")
    private double spamProbRounded;

    @JsonProperty("file")
    private String filename;

    /**
     * the probability of this file belonging to the 'spam' category/class
     */
    @JsonProperty("spamProbability")
    private double spamProbability;
    /**
     * the real class/category of the file; related to the folder it was loaded from 'spam' or 'ham'
     */
    @JsonProperty("actualClass")
    private String actualClass;

    //Testfile class parameterized constructor
    public TestFile(double spamProbRounded, String filename, double spamProbability, String actualClass) {
        this.spamProbRounded = spamProbRounded;
        this.filename = filename;
        this.spamProbability = spamProbability;
        this.actualClass = actualClass;
    }

    //Getter for SpamProbRounded
    public double getSpamProbRounded() { return this.spamProbRounded; }

    //Getter for Filename
    public String getFilename() { return this.filename; }

    //Getter for SpamProbability
    public double getSpamProbability() { return this.spamProbability; }

    //Getter for ActualClass
    public String getActualClass() { return this.actualClass; }

    //Setter for SpamProbRounded
    public void setSpamProbRounded(double value) { this.spamProbRounded = value; }

    //Setter for Filename
    public void setFilename(String value) { this.filename = value; }

    //Setter for SpamProbability
    public void setSpamProbability(double value) { this.spamProbability = value; }

    //Setter for ActualClass
    public void setActualClass(String value) { this.actualClass = value; }
}