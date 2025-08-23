package me.reply.be_gtt.service;

import me.reply.be_gtt.CsvReader;
import me.reply.be_gtt.model.Stop;
import me.reply.be_gtt.model.Vehicle;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@Service
public class LineService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final CsvReader    csvReader;

    public LineService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
        this.csvReader    = new CsvReader();
    }

    // Compute the distance between two points using the Haversine formula
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;
        return Math.sqrt(dLat * dLat + dLon * dLon);
    }

    private String[] getRoute(String lineId) {
        return csvReader.read("static/routes.csv").stream()
                .filter(record -> record[2].equals(lineId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("line not found"));
    }

    private List<String[]> getStops() {
        return csvReader.read("static/stops.csv").stream()
                .skip(1) // Ignore the header
                .toList();
    }

    private List<Vehicle> fetchVehicles(String lineId) {

        long   amp = System.currentTimeMillis();
        String url = "https://percorsieorari.gtt.to.it/das_ws/das_ws.asmx/GetVeicoliPerLineaWsJson?linea=" + lineId + "&_=" + amp;

        ResponseEntity<String> res = restTemplate.getForEntity(url, String.class);
        String json = res.getBody();

        try {
            return objectMapper.readValue(json, new TypeReference<List<Vehicle>>() {});
        } catch (Exception e) {
            throw new RuntimeException("error parsing vehicles", e);
        }
    }

    private Stop findNearestStop(Vehicle v, List<String[]> stops) {
        
        /**
         * 
         * 
         * Considering the current position of the vehicle, 
         * find the nearest stop. This is done by calculating 
         * the distance between the vehicle and each stop, 
         * and then finding the stop with the smallest 
         * distance.
         * 
         * 
         * 
         * Note: this is not the most accurate way to find 
         * the nearest stop, but it works well enough for 
         * this application.
         * 
         * 
         */

        return stops.stream()
                .min((s1, s2) -> Double.compare(
                        distance(v.getLatitude(), v.getLongitude(),
                                Double.parseDouble(s1[4]), Double.parseDouble(s1[5])),
                        distance(v.getLatitude(), v.getLongitude(),
                                Double.parseDouble(s2[4]), Double.parseDouble(s2[5]))
                ))
                .map(s -> new Stop(
                        s[0], 
                        s[1], 
                        s[2], 
                        s[3],
                        Double.parseDouble(s[4]),
                        Double.parseDouble(s[5]),
                        s[6], 
                        s[7], 
                        s[8], 
                        s[9],
                        s[10], 
                        s[11].isEmpty() ? null : Integer.parseInt(s[11])
                ))
                .orElse(null);
    }

    private Vehicle editVehicle(Vehicle v, String routeDescription, String routeUrl, String routeColor, List<String[]> stops) {
        
        v.setRouteDescription(routeDescription);
        v.setRouteUrl(routeUrl);
        v.setRouteColor(routeColor);

        if (v.getLatitude() != null && v.getLongitude() != null) {
            v.setLatitude(Math.round(v.getLatitude()   * 1e5) / 1e5);
            v.setLongitude(Math.round(v.getLongitude() * 1e5) / 1e5);
        }

        v.setStop(findNearestStop(v, stops));

        if (v.getOccupancy() != null && v.getOccupancy() > 40) {
            v.setRouteDescription(v.getRouteDescription() + " (full)");
        }

        return v;
    }

    public List<Vehicle> getLine(String lineId) {
        String[] route          = getRoute(lineId);
        String routeDescription = route[4];
        String routeUrl         = route[6];
        String routeColor       = route[7];

        List<String[]> stops = getStops();
        List<Vehicle> vehicles = fetchVehicles(lineId);

        return vehicles.stream()
                .map(v -> editVehicle(v, routeDescription, routeUrl, routeColor, stops))
                .toList();
    }
}
