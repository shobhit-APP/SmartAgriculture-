package com.example.AgriConnect.Model;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="CropRecommendation")
public class CropRecommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "N")
    @JsonProperty("N")
    private float N;

    @Column(name = "P")
    @JsonProperty("P")
    private float P;

    @Column(name = "K")
    @JsonProperty("K")
    private float K;

    @Column(name = "temperature")
    @JsonProperty("temperature")
    private float temperature;

    @Column(name = "humidity")
    @JsonProperty("humidity")
    private float humidity;

    @Column(name = "ph")
    @JsonProperty("ph")
    private float ph;

    @Column(name = "rainfall")
    @JsonProperty("rainfall")
    private float rainfall;

    @Column(name="predicted_crop")
    @JsonProperty("predicted_crop")
    private String PredictedCrop;

    @Column(name = "predicted_crophindi")
    @JsonProperty("predicted_crophindi")
    private String predictedCropHindi;

    // New field: Hindi description
    @Column(name = "hindi_description", length = 1000) // You can adjust length
    @JsonProperty("hindi_description")
    private String hindiDescription;

    // New field: English description
    @Column(name = "english_description", length = 1000) // You can adjust length
    @JsonProperty("english_description")
    private String englishDescription;
    @ManyToOne
    @JoinColumn(name = "UserId" ,referencedColumnName = "UserId")
    @ToString.Exclude
    private UserDetails1 userDetails1;
}
