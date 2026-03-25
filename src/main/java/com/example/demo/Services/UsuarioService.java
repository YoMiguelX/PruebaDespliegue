package com.example.demo.Services;

import com.example.demo.Dto.RegistroUsuarioDto;
import com.example.demo.Dto.UsuarioDto;
import com.example.demo.Dto.Response.ApiResponse;
import com.example.demo.Interface.IUsuarioService;
import com.example.demo.Model.Rol;
import com.example.demo.Model.Usuario;
import com.example.demo.Model.Jugador;
import com.example.demo.Repository.JugadorRepository;
import com.example.demo.Repository.ProgresoJugadorRepository;
import com.example.demo.Repository.ReporteRepository;
import com.example.demo.Repository.ResetTokenRepository;
import com.example.demo.Repository.RespuestasJugadorRepository;
import com.example.demo.Repository.RolRepository;
import com.example.demo.Repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService implements IUsuarioService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private GameTagGeneratorService gametagGenerator;  // ← CORREGIDO: GametagGenerator (no GameTagGeneratorService)

    @Autowired
    private JugadorRepository jugadorRepository;

    private final UsuarioRepository repo;
    private final ModelMapper mapper;
    private final RolRepository rolRepository;

    @Autowired
    private ProgresoJugadorRepository progresoJugadorRepository;

    @Autowired
    private RespuestasJugadorRepository respuestasJugadorRepository;

    @Autowired
    private ResetTokenRepository resetTokenRepository;

    @Autowired
    private ReporteRepository reporteRepository;

    public UsuarioService(
            UsuarioRepository repo,
            ModelMapper mapper,
            RolRepository rolRepository
    ) {
        this.repo = repo;
        this.mapper = mapper;
        this.rolRepository = rolRepository;
    }

    @Override
    public ApiResponse<List<UsuarioDto>> findAll() {
        ApiResponse<List<UsuarioDto>> response = new ApiResponse<>();
        try {
            List<UsuarioDto> usuarios = repo.findAll()
                    .stream()
                    .map(u -> mapper.map(u, UsuarioDto.class))
                    .toList();

            response.setSuccess(true);
            response.setHttpStatusCode(HttpStatus.OK.value());
            response.setMessage(usuarios.isEmpty()
                    ? "No hay usuarios registrados."
                    : "Lista de usuarios obtenida correctamente.");
            response.setData(usuarios);
            response.setTotalRecords(usuarios.size());
        } catch (Exception ex) {
            response.setSuccess(false);
            response.setHttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Error al obtener usuarios: " + ex.getMessage());
            response.setData(Collections.emptyList());
            response.setTotalRecords(0);
        }
        return response;
    }

    @Override
    public List<Usuario> obtenerTodosLosAdmins() {
        return repo.findByRol_IdRol(1);
    }

    @Override
    public ApiResponse<UsuarioDto> findById(Integer id) {
        ApiResponse<UsuarioDto> response = new ApiResponse<>();
        try {
            Usuario usuario = repo.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));
            UsuarioDto dto = mapper.map(usuario, UsuarioDto.class);
            response.setSuccess(true);
            response.setHttpStatusCode(HttpStatus.OK.value());
            response.setMessage("Usuario obtenido correctamente");
            response.setData(dto);
        } catch (EntityNotFoundException ex) {
            response.setSuccess(false);
            response.setHttpStatusCode(HttpStatus.NOT_FOUND.value());
            response.setMessage(ex.getMessage());
        } catch (Exception ex) {
            response.setSuccess(false);
            response.setHttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Error al obtener usuario: " + ex.getMessage());
        }
        return response;
    }

    // ------------------------------
    // REGISTRO
    // ------------------------------
    @Transactional
    @Override
    public ApiResponse<UsuarioDto> registrarUsuario(RegistroUsuarioDto dto) {
        ApiResponse<UsuarioDto> response = new ApiResponse<>();

        try {
            // Validar correo único
            if (repo.findByCorreoUsuario(dto.getCorreo()).isPresent()) {
                throw new RuntimeException("Ya existe un usuario con ese correo");
            }

            // Validar gametag único
            if (jugadorRepository.existsByNombre(dto.getGametag())) {
                throw new RuntimeException("El gametag ya está en uso");
            }

            // Crear usuario
            Usuario usuario = new Usuario();
            usuario.setNombreUsuario(dto.getNombreUsuario());
            usuario.setApellidoUsuario(dto.getApellidoUsuario());
            usuario.setCorreoUsuario(dto.getCorreo());
            usuario.setContrasena(passwordEncoder.encode(dto.getContrasena()));
            usuario.setTelUsuario(dto.getTelefono());
            usuario.setEstadoUsuario("Activo");
            usuario.setFechaCreacion(LocalDateTime.now());

            Rol rol = rolRepository.findById(2)
                    .orElseThrow(() -> new RuntimeException("El rol 2 (Usuario normal) no existe"));
            usuario.setRol(rol);

            Usuario saved = repo.save(usuario);

            // CREAR JUGADOR con gametag
            Jugador jugador = new Jugador();
            jugador.setNombre(dto.getGametag());
            jugador.setUsuario(saved);
            jugador.setFechaRegistro(LocalDate.now());
            jugador.setUltimaConexion(LocalDate.now());
            jugador.setEstado("ACTIVO");
            jugador.setProgreso(null);
            jugador.setProgresoJugador(null);
            jugador.setActive(true);
            jugador.setIsDeleted(false);

            jugadorRepository.save(jugador);

            // Mapear respuesta con gametag
            UsuarioDto dtoMapped = mapearUsuarioAUsuarioDto(saved);
            dtoMapped.setGametag(jugador.getNombre());

            response.setSuccess(true);
            response.setHttpStatusCode(HttpStatus.CREATED.value());
            response.setMessage("Usuario registrado exitosamente");
            response.setData(dtoMapped);

        } catch (Exception ex) {
            response.setSuccess(false);
            response.setHttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Error al registrar usuario: " + ex.getMessage());
        }

        return response;
    }

    // ------------------------------
    // UPDATE GENERICO
    // ------------------------------
    @Override
    public ApiResponse<UsuarioDto> update(Integer id, UsuarioDto dto) {

        Usuario usuario = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setNombreUsuario(dto.getNombre());
        usuario.setApellidoUsuario(dto.getApellido());
        usuario.setCorreoUsuario(dto.getCorreo());
        usuario.setTelUsuario(dto.getTelefono());
        usuario.setEstadoUsuario(dto.getEstado());

        Rol rol = rolRepository.findById(dto.getRolId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        usuario.setRol(rol);

        repo.save(usuario);

        return new ApiResponse<>(
                200,
                "Usuario actualizado correctamente",
                mapper.map(usuario, UsuarioDto.class)
        );
    }

    // ------------------------------
    // UPDATE (ADMIN)
    // ------------------------------
    @Override
    public ApiResponse<UsuarioDto> actualizarUsuario(UsuarioDto dto) {

        Usuario usuario = repo.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setNombreUsuario(dto.getNombre());
        usuario.setApellidoUsuario(dto.getApellido());
        usuario.setCorreoUsuario(dto.getCorreo());
        usuario.setTelUsuario(dto.getTelefono());
        usuario.setEstadoUsuario(dto.getEstado());

        Rol rol = rolRepository.findById(dto.getRolId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        usuario.setRol(rol);
        repo.save(usuario);

        return new ApiResponse<>(200, "Usuario actualizado correctamente", dto);
    }

    // ------------------------------
    // INACTIVAR (soft delete)
    // ------------------------------
    @Transactional
    public void inactivar(Integer id) {
        Usuario usuario = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));
        usuario.setEstadoUsuario("Inactivo");
        repo.save(usuario);
    }

    // ------------------------------
    // DELETE REAL (borra en cascada)
    // ------------------------------
    @Transactional
    @Override
    public void delete(Integer id) {
        repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));

        // 1. Borrar reportes del usuario
        reporteRepository.deleteByUsuario_IdUsuario(id);

        // 2. Borrar reset tokens del usuario
        resetTokenRepository.deleteByUsuario_IdUsuario(id);

        // 3. Obtener jugadores y limpiar FK circular Jugador->ProgresoJugador
        // IMPORTANTE: Usar el método que devuelve List<Jugador>
        List<Jugador> jugadores = jugadorRepository.findAllByUsuario_IdUsuario(id);

        if (jugadores != null && !jugadores.isEmpty()) {
            jugadorRepository.nullifyProgresoByUsuario(id);

            // 4. Borrar respuestas y progreso de cada jugador
            for (Jugador jugador : jugadores) {
                respuestasJugadorRepository.deleteByJugador(jugador);
                progresoJugadorRepository.deleteByJugador(jugador);
            }
        }

        // 5. Borrar jugadores y finalmente el usuario
        jugadorRepository.deleteByUsuario_IdUsuario(id);
        repo.deleteById(id);
    }

    @Override
    public ApiResponse<UsuarioDto> verificarUsuario(String correo, String contrasena) {
        return null;
    }

    // ------------------------------
    // LOGIN
    // ------------------------------
    @Override
    public ApiResponse<UsuarioDto> login(String correo, String contrasena) {
        ApiResponse<UsuarioDto> response = new ApiResponse<>();

        try {
            Usuario usuario = repo.findByCorreoUsuario(correo)
                    .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

            if (!passwordEncoder.matches(contrasena, usuario.getContrasena())) {
                throw new RuntimeException("Credenciales inválidas");
            }

            // BUSCAR JUGADOR
            Optional<Jugador> jugadorOpt = jugadorRepository.findByUsuario_IdUsuario(usuario.getIdUsuario());
            Jugador jugador;

            if (jugadorOpt.isEmpty()) {
                // Crear jugador con gametag automático estilo Xbox
                String gametagAuto = gametagGenerator.generarGametagAleatorio();

                jugador = new Jugador();
                jugador.setNombre(gametagAuto);
                jugador.setUsuario(usuario);
                jugador.setFechaRegistro(LocalDate.now());
                jugador.setUltimaConexion(LocalDate.now());
                jugador.setEstado("ACTIVO");
                jugador.setProgreso(null);
                jugador.setProgresoJugador(null);
                jugador.setActive(true);
                jugador.setIsDeleted(false);

                jugadorRepository.save(jugador);
            } else {
                jugador = jugadorOpt.get();
                // Actualizar última conexión
                jugador.setUltimaConexion(LocalDate.now());
                jugadorRepository.save(jugador);
            }

            UsuarioDto dto = mapearUsuarioAUsuarioDto(usuario);
            dto.setGametag(jugador.getNombre());

            response.setSuccess(true);
            response.setHttpStatusCode(200);
            response.setMessage("Login exitoso");
            response.setData(dto);

        } catch (Exception ex) {
            response.setSuccess(false);
            response.setHttpStatusCode(401);
            response.setMessage(ex.getMessage());
        }

        return response;
    }

    @Override
    public Usuario loginWeb(String correo, String contrasena) {
        Usuario usuario = repo.findByCorreoUsuario(correo)
                .orElseThrow(() -> new EntityNotFoundException("Correo o contraseña incorrectos"));

        if (!passwordEncoder.matches(contrasena, usuario.getContrasena())) {
            throw new EntityNotFoundException("Correo o contraseña incorrectos");
        }

        // Bloquear acceso a cuentas inactivas
        if ("Inactivo".equalsIgnoreCase(usuario.getEstadoUsuario())) {
            throw new EntityNotFoundException("Tu cuenta ha sido desactivada. Contacta al administrador.");
        }

        return usuario;
    }

    // ------------------------------
    // MAPEO DTO
    // ------------------------------
    public UsuarioDto mapearUsuarioAUsuarioDto(Usuario usuario) {
        UsuarioDto dto = new UsuarioDto(
                usuario.getIdUsuario(),
                usuario.getNombreUsuario(),
                usuario.getApellidoUsuario(),
                usuario.getCorreoUsuario(),
                usuario.getTelUsuario(),
                usuario.getEstadoUsuario(),
                usuario.getRol() != null ? usuario.getRol().getIdRol() : null
        );
        return dto;
    }

    @PostConstruct
    public void configurarMapper() {
        mapper.typeMap(Usuario.class, UsuarioDto.class).addMappings(m -> {
            m.map(Usuario::getNombreUsuario, UsuarioDto::setNombre);
            m.map(Usuario::getApellidoUsuario, UsuarioDto::setApellido);
            m.map(Usuario::getCorreoUsuario, UsuarioDto::setCorreo);
            m.map(Usuario::getTelUsuario, UsuarioDto::setTelefono);
            m.map(src -> src.getRol() != null ? src.getRol().getIdRol() : null,
                    UsuarioDto::setRolId);
        });
    }

    // ------------------------------
    // OTROS
    // ------------------------------
    @Override
    public Usuario crearAdministrador(Usuario usuario) {

        Rol rolAdmin = rolRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("No existe el rol Administrador (ID 1)."));

        usuario.setRol(rolAdmin);

        if (!usuario.getContrasena().startsWith("$2a")) {
            usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        }

        usuario.setEstadoUsuario("Activo");
        usuario.setFechaCreacion(LocalDateTime.now());

        return repo.save(usuario);
    }

    @Override
    public List<Usuario> obtenerTodosLosUsuarios() {
        return repo.findAll();
    }

    public Usuario buscarPorCorreo(String correo) {
        return repo.findByCorreoUsuario(correo).orElse(null);
    }

    public String generarToken(Usuario usuario) {
        String token = UUID.randomUUID().toString();
        usuario.setResetToken(token);
        usuario.setResetTokenExpiration(LocalDateTime.now().plusHours(1));
        repo.save(usuario);
        return token;
    }

    public Usuario buscarPorToken(String token) {
        return repo.findByResetToken(token).orElse(null);
    }

    public Usuario guardarUsuario(Usuario usuario) {
        return repo.save(usuario);
    }

    @Override
    public List<Usuario> filtrar(Integer rol, String nombre, String apellido, String correo, String telefono) {
        return repo.filtrar(rol, nombre, apellido, correo, telefono);
    }

    public ApiResponse<UsuarioDto> findByCorreo(String correo) {
        Usuario usuario = repo.findByCorreoUsuario(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        UsuarioDto dto = mapper.map(usuario, UsuarioDto.class);

        return new ApiResponse<>(dto);
    }

    public Usuario buscarPorIdEntity(Integer id) {
        return repo.findById(id).orElse(null);
    }

    public void cambiarPassword(Integer idUsuario, String passwordActual, String passwordNueva) {
        Usuario usuario = repo.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar que la contraseña actual sea correcta
        if (!passwordEncoder.matches(passwordActual, usuario.getContrasena())) {
            throw new RuntimeException("La contraseña actual es incorrecta");
        }

        // Encriptar y guardar la nueva contraseña
        usuario.setContrasena(passwordEncoder.encode(passwordNueva));
        repo.save(usuario);
    }

    @Transactional
    public ApiResponse<UsuarioDto> cambiarGametag(Integer usuarioId, String nuevoGametag) {
        ApiResponse<UsuarioDto> response = new ApiResponse<>();

        try {
            // Buscar jugador por usuario
            Jugador jugador = jugadorRepository.findByUsuario_IdUsuario(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Jugador no encontrado"));

            // Validar que el gametag no esté en uso por otro jugador
            if (!jugador.getNombre().equals(nuevoGametag) &&
                    jugadorRepository.existsByNombre(nuevoGametag)) {
                throw new RuntimeException("El gametag ya está en uso");
            }

            // Validar formato
            if (!nuevoGametag.matches("^[A-Za-z0-9_]{3,20}$")) {
                throw new RuntimeException("El gametag debe tener entre 3 y 20 caracteres, solo letras, números y guión bajo");
            }

            // Actualizar gametag
            jugador.setNombre(nuevoGametag);
            jugadorRepository.save(jugador);

            // Obtener usuario para respuesta
            Usuario usuario = repo.findById(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Construir respuesta con gametag
            UsuarioDto dto = mapearUsuarioAUsuarioDto(usuario);
            dto.setGametag(nuevoGametag);

            response.setSuccess(true);
            response.setHttpStatusCode(200);
            response.setMessage("Gametag actualizado correctamente");
            response.setData(dto);

        } catch (Exception ex) {
            response.setSuccess(false);
            response.setHttpStatusCode(400);
            response.setMessage(ex.getMessage());
        }

        return response;
    }

    public static UsuarioDto toDto(Usuario usuario) {
        return new UsuarioDto(
                usuario.getIdUsuario(),
                usuario.getNombreUsuario(),
                usuario.getApellidoUsuario(),
                usuario.getCorreoUsuario(),
                usuario.getTelUsuario(),
                usuario.getEstadoUsuario(),
                usuario.getRol() != null ? usuario.getRol().getIdRol() : null
        );
    }
}