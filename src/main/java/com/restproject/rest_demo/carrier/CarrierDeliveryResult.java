package com.restproject.rest_demo.carrier;

public record CarrierDeliveryResult(boolean success, String carrierRef, String errorMessage) {
}
