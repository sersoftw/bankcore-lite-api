package com.sergio.bankcore.service;

import com.sergio.bankcore.dto.ClienteRequest;
import com.sergio.bankcore.dto.ClienteResponse;
import com.sergio.bankcore.exception.BadRequestException;
import com.sergio.bankcore.exception.ResourceNotFoundException;
import com.sergio.bankcore.model.Cliente;
import com.sergio.bankcore.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<ClienteResponse> listar() {
        return clienteRepository.findAll().stream().map(this::toResponse).toList();
    }

    public ClienteResponse buscarPorId(Long id) {
        return toResponse(findEntity(id));
    }

    @Transactional
    public ClienteResponse crear(ClienteRequest request) {
        clienteRepository.findByDni(request.dni()).ifPresent(c -> {
            throw new BadRequestException("Ya existe un cliente con ese DNI");
        });
        clienteRepository.findByEmail(request.email()).ifPresent(c -> {
            throw new BadRequestException("Ya existe un cliente con ese email");
        });

        Cliente cliente = new Cliente(
                request.nombre(),
                request.apellidos(),
                request.dni(),
                request.email(),
                request.telefono()
        );
        return toResponse(clienteRepository.save(cliente));
    }

    @Transactional
    public ClienteResponse actualizar(Long id, ClienteRequest request) {
        Cliente cliente = findEntity(id);
        cliente.setNombre(request.nombre());
        cliente.setApellidos(request.apellidos());
        cliente.setDni(request.dni());
        cliente.setEmail(request.email());
        cliente.setTelefono(request.telefono());
        return toResponse(cliente);
    }

    @Transactional
    public void desactivar(Long id) {
        Cliente cliente = findEntity(id);
        cliente.setActivo(false);
    }

    public Cliente findEntity(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id " + id));
    }

    private ClienteResponse toResponse(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getApellidos(),
                cliente.getDni(),
                cliente.getEmail(),
                cliente.getTelefono(),
                cliente.getFechaAlta(),
                cliente.isActivo()
        );
    }
}
