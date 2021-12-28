package com.griddynamics.cd.service;

import com.griddynamics.cd.entity.ColorEntity;
import com.griddynamics.cd.model.Color;
import com.griddynamics.cd.repository.ColorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@AllArgsConstructor
public class ColorService {

    private final ColorRepository colorRepository;

    public Color getColorById(Long id) {
        return mapColorEntityToColorModel(colorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Color with " + id + " id was not found"))
        );
    }

    public void saveColor(Color color) {
        colorRepository.save(mapColorModelToColorModel(color));
    }

    public void deleteColorById(Long id) {
        colorRepository.deleteById(id);
    }

    public Color mapColorEntityToColorModel(ColorEntity entity) {
        return Color.builder()
                .id(entity.getId())
                .colorName(entity.getColorName())
                .build();
    }

    public ColorEntity mapColorModelToColorModel(Color color) {
        return ColorEntity.builder()
                .id(color.getId())
                .colorName(color.getColorName())
                .build();
    }
}
