package com.efpcode.domain.staffinvitation;

import com.efpcode.domain.staffinvitation.exceptions.InvalidStaffInvitationTokenException;

public record StaffInvitationTokenHash(String value) {

     public StaffInvitationTokenHash{
        if(value == null || value.isBlank()){
            throw new InvalidStaffInvitationTokenException("Invitation token hash is required");
        }

    }

    public static StaffInvitationTokenHash ofHashed(String hash){
         return new StaffInvitationTokenHash(hash);
    }

}
