package com.griddynamics.cd.service;


import com.griddynamics.cd.entity.ColorEntity;
import com.griddynamics.cd.mapper.ColorMapper;
import com.griddynamics.cd.model.Color;
import com.griddynamics.cd.model.create.CreateColorRequest;
import com.griddynamics.cd.model.update.UpdateColorRequest;
import com.griddynamics.cd.repository.ColorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class ColorService {

    private final ColorRepository colorRepository;
    private final ColorMapper colorMapper;

    public List<Color> getAllColors() {
        return StreamSupport.stream(colorRepository.findAll().spliterator(), false)
                .map(colorMapper::toColorModel)
                .collect(Collectors.toList());
    }

    public Color getColorById(Long colorId) {
        return colorMapper.toColorModel(
                colorRepository.findById(colorId)
                        .orElseThrow(() -> new EntityNotFoundException("Color with " + colorId + " id was not found"))
        );
    }

    public Color saveColor(CreateColorRequest createColorRequest) {
        return colorMapper.toColorModel(
                colorRepository.save(
                        colorMapper.toColorEntity(createColorRequest))
        );
    }

    public Color updateColor(UpdateColorRequest updateColorRequest, Long colorId) {
        ColorEntity colorEntity = colorRepository.findById(colorId)
                .orElseThrow(() -> new EntityNotFoundException("Color with " + colorId + " id was not found"));

        return colorMapper.toColorModel(
                colorRepository.save(
                        colorMapper.toColorEntity(updateColorRequest, colorEntity))
        );
    }

    public void deleteColorById(Long colorId) {
        if (!colorRepository.existsById(colorId)) {
            throw new EntityNotFoundException("Color with " + colorId + " id was not found");
        }
        colorRepository.deleteById(colorId);
    }
}
