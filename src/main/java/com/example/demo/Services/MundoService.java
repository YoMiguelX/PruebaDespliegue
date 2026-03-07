package com.example.demo.Services;

import com.example.demo.Model.Mundo;
import com.example.demo.Repository.MundoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MundoService {

    private final MundoRepository mundoRepository;

    public MundoService(MundoRepository mundoRepository) {
        this.mundoRepository = mundoRepository;
    }

    public List<Mundo> findAll() {
        return mundoRepository.findAll();
    }
}
