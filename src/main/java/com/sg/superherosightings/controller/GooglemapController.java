/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.superherosightings.controller;

import com.sg.superherosightings.dao.LocationDao;
import com.sg.superherosightings.dao.OrganizationDao;
import com.sg.superherosightings.dao.PowerDao;
import com.sg.superherosightings.dao.SightingDao;
import com.sg.superherosightings.dao.SuperheroDao;
import com.sg.superherosightings.entity.Locations;
import com.sg.superherosightings.entity.Sightings;
import com.sg.superherosightings.entity.Superheros;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author SHIVALI
 */
@RestController
public class GooglemapController {

    @Autowired
    SuperheroDao superheroDao;

    @Autowired
    PowerDao powerDao;

    @Autowired
    SightingDao sightingDao;

    @Autowired
    LocationDao locationDao;

    @Autowired
    OrganizationDao organizationDao;

  @GetMapping("sightingsmap")
         public   List<Sightings> displayMap(Model model) {

       List<Sightings> sightings = sightingDao.getAllSightings();
    //  List<Superheros> superheros = superheroDao.getAllSuperheros();
     List<Locations> locations = locationDao.getAllLocations();

       model.addAttribute("sightings", sightings);
      //  model.addAttribute("superheros", superheros);
     model.addAttribute("locations", locations);
       return sightings  ;
    }

 }
