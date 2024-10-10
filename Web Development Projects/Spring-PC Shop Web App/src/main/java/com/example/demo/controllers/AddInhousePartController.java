package com.example.demo.controllers;

import com.example.demo.domain.InhousePart;
import com.example.demo.service.InhousePartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class AddInhousePartController {

    @Autowired
    private ApplicationContext context;

    private final int STORE_NUMBER = 18865; //For Inhouse parts, it comes from our store, so we can hardcode it

    @GetMapping("/showFormAddInPart")
    public String showFormAddInhousePart(Model theModel) {
        InhousePart inhousepart = new InhousePart();
        theModel.addAttribute("inhousepart", inhousepart);
        return "InhousePartForm";
    }

    @PostMapping("/showFormAddInPart")
    public String submitForm(
            @Valid @ModelAttribute("inhousepart") InhousePart part,
            BindingResult theBindingResult,
            Model theModel
    ) {
        theModel.addAttribute("inhousepart", part);

        if (theBindingResult.hasErrors()) {
            return "InhousePartForm";
        }

        part.setStoreNumber(STORE_NUMBER);

        InhousePartService repo = context.getBean(InhousePartService.class);
        repo.save(part); //functionality to save the part to the database

        return "confirmationaddpart";
    }
}
