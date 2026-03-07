package com.example.demo.Repository;

import com.example.demo.Model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Integer> {



    Optional<Rol> findByNombreRol(String nombreRol);

    List<Rol> findAll();
     Optional<Rol> findById(Integer integer);
}
