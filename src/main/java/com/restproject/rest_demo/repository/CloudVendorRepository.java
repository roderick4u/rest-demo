package com.restproject.rest_demo.repository;

import com.restproject.rest_demo.model.CloudVendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CloudVendorRepository extends JpaRepository<CloudVendor, String> {
    List<CloudVendor> findByVendorName(String vendorName);
}
