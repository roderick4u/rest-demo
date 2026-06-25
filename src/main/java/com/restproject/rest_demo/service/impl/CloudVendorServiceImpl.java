package com.restproject.rest_demo.service.impl;

import com.restproject.rest_demo.exception.CloudVendorNotFoundException;
import com.restproject.rest_demo.model.CloudVendor;
import com.restproject.rest_demo.repository.CloudVendorRepository;
import com.restproject.rest_demo.service.CloudVendorService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CloudVendorServiceImpl implements CloudVendorService {

    CloudVendorRepository cloudVendorRepository;

    public CloudVendorServiceImpl(CloudVendorRepository cloudVendorRepository){
        this.cloudVendorRepository= cloudVendorRepository;
    }

    // Create a cloudvendor
    @Override
    public String createCloudVendor(CloudVendor cloudVendor) {
       cloudVendorRepository.save(cloudVendor);
       return "Successfully Created";
    }
    // Update a cloudvendor
    @Override
    public String updateCloudVendor(CloudVendor cloudVendor) {
        cloudVendorRepository.save(cloudVendor);
        return "Successfully Updated";
    }
    // Delete a cloudvendor by id
    @Override
    public String deleteCloudVendor(String cloudVendorId) {
        cloudVendorRepository.deleteById(cloudVendorId);
        return "Succesfully Deleted";
    }
    // Finds the cloudvendor by their id
    @Override
    public CloudVendor getCloudVendor(String cloudVendorId) {
        if(cloudVendorRepository.findById(cloudVendorId).isEmpty())
            throw new CloudVendorNotFoundException("Requested Cloud Vendor does not exist");
        return cloudVendorRepository.findById(cloudVendorId).get();
    }
    // List of all the cloudvendors
    @Override
    public List<CloudVendor> getAllCloudVendors() {
        return cloudVendorRepository.findAll();
    }
}
