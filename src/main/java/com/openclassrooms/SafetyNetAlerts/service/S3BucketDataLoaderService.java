/*
package com.openclassrooms.SafetyNetAlerts.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.SafetyNetAlerts.model.DataLoaded;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
public class S3BucketDataLoaderService implements DataLoaderService {
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final String s3BucketURL = "https://s3-eu-west-1.amazonaws.com/course.oc-static.com/projects/DA+Java+EN/P5+/data.json";

    public S3BucketDataLoaderService(ObjectMapper objectMapper, RestTemplate restTemplate) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    } // end constructor

    @Override
    public DataLoaded loadData() {
        try {
            String response = restTemplate.getForObject(s3BucketURL, String.class);
            if (response == null) {
                System.out.println("Error in retrieving data from S3 bucket");
                return null;
            }
            DataLoaded dataLoaded = objectMapper.readValue(response, DataLoaded.class);
            return dataLoaded;
        } catch(IOException e ){
            System.out.println("Error in retrieving data from S3 bucket");
            System.out.println(e.getMessage());
            return null;
        }
        catch(Exception e){
            System.out.println("Error loading data "+ e.getMessage());
            return null;
        }
    }
}
*/
