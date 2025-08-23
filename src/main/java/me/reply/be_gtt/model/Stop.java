package me.reply.be_gtt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stop {
    
    private String stopId;              // Stop id
    private String stopCode;            // Code of the stop
    private String stopName;            // Name of the stop
    private String stopDesc;            // Stop description
    private Double stopLat;             // Latitude
    private Double stopLon;             // Longitude
    private String zoneId;              // Urban zone id
    private String stopUrl;             // Url of the stop
    private String locationType;        // Location stop
    private String parentStation;       // Parent
    private String stopTimezone;        // Timezone
    private Integer wheelchairBoarding; // Is this stop wheelchair accessible?
}
