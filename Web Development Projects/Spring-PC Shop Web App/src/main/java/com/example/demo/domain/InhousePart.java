package com.example.demo.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("1")
public class InhousePart extends Part { //usage of extends--> inhertiance

    private int partId;

    public InhousePart() {
        super();
    }

    // Updated constructor to match the Part class constructor
    public InhousePart(String name, double price, int inv, int min, int max, LocalDateTime dateAdded, int storeNumber) {
        super(name, price, inv, min, max, dateAdded, storeNumber); // Pass the correct arguments to the Part class
        this.inv = inv;
    }

    public int getInv() {
        return inv;
    }

    public void setInv(int inv) {
        this.inv = inv;
    }

    public int getPartId() {
        return partId;
    }

    public void setPartId(int partId) {
        this.partId = partId;
    }

    @Override
    public String toString() {
        return name;
    }
}
