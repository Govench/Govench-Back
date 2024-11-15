package com.upao.govench.govench.service;

import com.upao.govench.govench.model.dto.RatingDTO;

import java.util.List;

// RatingService.java
public interface RatingService {
    List<RatingDTO> getRatingsByEventId(int eventId);
}
