//package com.openclassrooms.SafetyNetAlerts.service;
//
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.openclassrooms.SafetyNetAlerts.model.*;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.core.io.ClassPathResource;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class LocalFileDataLoaderServiceTest {
//
//    @InjectMocks
//    private LocalFileDataLoaderService dataLoaderService;
//
//    @Mock
//    private ObjectMapper objectMapper;
//
//    @Mock
//    private ClassPathResource resource;
//
//    @Test
//    public void testLoadData_success() throws IOException {
//        // Arrange
//        // Create sample data
//        List<Person> persons = Arrays.asList(new Person("John", "Doe", "123 Main St", "City", "12345", "555-1234", "john.doe@example.com"));
//        List<FireStation> firestations = Arrays.asList(new FireStation("123 Main St", "1"));
//        List<MedicalRecord> medicalrecords = Arrays.asList(new MedicalRecord("John", "Doe", "01/01/1990", Arrays.asList("Med1", "Med2"), Arrays.asList("Allergy1")));
//
//        DataLoaded expectedData = new DataLoaded(persons, firestations, medicalrecords); // Create an expected DataLoaded object
//
//        InputStream inputStream = mock(InputStream.class);
//
//        when(resource.getInputStream()).thenReturn(inputStream); // Mock the getInputStream() method
//        when(objectMapper.readValue(any(InputStream.class), eq(DataLoaded.class))).thenReturn(expectedData);
//
//
//        // Act
//        DataLoaded actualData = dataLoaderService.loadData();
//
//        // Assert
//        assertNotNull(actualData);
//        assertEquals(expectedData.getPersons(), actualData.getPersons());
//        assertEquals(expectedData.getFirestations(), actualData.getFirestations());
//        assertEquals(expectedData.getMedicalrecords(), actualData.getMedicalrecords());
//        verify(resource, times(1)).getInputStream();
//        verify(objectMapper, times(1)).readValue(inputStream, DataLoaded.class);
//    }
//
//    @Test
//    public void testLoadData_fileNotFound() throws IOException {
//
//        when(new ClassPathResource("testData.json").getInputStream()).thenThrow(IOException.class);
//
//        // Act & Assert
//        assertThrows(RuntimeException.class, () -> dataLoaderService.loadData());
//    }
//
//
//    @Test
//    public void testLoadData_jsonParsingError() throws IOException {
//        InputStream inputStream = new ByteArrayInputStream("invalid json".getBytes());
//        when(new ClassPathResource("testData.json").getInputStream()).thenReturn(inputStream);
//        when(objectMapper.readValue(any(InputStream.class), org.mockito.Mockito.eq(DataLoaded.class))).thenThrow(IOException.class);
//
//
//        // Act & Assert
//        assertThrows(RuntimeException.class, () -> dataLoaderService.loadData());
//    }
//}