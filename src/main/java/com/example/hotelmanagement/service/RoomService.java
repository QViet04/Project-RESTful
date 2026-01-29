package com.example.hotelmanagement.service;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.hotelmanagement.dto.RoomCreateDTO;
import com.example.hotelmanagement.dto.RoomDTO;
import com.example.hotelmanagement.entity.Room;
import com.example.hotelmanagement.entity.RoomStatus;
import com.example.hotelmanagement.entity.RoomType;
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
        room.setFloor(request.getFloor());
        room.setStatus(request.getStatus());
        Room saved = roomRepository.save(room);
        return toDTO(saved);
    }

    public Page<RoomDTO> findAll(RoomType type, RoomStatus status, Integer floor,
            BigDecimal minPrice, BigDecimal maxPrice, int page, int size) {

        Specification<Room> spec = Specification.where(null);

        if (type != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("type"), type));
        }
        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }
        if (floor != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("floor"), floor));
        }
        if (minPrice != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("price"), minPrice));
        }
        if (maxPrice != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), maxPrice));
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        return roomRepository.findAll(spec, pageable)
                .map(this::toDTO);
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
        room.setFloor(request.getFloor());
        room.setStatus(request.getStatus());
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
        dto.setFloor(room.getFloor());
        dto.setStatus(room.getStatus());
        return dto;
    }
}
