package com.example.demo.Services;

import com.example.demo.Model.Nivel;
import com.example.demo.Repository.NivelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NivelService {

    @Autowired
    private NivelRepository nivelRepository;

    public List<Nivel> obtenerTodos() {
        return nivelRepository.findAll();
    }

    public Optional<Nivel> obtenerPorId(Integer id) {
        return nivelRepository.findById(id);
    }

    public Nivel guardar(Nivel nivel) {
        return nivelRepository.save(nivel);
    }

    public void eliminar(Integer id) {
        nivelRepository.deleteById(id);
    }
}
