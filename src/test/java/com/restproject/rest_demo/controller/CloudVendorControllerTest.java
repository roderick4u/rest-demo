package com.restproject.rest_demo.controller;

import com.restproject.rest_demo.model.CloudVendor;
import com.restproject.rest_demo.service.CloudVendorService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class CloudVendorControllerTest {

    @Mock
    private CloudVendorService cloudVendorService;

    private CloudVendorController cloudVendorController;
    private AutoCloseable autoCloseable;
    private CloudVendor cloudVendorOne;
    private CloudVendor cloudVendorTwo;
    private List<CloudVendor> cloudVendorList;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        // Instantiating manually just like we did with the service!
        cloudVendorController = new CloudVendorController(cloudVendorService);

        cloudVendorOne = new CloudVendor("Amazon", "1", "USA", 1234);
        cloudVendorTwo = new CloudVendor("GCP", "2", "UK", 5678);

        cloudVendorList = new ArrayList<>();
        cloudVendorList.add(cloudVendorOne);
        cloudVendorList.add(cloudVendorTwo);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testGetCloudVendorDetails() {
        // Arrange
        when(cloudVendorService.getCloudVendor("1")).thenReturn(cloudVendorOne);

        // Act
        ResponseEntity<Object> response = cloudVendorController.getCloudVendorDetails("1");

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Extracting map values from your custom ResponseHandler
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertThat(responseBody.get("message")).isEqualTo("Requested Vendor Details are given here");
        assertThat(responseBody.get("data")).isEqualTo(cloudVendorOne);
    }

    @Test
    void testGetAllCloudVendorDetails() {
        // Arrange
        when(cloudVendorService.getAllCloudVendors()).thenReturn(cloudVendorList);

        // Act
        List<CloudVendor> result = cloudVendorController.getAllCloudVendorDetails();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getVendorName()).isEqualTo("Amazon");
    }

    @Test
    void testCreateCloudVendorDetails() {
        // Arrange
        when(cloudVendorService.createCloudVendor(cloudVendorOne)).thenReturn("Successfully Created");

        // Act
        String result = cloudVendorController.createCloudVendorDetails(cloudVendorOne);

        // Assert
        assertThat(result).isEqualTo("Cloud Vendor Created Succesfully");
    }

    @Test
    void testUpdateCloudVendorDetails() {
        // Arrange
        when(cloudVendorService.updateCloudVendor(cloudVendorOne)).thenReturn("Successfully Updated");

        // Act
        String result = cloudVendorController.updateCloudVendorDetails(cloudVendorOne);

        // Assert
        assertThat(result).isEqualTo("Cloud Vendor Updated Succesfully");
    }

    @Test
    void testDeleteCloudVendorDetails() {
        // Arrange
        when(cloudVendorService.deleteCloudVendor("1")).thenReturn("Successfully Deleted");

        // Act
        String result = cloudVendorController.deleteCloudVendorDetails("1");

        // Assert
        assertThat(result).isEqualTo("Cloud Vendor Deleted Succesfully");
    }
}