package com.upao.govench.govench.repository;

import com.upao.govench.govench.model.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {
    List<Rating> findAllByRatedUser_Id(Integer ratedUserId);//buscar las calificaciones por autor
    List<Rating> findAllByRaterUser_Id(Integer ratedUserId);//buscar las califiaciones por calificado
    int countByRatedUser_IdAndRatingValue(Integer userId, int ratingValue);//cuenta la cantidad de veces que un usuario ha recibido una calificación
}
