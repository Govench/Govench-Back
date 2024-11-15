package com.upao.govench.govench.model.dto;

import com.upao.govench.govench.model.entity.Event;
import com.upao.govench.govench.model.entity.User;
import lombok.Data;

@Data
public class ReminderRequestDTO {
    private boolean enableNotifications;
}
