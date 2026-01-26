package com.example.hotelmanagement.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.hotelmanagement.dto.RoomCreateDTO;
import com.example.hotelmanagement.dto.RoomDTO;
import com.example.hotelmanagement.entity.Room;
import com.example.hotelmanagement.repository.RoomRepository;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public RoomDTO create(RoomCreateDTO request) {
        Room room = new Room();
        room.setNumber(request.getNumber());
        room.setType(request.getType());
        room.setPrice(request.getPrice());
        room.setFloor(1);                
        room.setStatus("AVAILABLE");
        Room saved = roomRepository.save(room);
        return toDTO(saved);
    }

    public List<RoomDTO> findAll() {
        return roomRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    public RoomDTO findById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found"));
        return toDTO(room);
    }

    public RoomDTO update(Long id, RoomCreateDTO request) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found"));
        room.setNumber(request.getNumber());
        room.setType(request.getType());
        room.setPrice(request.getPrice());
        Room updated = roomRepository.save(room);
        return toDTO(updated);
    }

    public void delete(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found");
        }
        roomRepository.deleteById(id);
    }

    private RoomDTO toDTO(Room room) {
        RoomDTO dto = new RoomDTO();
        dto.setId(room.getId());
        dto.setNumber(room.getNumber());
        dto.setType(room.getType());
        dto.setPrice(room.getPrice());
        return dto;
    }
}
