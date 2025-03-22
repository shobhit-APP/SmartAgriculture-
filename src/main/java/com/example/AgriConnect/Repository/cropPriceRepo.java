package com.example.AgriConnect.Repository;

import com.example.AgriConnect.Model.Crop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
/*
    Using Jpa Repository is an Interface that Provides a Powerful abstractions Layer For Interacting
    with Database it extends The Crud Repository (Create,Read,Update,Delete)and Paging And Sorting
    Repo And You Should Also Make Your Custom Query Method To Work With Relational Database
 */
/*
    Using @Repository Annotation To Add This Repo Component In Spring Boot Container
    To Make It Object....
 */
@Repository
public interface cropPriceRepo extends JpaRepository<Crop,Long > {


    // In your CropRepository interface
        List<Crop> findByStateAndUserId(String state, Long userId);
    //    Creating A Custom Method To Find The Market Details for Specific Crop
        List<Crop> findByUserDetails1UserId(Long userId);
}
