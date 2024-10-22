package com.upao.govench.govench.service.impl;

import com.upao.govench.govench.model.dto.RatingDTO;
import com.upao.govench.govench.model.entity.RatingEvent;
import com.upao.govench.govench.repository.RatingEventRepository;
import com.upao.govench.govench.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    private RatingEventRepository ratingEventRepository;

    @Override
    public List<RatingDTO> getRatingsByEventId(int eventId) {
        // Obtener la lista de RatingEvent para el evento dado
        List<RatingEvent> ratingEvents = ratingEventRepository.findRatingsByEventId(eventId);
        // Convertir cada RatingEvent a RatingDTO
        return ratingEvents.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private RatingDTO convertToDTO(RatingEvent ratingEvent) {
        RatingDTO ratingDTO = new RatingDTO();
        ratingDTO.setIdRating(ratingEvent.getId());
        ratingDTO.setRatingValue(ratingEvent.getValorPuntuacion());
        ratingDTO.setComment("No hay comentarios en RatingEvent");

        // Asignar el nombre y apellido según si es un participante o un organizador
        if (ratingEvent.getUserId() != null && ratingEvent.getUserId().getParticipant() != null) {
            ratingDTO.setName(ratingEvent.getUserId().getParticipant().getName());
            ratingDTO.setLastname(ratingEvent.getUserId().getParticipant().getLastname());
            ratingDTO.setIdUser(ratingEvent.getUserId().getParticipant().getId());
        } else if (ratingEvent.getUserId() != null && ratingEvent.getUserId().getOrganizer() != null) {
            ratingDTO.setName(ratingEvent.getUserId().getOrganizer().getName());
            ratingDTO.setLastname(ratingEvent.getUserId().getOrganizer().getLastname());
            ratingDTO.setIdUser(ratingEvent.getUserId().getOrganizer().getId());
        }

        return ratingDTO;
    }
}
