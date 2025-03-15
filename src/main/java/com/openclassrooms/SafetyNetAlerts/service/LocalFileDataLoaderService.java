package com.openclassrooms.SafetyNetAlerts.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.SafetyNetAlerts.model.DataLoaded;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Service
public class LocalFileDataLoaderService implements DataLoaderService {

    public static final Logger logger = LoggerFactory.getLogger(LocalFileDataLoaderService.class);
    public static final String DATA_FILE_NAME = "testdata.json";

    public final ObjectMapper objectMapper;
    private DataLoaded dataLoaded; // store the loaded data in the memory

    public LocalFileDataLoaderService(ObjectMapper objectMapper) {
        this.objectMapper = new ObjectMapper();
       this.dataLoaded = loadData();
    } // end of constructor
    @Override
    public DataLoaded loadData() {
        logger.info("Loading data from local file: {}", DATA_FILE_NAME);

        try {
            InputStream inputStream = new ClassPathResource(DATA_FILE_NAME).getInputStream();
            DataLoaded data = objectMapper.readValue(inputStream, DataLoaded.class);
            logger.info("Successfully loaded data with {} persons, {} firestations and {} medical records, from local file: {}"
                    , data.getPersons().size(), data.getFirestations().size(), data.getMedicalrecords().size(), DATA_FILE_NAME);
            return data;
        } catch (IOException e) {
            logger.error("Failed to load data from local file {}", DATA_FILE_NAME, e);
            throw new RuntimeException("Failed to load data from local file", e);
        }
    } // end of loadData

    public DataLoaded getDataLoaded() {
        if (dataLoaded == null) {
            throw new IllegalStateException("Data is not loaded yet, Ensure loadData() is called first");
        }
        return dataLoaded;
    } // end of getDataLoaded

    public void saveData(DataLoaded data) {
        this.dataLoaded = data;  // here we are just updating in-memory data
/*
// if we want to save into the file

        try{
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValues(new File(DATA_FILE_NAME));
            logger.info("Data saved successfully to file: {}, Data: {}", DATA_FILE_NAME, data);
        } catch (IOException e) {
            logger.error("Failed to save data to file {}", DATA_FILE_NAME, e);
            throw new RuntimeException("Failed to save data to file", e);
        }
        */
    }
} // end of class
