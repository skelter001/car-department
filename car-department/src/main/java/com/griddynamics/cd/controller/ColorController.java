package com.griddynamics.cd.controller;

import com.griddynamics.cd.model.Color;
import com.griddynamics.cd.model.ColorRequest;
import com.griddynamics.cd.service.ColorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
public class ColorController {

    private final ColorService colorService;

    @GetMapping("/colors")
    @Operation(
            summary = "Get all colors",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            }
    )
    public List<Color> getAllColors() {
        return colorService.getAllColors();
    }

    @GetMapping("/colors/{colorId}")
    @Operation(
            summary = "Get color model by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            }
    )
    public Color getColorById(@PathVariable Long colorId) {
        return colorService.getColorById(colorId);
    }

    @PostMapping("/colors")
    @Operation(
            summary = "Save color model",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            }
    )
    public Color saveColor(@RequestBody @Valid ColorRequest colorRequest) {
        return colorService.saveColor(colorRequest);
    }

    @DeleteMapping("/colors/{colorId}")
    @Operation(
            summary = "Delete color by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            }
    )
    public void deleteColorById(@PathVariable Long colorId) {
        colorService.deleteColorById(colorId);
    }
}
