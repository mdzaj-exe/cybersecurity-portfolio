package com.example.demo.service;

import com.example.demo.domain.OutsourcedPart;
import com.example.demo.repositories.OutsourcedPartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 *
 *
 *
 */
@Service
public class OutsourcedPartServiceImpl implements OutsourcedPartService{ //usage of implements--> polymorphism
    private OutsourcedPartRepository partRepository;

    @Autowired
    public OutsourcedPartServiceImpl(OutsourcedPartRepository partRepository) {
        this.partRepository = partRepository;
    }

    @Override
    public List<OutsourcedPart> findAll() {
        return (List<OutsourcedPart>) partRepository.findAll();
    }

    @Override
    public OutsourcedPart findById(int theId) {
        Long theIdl=(long)theId;
        Optional<OutsourcedPart> result = partRepository.findById(theIdl);

        OutsourcedPart thePart = null;

        if (result.isPresent()) {
            thePart = result.get();
        }
        else {

            return null;
        }

        return thePart;
    }

    @Override
    public void save(OutsourcedPart thePart) {
        partRepository.save(thePart);

    }

    @Override
    public void deleteById(int theId) { //functionality to delete a part from the database
        Long theIdl=(long)theId;
        partRepository.deleteById(theIdl);
    }

}
