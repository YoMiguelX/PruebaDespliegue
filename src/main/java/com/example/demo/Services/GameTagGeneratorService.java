package com.example.demo.Services;

import com.example.demo.Repository.JugadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class GameTagGeneratorService {

    private static final String[] ADJETIVOS = {
            "Dancing", "Fierce", "Silent", "Brave", "Swift", "Mystic", "Frost", "Storm",
            "Shadow", "Golden", "Silver", "Crimson", "Emerald", "Iron", "Steel", "Wild",
            "Ancient", "Dark", "Light", "Thunder", "Phoenix", "Dragon", "Wolf", "Hawk"
    };

    private static final String[] SUSTANTIVOS = {
            "Maple", "Warrior", "Hunter", "Mage", "Knight", "Rogue", "Druid", "Shaman",
            "Ranger", "Paladin", "Warlock", "Priest", "Berserker", "Guardian", "Sentinel",
            "Blade", "Arrow", "Fang", "Claw", "Wing", "Heart", "Soul", "Spirit"
    };

    private static final Random random = new Random();

    @Autowired
    private JugadorRepository jugadorRepository;

    public String generarGametagAleatorio() {
        String adjetivo = ADJETIVOS[random.nextInt(ADJETIVOS.length)];
        String sustantivo = SUSTANTIVOS[random.nextInt(SUSTANTIVOS.length)];
        String base = adjetivo + sustantivo;

        // Verificar si ya existe
        if (!jugadorRepository.existsByNombre(base)) {
            return base;
        }

        // Si existe, agregar número aleatorio
        int numero = random.nextInt(1000) + 1;
        String conNumero = base + numero;

        // Si aún existe, seguir intentando (máximo 10 intentos)
        int intentos = 0;
        while (jugadorRepository.existsByNombre(conNumero) && intentos < 10) {
            numero = random.nextInt(1000) + 1;
            conNumero = base + numero;
            intentos++;
        }

        return conNumero;
    }
}