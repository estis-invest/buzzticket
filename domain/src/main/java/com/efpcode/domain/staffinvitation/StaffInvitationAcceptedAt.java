package com.efpcode.domain.staffinvitation;

import com.efpcode.domain.staffinvitation.exceptions.InvalidStaffInvitationDateException;

import java.time.Instant;

public record StaffInvitationAcceptedAt(Instant time) {
    private static final int GRACE_PERIOD = 60;

    public StaffInvitationAcceptedAt{
        if(time == null || time.equals(Instant.ofEpochMilli(0))){
            throw new InvalidStaffInvitationDateException("Staff invitation requires date");
        }

        if(time.isAfter(Instant.now().plusSeconds(GRACE_PERIOD))){
            throw new InvalidStaffInvitationDateException("Staff invitation cannot be created in the future");
        }

    }
}
