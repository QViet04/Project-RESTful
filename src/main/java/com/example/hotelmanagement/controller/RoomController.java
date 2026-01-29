package com.example.hotelmanagement.controller;

import java.math.BigDecimal;
import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.hotelmanagement.dto.ErrorResponse;
import com.example.hotelmanagement.dto.RoomCreateDTO;
import com.example.hotelmanagement.dto.RoomDTO;
import com.example.hotelmanagement.entity.RoomStatus;
import com.example.hotelmanagement.entity.RoomType;
import com.example.hotelmanagement.service.RoomService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

@RestController
@Validated
@RequestMapping("/api/v1/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @Operation(summary = "Get rooms", description = "Retrieve paginated rooms with optional filters")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rooms retrieved")
    })
    @GetMapping
    public ResponseEntity<Page<RoomDTO>> getRooms(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Positive int size,
            @RequestParam(required = false) RoomType type,
            @RequestParam(required = false) RoomStatus status,
            @RequestParam(required = false) Integer floor,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {

        Page<RoomDTO> result = roomService.findAll(type, status, floor, minPrice, maxPrice, page, size);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get room by id", description = "Retrieve details for a single room")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Room found"),
            @ApiResponse(responseCode = "404", description = "Room not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<RoomDTO> getRoom(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.findById(id));
    }

    @Operation(summary = "Create new room", description = "Add a new room")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Room created", content = @Content(schema = @Schema(implementation = RoomDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<RoomDTO> createRoom(@Valid @RequestBody RoomCreateDTO request) {
        RoomDTO created = roomService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(created.getId())
            .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "Update room", description = "Update an existing room")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Room updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Room not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<RoomDTO> updateRoom(@PathVariable Long id, @Valid @RequestBody RoomCreateDTO request) {
        RoomDTO updated = roomService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete room", description = "Remove a room")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Room deleted"),
            @ApiResponse(responseCode = "404", description = "Room not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
