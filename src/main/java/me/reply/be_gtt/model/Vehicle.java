package me.reply.be_gtt.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Vehicle {

    @JsonProperty("id")                     // Veichle ID
    private Long id;

    @JsonProperty("tipo")                   // Vehicle type - it can be a either a bus or a tram
    private String type;

    @JsonProperty("disabili")
    private Boolean wheelchairAccessible;   // Is the vehicle wheelchair accessible?

    @JsonProperty("lat")
    private Double latitude;                // Vehicle latitude

    @JsonProperty("lon")
    private Double longitude;               // Vehicle longitude

    @JsonProperty("direzione")
    private Integer direction;              // Vehicle direction (?)

    @JsonProperty("aggiornamento")
    private String lastUpdate;              // How fresh is the vehicle data

    @JsonProperty("occupazione")
    private Integer occupancy;              // How many people are on the vehicle

    @JsonProperty("descrizione_percorso")
    private String routeDescription;        // Vehicle route

    @JsonProperty("url_percorso")
    private String routeUrl;                // Vehicle route URL

    @JsonProperty("colore_percorso")
    private String routeColor;              // Vehicle route color

    @JsonProperty("fermata_vicina")
    private Stop stop;                      // Closest stop

}
