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
import com.sg.superherosightings.entity.Organizations;
import com.sg.superherosightings.entity.Powers;
import com.sg.superherosightings.entity.Superheros;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validation;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.io.IOException;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.file.Files;

import java.nio.file.Paths;
import java.nio.file.Paths;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author SHIVALI
 */
@Controller

public class SuperheroController {

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
    
   public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources/static/images/";
    Set<ConstraintViolation<Superheros>> violations = new HashSet<>();

    @GetMapping("superheros")
    public String displaySuperheros(Model model) {
        List<Superheros> superheros = superheroDao.getAllSuperheros();
        List<Powers> powers = powerDao.getAllPowers();
        List<Organizations> organizations = organizationDao.getAllOrganizations();
        model.addAttribute("errors", violations);
        model.addAttribute("superheros", superheros);
        model.addAttribute("powers", powers);
        model.addAttribute("organizations", organizations);

        return "superheros";
    }

    @PostMapping("addSuperhero")
    public String addSuperhero(Superheros superhero, Model model,
            HttpServletRequest request) {

        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String[] powerIds = request.getParameterValues("powerId");
        String[] organizationIds = request.getParameterValues("organizationId");
        boolean isHero = Boolean.parseBoolean(request.getParameter("isHero"));

        List<Powers> powers = new ArrayList<>();

        if (powerIds != null) {
            for (String powerId : powerIds) {
                powers.add(powerDao.getPowerById(Integer.parseInt(powerId)));
            }
        }

        List<Organizations> organizations = new ArrayList<>();
        if (organizationIds != null) {
            for (String organizationId : organizationIds) {
                organizations.add(organizationDao.getOrganizationById(Integer.parseInt(organizationId)));
            }
        }

        superhero.setName(name);
        superhero.setDescription(description);
        superhero.setPowers(powers);
        superhero.setOrganizations(organizations);
        superhero.setIsHero(isHero);

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(superhero);
        if (violations.isEmpty()) {
            superheroDao.addSuperhero(superhero);
        }

        //  model.addAttribute("errors", violations);
        //superheroDao.addSuperhero(superhero);
        return "redirect:/superheros";
    }

    @GetMapping("detailSuperhero")
    public String detailSuperhero(Integer id, Model model) {
        Superheros superheros = superheroDao.getSuperheroById(id);
        model.addAttribute("superhero", superheros);

        return "detailSuperhero";
    }

    @GetMapping("deleteSuperhero")
    public String deleteSuperhero(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        superheroDao.deleteSuperheroById(id);

        return "redirect:/superheros";
    }

    @GetMapping("editSuperhero")
    public String editSuperhero(HttpServletRequest request, Model model) {
        int id = Integer.parseInt(request.getParameter("id"));
        Superheros superhero = superheroDao.getSuperheroById(id);
        List<Powers> powers = powerDao.getAllPowers();
        List<Organizations> organizations = organizationDao.getAllOrganizations();

        model.addAttribute("superhero", superhero);
        model.addAttribute("powers", powers);
        model.addAttribute("organizations", organizations);
        model.addAttribute("errors", violations);
        return "editSuperhero";
    }

    @PostMapping("editSuperhero")

    public String performEditSuperhero(Superheros superhero,
            HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {

        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String[] powerIds = request.getParameterValues("powerId");
        String[] organizationIds = request.getParameterValues("organizationId");

        superhero.setName(name);
        superhero.setDescription(description);

        List<Powers> powers = new ArrayList<>();
        if (powerIds != null) {
            for (String powerId : powerIds) {
                powers.add(powerDao.getPowerById(Integer.parseInt(powerId)));
            }
        }
        //   else {
        //      FieldError error = new FieldError("superheros", "powers", "Must include one power");
        // result.addError(error);
        //  }
        superhero.setPowers(powers);
        List<Organizations> organizations = new ArrayList<>();
        if (organizationIds != null) {
            for (String organizationId : organizationIds) {
                organizations.add(organizationDao.getOrganizationById(Integer.parseInt(organizationId)));
            }
        }

        //  else {
        //    FieldError error = new FieldError("superheros", "organizations", "Must include one organization");
        //  result.addError(error);
        //  }
        superhero.setOrganizations(organizations);

        //Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        //   violations = validate.validate(superhero);
        //  if(violations.isEmpty()) {superheroDao.updateSuperhero(superhero);
        //  return "redirect:/superheros";
        // }
        //else {
        //  superhero = superheroDao.getSuperheroById(superhero.getId());
        //  model.addAttribute("superhero", superhero);
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(superhero);
        if (violations.isEmpty()) {
            superheroDao.updateSuperhero(superhero);
            redirectAttributes.addAttribute("id", superhero.id);
            return "redirect:detailSuperhero";
        }

        // if(result.hasErrors()) {
        //   model.addAttribute("powers", powerDao.getAllPowers());
        //   model.addAttribute("organizations", organizationDao.getAllOrganizations());
        //  model.addAttribute("superhero", superhero);
        // }
        //  superheroDao.updateSuperhero(superhero);
        //   model.addAttribute("errors", violations);
        redirectAttributes.addAttribute("id", superhero.id);
        // return  "redirect:/superheros";

        return "redirect:editSuperhero";
    }

    @GetMapping("uploadImage")
    public String displayUploadForm(Integer id, Model model) {

        Superheros superhero = superheroDao.getSuperheroById(id);
        //Superheros superheros  = superheroDao.getSuperheroById(id);
        // redirectAttributes.addAttribute("id", superhero.id);
        model.addAttribute("superhero", superhero);
        // redirectAttributes.addAttribute("id", superhero.id);
        return "uploadImage";
    }

    @PostMapping("uploadImage")
    public String uploadImage(Model model, HttpServletRequest request, @RequestParam("image") MultipartFile file)
            throws IOException {  
        java.nio.file.Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
        Files.write(fileNameAndPath, file.getBytes());

          int id = Integer.parseInt(request.getParameter("id"));
        String image = file.getOriginalFilename();
        superheroDao.addImage(id, image);
        model.addAttribute("imagesuccess", "Image uploaded successfully");
        return "redirect:detailSuperhero?id=" + id ;
    }

}
