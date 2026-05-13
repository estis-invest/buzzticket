package com.efpcode.application.policy;

import java.time.Duration;

public interface StaffInvitationTimeToLivePolicy {
  Duration timeToLive();
}
