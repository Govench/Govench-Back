package com.upao.govench.govench.service.impl;

import com.upao.govench.govench.mapper.ReportMapper;
import com.upao.govench.govench.model.dto.ReportResponseDTO;
import com.upao.govench.govench.model.entity.UserCommunity;
import com.upao.govench.govench.model.entity.UserEvent;
import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.repository.UserCommunityRepository;
import com.upao.govench.govench.repository.UserEventRepository;
import com.upao.govench.govench.repository.UserRepository;
import com.upao.govench.govench.service.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserEventRepository userEventRepository;

    @Autowired
    private UserCommunityRepository userCommunityRepository;

    @Autowired
    private ReportMapper reportMapper;


    @Override
    public ReportResponseDTO generateReport(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        List<UserEvent> userEvents = userEventRepository.findByUser(user);
        List<UserCommunity> userCommunities = userCommunityRepository.findByUser(user);

        int totalEvents = userEvents.size();
        int newFollowers = user.getOrganizer().getFollowers().size();
        int connectionsMade = user.getOrganizer().getFollowings().size();
        int eventsAttended = userEvents.size();
        int totalCommunities = countDistinctCommunities(userCommunities);
        int totalUsersInCommunities = countDistinctUsersInCommunities(userCommunities);
        int totalPostsInCommunities = countDistinctUsersInCommunities(userCommunities);

        return reportMapper.toReportResponseDTO(user, totalEvents, newFollowers, connectionsMade,
                eventsAttended, totalCommunities, totalUsersInCommunities, totalPostsInCommunities);
    }

    private int countDistinctCommunities(List<UserCommunity> userCommunities) {
        return userCommunities.size();
    }

    private int countDistinctUsersInCommunities(List<UserCommunity> userCommunities) {
        return (int) userCommunities.stream()
                .map(UserCommunity::getUser)
                .distinct()
                .count();
    }
}
