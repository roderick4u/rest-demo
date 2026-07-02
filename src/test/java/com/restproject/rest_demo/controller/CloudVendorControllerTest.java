package com.restproject.rest_demo.controller;

import com.restproject.rest_demo.model.CloudVendor;
import com.restproject.rest_demo.service.CloudVendorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectWriter;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CloudVendorController.class)
class CloudVendorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CloudVendorService cloudVendorService;

    private CloudVendor cloudVendorOne;
    private CloudVendor cloudVendorTwo;
    private List<CloudVendor> cloudVendorList;
    private String requestJson;

    @BeforeEach
    void setUp() throws Exception {
        cloudVendorOne = new CloudVendor("Amazon", "1", "USA", 1234);
        cloudVendorTwo = new CloudVendor("GCP", "2", "UK", 5678);

        cloudVendorList = new ArrayList<>();
        cloudVendorList.add(cloudVendorOne);
        cloudVendorList.add(cloudVendorTwo);

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        requestJson = ow.writeValueAsString(cloudVendorOne);
    }

    @Test
    void getCloudVendorDetails() throws Exception {
        when(cloudVendorService.getCloudVendor("1")).thenReturn(cloudVendorOne);

        this.mockMvc.perform(get("/cloudvendor/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getAllCloudVendorDetails() throws Exception {
        when(cloudVendorService.getAllCloudVendors()).thenReturn(cloudVendorList);

        this.mockMvc.perform(get("/cloudvendor"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void createCloudVendorDetails() throws Exception {
        when(cloudVendorService.createCloudVendor(any(CloudVendor.class))).thenReturn("Successfully Created");

        this.mockMvc.perform(post("/cloudvendor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Cloud Vendor Created Succesfully"));
    }

    @Test
    void updateCloudVendorDetails() throws Exception {
        when(cloudVendorService.updateCloudVendor(any(CloudVendor.class))).thenReturn("Successfully Updated");

        this.mockMvc.perform(put("/cloudvendor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Cloud Vendor Updated Succesfully"));
    }

    @Test
    void deleteCloudVendorDetails() throws Exception {
        when(cloudVendorService.deleteCloudVendor("1")).thenReturn("Successfully Deleted");

        this.mockMvc.perform(delete("/cloudvendor/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Cloud Vendor Deleted Succesfully"));
    }
}
