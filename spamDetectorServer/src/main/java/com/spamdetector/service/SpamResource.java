package com.spamdetector.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spamdetector.domain.TestFile;
import com.spamdetector.util.SpamDetector;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import jakarta.ws.rs.core.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/spam")
public class SpamResource {

    //    your SpamDetector Class responsible for all the SpamDetecting logic
    SpamDetector detector = new SpamDetector();
    private List<TestFile> testResults;
    ObjectMapper mapper = new ObjectMapper();


    SpamResource() throws IOException {
//        TODO: load resources, train and test to improve performance on the endpoint calls

        trainAndTest();

        System.out.print("Training and testing the model, please wait");

//      TODO: call  this.trainAndTest();
        this.trainAndTest();

    }
    @GET
    @Produces("application/json")
    public Response getSpamResults() throws IOException {
//       TODO: return the test results list of TestFile, return in a Response object

        URL url = this.getClass().getClassLoader().getResource("/data");

        File mainDirectory = null;
        try {
            mainDirectory = new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        testResults = detector.trainAndTest(mainDirectory);

        Response myResp = Response.status(200).header("Access-Control-Allow-Origin", "*")
                .header("Content-Type", "application/json")
                .entity(mapper.writeValueAsString(testResults))
                .build();

        return myResp;
    }

    @GET
    @Path("/accuracy")
    @Produces("application/json")
    public Response getAccuracy() throws IOException {
//      TODO: return the accuracy of the detector, return in a Response object
        URL url = this.getClass().getClassLoader().getResource("/data");

        File mainDirectory = null;
        try {
            mainDirectory = new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        testResults = detector.trainAndTest(mainDirectory);
        Map<String, Double> accuracy = detector.accuracy(testResults);

        Response myResp = Response.status(200).header("Access-Control-Allow-Origin", "*")
                .header("Content-Type", "application/json")
                .entity(mapper.writeValueAsString(accuracy))
                .build();

        return myResp;
    }

    @GET
    @Path("/precision")
    @Produces("application/json")
    public Response getPrecision() throws IOException {
        //      TODO: return the precision of the detector, return in a Response object
        URL url = this.getClass().getClassLoader().getResource("/data");

        File mainDirectory = null;
        try {
            mainDirectory = new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        testResults = detector.trainAndTest(mainDirectory);
        Map<String, Double> precision = detector.precision(testResults);

        Response myResp = Response.status(200).header("Access-Control-Allow-Origin", "*")
                .header("Content-Type", "application/json")
                .entity(mapper.writeValueAsString(precision))
                .build();

        return myResp;
    }

    public List<TestFile> trainAndTest() throws IOException {
        if (this.detector==null){
            this.detector = new SpamDetector();
        }

//        TODO: load the main directory "data" here from the Resources folder
        URL url = this.getClass().getClassLoader().getResource("/data");

        File mainDirectory = null;
        try {
            mainDirectory = new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return this.detector.trainAndTest(mainDirectory);
    }
}