package com.example.demo.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 *
 *
 *
 */
@Entity
@DiscriminatorValue("2")
public class OutsourcedPart extends Part{ //usage of extends--> inheritance

    @NotNull(message = "Company name must not be null")
    @Size(min = 1, message = "Name must have at least 1 character")
    private String companyName;

    private Integer partId;

    public OutsourcedPart() {
        super();
        this.partId = 0;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public OutsourcedPart(String name, double price, int inv, int min, int max, LocalDateTime dateAdded, int storeNumber, int partId) {
        super(name, price, inv, min, max, dateAdded, storeNumber);
        this.partId = partId;
    }

    public int getPartId() {
        return partId;
    }

    public void setPartId(int partId) {
        this.partId = partId;
    }
}
