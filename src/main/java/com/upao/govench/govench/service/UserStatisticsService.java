package com.upao.govench.govench.service;

import com.upao.govench.govench.model.entity.Community;
import com.upao.govench.govench.model.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserStatisticsService {
    List<Community> getTotalCommunitiesUserBelongsTo(int userId);
    int getTotalEventsAttendedByUser(Integer userId);
    int getTotalEventsCreatedByUser(Integer userId);
    int getTotalRatingsForAttendedEvents(Integer userId);
    int getTotalRatingsForCreatedEvents(Integer userId);
    int getTotalRatingsReceivedByUser(Integer userId);
    int getTotalPostsInCreatedCommunities(Integer userId);
}
