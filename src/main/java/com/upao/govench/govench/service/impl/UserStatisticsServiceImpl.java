package com.upao.govench.govench.service.impl;

import com.upao.govench.govench.model.entity.Community;
import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.repository.CommunityRepository;
import com.upao.govench.govench.repository.EventRepository;
import com.upao.govench.govench.repository.PostRepository;
import com.upao.govench.govench.repository.RatingRepository;
import com.upao.govench.govench.service.UserStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserStatisticsServiceImpl implements UserStatisticsService {
    @Autowired
    private CommunityRepository communityRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private PostRepository postRepository;

    @Override
    public List<Community> getTotalCommunitiesUserBelongsTo(int userId) {
        return communityRepository.findByOwner_id(userId);
    }

    @Override
    public int getTotalEventsAttendedByUser(Integer userId) {
        return eventRepository.countAttendedByUserId(userId);
    }

    @Override
    public int getTotalEventsCreatedByUser(Integer userId) {
        return eventRepository.countCreatedByUserId(userId);
    }

    @Override
    public int getTotalRatingsForAttendedEvents(Integer userId) {
        return ratingRepository.countRatingsForAttendedEvents(userId);
    }

    @Override
    public int getTotalRatingsForCreatedEvents(Integer userId) {
        return ratingRepository.countRatingsForCreatedEvents(userId);
    }

    @Override
    public int getTotalRatingsReceivedByUser(Integer userId) {
        return ratingRepository.countRatingsReceivedByUserId(userId);
    }

    @Override
    public int getTotalPostsInCreatedCommunities(Integer userId) {
        return postRepository.countPostsInCommunitiesCreatedByUser(userId);
    }
}
