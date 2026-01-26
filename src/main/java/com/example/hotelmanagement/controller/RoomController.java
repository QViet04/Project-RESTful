package com.example.hotelmanagement.controller;

import com.example.hotelmanagement.dto.RoomCreateDTO;
import com.example.hotelmanagement.dto.RoomDTO;
import com.example.hotelmanagement.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @Operation(summary = "Get all rooms", description = "Retrieve list of rooms")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rooms retrieved")
    })
    @GetMapping
    public ResponseEntity<List<RoomDTO>> getRooms() {
        return ResponseEntity.ok(roomService.findAll());
    }

    @Operation(summary = "Get room by id", description = "Retrieve details for a single room")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Room found"),
            @ApiResponse(responseCode = "404", description = "Room not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<RoomDTO> getRoom(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.findById(id));
    }

    @Operation(summary = "Create new room", description = "Add a new room")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Room created", content = @Content(schema = @Schema(implementation = RoomDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping
    public ResponseEntity<RoomDTO> createRoom(@Valid @RequestBody RoomCreateDTO request) {
        RoomDTO created = roomService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Update room", description = "Update an existing room")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Room updated"),
            @ApiResponse(responseCode = "404", description = "Room not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<RoomDTO> updateRoom(@PathVariable Long id, @Valid @RequestBody RoomCreateDTO request) {
        RoomDTO updated = roomService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete room", description = "Remove a room")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Room deleted"),
            @ApiResponse(responseCode = "404", description = "Room not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
