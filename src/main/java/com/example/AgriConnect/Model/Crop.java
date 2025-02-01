package com.example.AgriConnect.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="Crop")
public class Crop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "state")
    @JsonProperty("state")
    private String state;

    @Column(name = "district")
    @JsonProperty("district")
    private String district;

    @Column(name = "market")
    @JsonProperty("market")
    private String market;

    @Column(name = "crop_name")
    @JsonProperty("crop_name")
    private String cropName;

    @Column(name = "arrival_date")
    @JsonProperty("arrival_date")
    private LocalDate arrivalDate;


    @Column(name = "min_price")
    @JsonProperty("min_price")
    private Double minPrice;

    @Column(name = "max_price")
    @JsonProperty("max_price")
    private Double maxPrice;

    @Column(name = "suggested_price")
    @JsonProperty("suggested_price")
    private Double suggestedPrice;

    @ManyToOne
    @JoinColumn(name = "UserId" ,referencedColumnName = "UserId")
    @ToString.Exclude
    private UserDetails1 userDetails1;

}
