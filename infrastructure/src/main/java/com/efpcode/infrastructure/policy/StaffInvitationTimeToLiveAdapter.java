package com.efpcode.infrastructure.policy;

import com.efpcode.application.policy.StaffInvitationTimeToLivePolicy;
import com.efpcode.infrastructure.config.properties.StaffInvitationTimeToLiveProperties;
import java.time.Duration;
import org.springframework.stereotype.Component;

@Component
public class StaffInvitationTimeToLiveAdapter implements StaffInvitationTimeToLivePolicy {

  private final StaffInvitationTimeToLiveProperties staffInvitationTimeToLiveProperties;

  public StaffInvitationTimeToLiveAdapter(
      StaffInvitationTimeToLiveProperties staffInvitationTimeToLiveProperties) {
    this.staffInvitationTimeToLiveProperties = staffInvitationTimeToLiveProperties;
  }

  @Override
  public Duration timeToLive() {
    return Duration.ofDays(staffInvitationTimeToLiveProperties.ttlDays());
  }
}
