package com.example.demo.service;

import com.example.demo.domain.InhousePart;
import com.example.demo.domain.Part;
import com.example.demo.repositories.PartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 *
 *
 *
 *
 */

@Service
public class PartServiceImpl implements PartService{ //usage of implements--> polymorphism
        private PartRepository partRepository;

        @Autowired

    public PartServiceImpl(PartRepository partRepository) {
        this.partRepository = partRepository;
    }

    @Override
    public List<Part> findAll() {
        return (List<Part>) partRepository.findAll();
    }
    public List<Part> listAll(String keyword){
        if(keyword !=null){
            return partRepository.search(keyword);
        }
        return (List<Part>) partRepository.findAll();
    }
    @Override
    public Part findById(int theId) {
        Long theIdl=(long)theId;
        Optional<Part> result = partRepository.findById(theIdl);

        Part thePart = null;

        if (result.isPresent()) {
            thePart = result.get();
        }
        else {

            throw new RuntimeException("Did not find part id - " + theId);
        }

        return thePart;
    }

    @Override
    public void save(Part thePart) {
            partRepository.save(thePart);

    }

    @Override
    public void deleteById(int theId) {
        Long theIdl=(long)theId;
        partRepository.deleteById(theIdl);
    }

    @Override
    public boolean isPartListEmpty() {
        return partRepository.count() == 0;
    }

    @Transactional //Example of Spring Framework's Transaction Management
    @Override
    public boolean addSampleParts() {
        // Sample data for parts
        List<Part> sampleParts = List.of(
                new InhousePart("NVIDIA RTX 3080", 699.99, 10, 2, 15, LocalDateTime.now(), 18865),
                new InhousePart("AMD Ryzen 9 5900X", 549.99, 15, 1, 20, LocalDateTime.now(), 18865),
                new InhousePart("Corsair 16GB DDR4 RAM", 89.99, 25, 5, 40, LocalDateTime.now(), 18865),
                new InhousePart("Samsung 970 EVO 1TB SSD", 129.99, 20, 3, 30, LocalDateTime.now(), 18865),
                new InhousePart("NZXT H510 Case", 69.99, 30, 10, 50, LocalDateTime.now(), 18865),
                new InhousePart("Logitech G Pro Keyboard", 129.99, 12, 2, 20, LocalDateTime.now(), 18865),
                new InhousePart("Razer DeathAdder V2 Mouse", 79.99, 18, 3, 25, LocalDateTime.now(), 18865),
                new InhousePart("Corsair RM850x PSU", 139.99, 12, 1, 15, LocalDateTime.now(), 18865),
                new InhousePart("Noctua NH-D15 Cooler", 89.99, 8, 1, 12, LocalDateTime.now(), 18865),
                new InhousePart("BenQ Zowie XL2546 Monitor", 399.99, 7, 1, 10, LocalDateTime.now(), 18865),
                new InhousePart("TP-Link Archer AX50 Router", 119.99, 22, 5, 30, LocalDateTime.now(), 18865),
                new InhousePart("Seagate Barracuda 2TB HDD", 79.99, 25, 4, 35, LocalDateTime.now(), 18865)
        );

        boolean errorOccurred = false;

        for (Part samplePart : sampleParts) {
            List<Part> existingParts = partRepository.search(samplePart.getName());

            if (existingParts.isEmpty()) {
                // No existing part, so save it as a new part
                partRepository.save(samplePart);
            } else {
                // Create a new part for this specific requirement
                String uniqueName = samplePart.getName() + " - Set"; // Or other unique identifier
                Part newPart = new InhousePart(uniqueName, samplePart.getPrice(), samplePart.getInv(), samplePart.getMin(), samplePart.getMax(), LocalDateTime.now(), 18865);
                partRepository.save(newPart);
            }
        }

        return !errorOccurred;
    }

}
