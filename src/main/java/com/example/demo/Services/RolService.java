package com.example.demo.Services;

import com.example.demo.Model.Rol;
import com.example.demo.Repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    public Rol findById(Integer id) {
        return rolRepository.findById(id).orElse(null);
    }

    public List<Rol> findAll() {
        return rolRepository.findAll();
    }
}
