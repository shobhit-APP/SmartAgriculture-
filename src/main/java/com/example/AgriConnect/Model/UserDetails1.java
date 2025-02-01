package com.example.AgriConnect.Model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "UserDetails1")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetails1 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserId")
    private Long userId;

    @Column(name = "username")
    private String username;

    @Column(name = "UserPassword")
    private String userPassword;

    @Column(name = "UserEmail")
    private String userEmail;

    @Column(name = "ContactNumber")
    private Long contactNumber;

    @OneToMany(mappedBy = "userDetails1",cascade = CascadeType.ALL)
    @ToString.Exclude
    List<Crop> cropList;

    @OneToMany(mappedBy = "userDetails1",cascade = CascadeType.ALL)
    @ToString.Exclude
    List<CropRecommendation> CropRecommendation;
}