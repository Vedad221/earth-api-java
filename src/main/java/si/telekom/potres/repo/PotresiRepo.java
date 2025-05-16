//package com.example.java_api.repo;
//
//
//import org.springframework.stereotype.Repository;
//import org.springframework.web.client.RestTemplate;
//
//public class PotresiRepo {
//    private static final RestTemplate restTemplate = new RestTemplate();
//    public static String najdiZadnjiMesec() {
//        String url = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_week.geojson";
//        String response = restTemplate.getForObject(url, String.class);
//        return response;
//    }
//}
