package com.example.demo.controllers;

import com.example.demo.domain.InhousePart;
import com.example.demo.domain.OutsourcedPart;
import com.example.demo.domain.Part;
import com.example.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class AddPartController {
    @Autowired
    private ApplicationContext context;
    @Autowired
    private PartService partService;

    @Autowired
    private ReportService reportService;

    @GetMapping("/showPartFormForUpdate")
    public String showPartFormForUpdate(@RequestParam("partID") int theId, Model theModel) {
        PartService repo = context.getBean(PartServiceImpl.class);
        OutsourcedPartService outsourcedrepo = context.getBean(OutsourcedPartServiceImpl.class);
        InhousePartService inhouserepo = context.getBean(InhousePartServiceImpl.class);

        boolean inhouse = true;
        List<OutsourcedPart> outsourcedParts = outsourcedrepo.findAll();
        for (OutsourcedPart outsourcedPart : outsourcedParts) {
            if (outsourcedPart.getId() == theId) inhouse = false;
        }
        String formtype;
        if (inhouse) {
            InhousePart inhousePart = inhouserepo.findById(theId);
            theModel.addAttribute("inhousepart", inhousePart);
            formtype = "InhousePartForm";
        } else {
            OutsourcedPart outsourcedPart = outsourcedrepo.findById(theId);
            theModel.addAttribute("outsourcedpart", outsourcedPart);
            formtype = "OutsourcedPartForm";
        }
        return formtype;
    }

    @PostMapping("/savePart")
    public String savePart(@Valid @ModelAttribute("part") Part part, BindingResult bindingResult, Model theModel) {
        if (bindingResult.hasErrors()) {
            // Handle validation errors
            return part instanceof InhousePart ? "InhousePartForm" : "OutsourcedPartForm";
        }

        PartService repo = context.getBean(PartServiceImpl.class);

        // Automatically set the dateAdded if it's a new part
        if (part.getId() == 0) {
            part.setDateAdded(LocalDateTime.now());  // Set the dateAdded to the current date
        }

        repo.save(part); //functionality to save the part to the database
        return "confirmationaddpart";
    }


    @GetMapping("/deletepart") //functionality to delete a part
    public String deletePart(@Valid @RequestParam("partID") int theId, Model theModel) {
        PartService repo = context.getBean(PartServiceImpl.class);
        Part part = repo.findById(theId);
        if (part.getProducts().isEmpty()) {
            repo.deleteById(theId);
            return "confirmationdeletepart";
        } else {
            return "negativeerror";
        }
    }

    @GetMapping("/generatePartReport")
    public void generatePartReport(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"parts_report.csv\"");

        List<Part> parts = partService.findAll();
        PrintWriter writer = response.getWriter();

        reportService.generatePartReport(parts, writer);
    }
}
