package com.openclassrooms.SafetyNetAlerts.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.SafetyNetAlerts.model.DataLoaded;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocalFileDataLoaderServiceTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private LocalFileDataLoaderService localFileDataLoaderService;

    @Test
    void loadData_success() throws IOException {
        DataLoaded dataLoaded = new DataLoaded();
        dataLoaded.setPersons(Collections.emptyList());
        dataLoaded.setFirestations(Collections.emptyList());
        dataLoaded.setMedicalrecords(Collections.emptyList());
        when(objectMapper.readValue(any(java.io.InputStream.class), eq(DataLoaded.class))).thenReturn(dataLoaded);

        DataLoaded loadedData = localFileDataLoaderService.loadData();
        assertNotNull(loadedData);
        verify(objectMapper, times(1)).readValue(any(java.io.InputStream.class), eq(DataLoaded.class));
    }

    @Test
    void loadData_failure() throws IOException {
        when(objectMapper.readValue(any(java.io.InputStream.class), eq(DataLoaded.class))).thenThrow(new IOException("Test Exception"));
        assertThrows(RuntimeException.class, () -> {
            localFileDataLoaderService.loadData();
        });
    }

    @Test
    void getDataLoaded_success() throws IOException {
        DataLoaded dataLoaded = new DataLoaded();
        dataLoaded.setPersons(Collections.emptyList());
        dataLoaded.setFirestations(Collections.emptyList());
        dataLoaded.setMedicalrecords(Collections.emptyList());
        when(objectMapper.readValue(any(java.io.InputStream.class), eq(DataLoaded.class))).thenReturn(dataLoaded);

        localFileDataLoaderService.loadData();
        DataLoaded loadedData = localFileDataLoaderService.getDataLoaded();
        assertNotNull(loadedData);
    }

    @Test
    void getDataLoaded_notLoaded() {
        LocalFileDataLoaderService service = new LocalFileDataLoaderService(new ObjectMapper());
        assertThrows(IllegalStateException.class, () -> service.getDataLoaded());
    }

    @Test
    void saveData_success() throws IOException {
        DataLoaded dataLoaded = new DataLoaded();
        dataLoaded.setPersons(Collections.emptyList());
        dataLoaded.setFirestations(Collections.emptyList());
        dataLoaded.setMedicalrecords(Collections.emptyList());
        when(objectMapper.readValue(any(java.io.InputStream.class), eq(DataLoaded.class))).thenReturn(dataLoaded);

        localFileDataLoaderService.loadData();
        DataLoaded data = new DataLoaded();
        localFileDataLoaderService.saveData(data);
        assertEquals(data, localFileDataLoaderService.getDataLoaded());
    }
} // end of class