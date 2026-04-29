package com.efpcode.infrastructure.security.filter;

import com.efpcode.application.port.policy.BootstrapPolicy;
import com.efpcode.application.port.security.BootstrapTokenIssuer;
import com.efpcode.infrastructure.security.exceptions.BootstrapNotAllowedException;
import com.efpcode.infrastructure.security.exceptions.InvalidTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Profile("!test")
@Component
public class BootstrapAuthorizationFilter extends OncePerRequestFilter {

  private static final String BOOTSTRAP_PATH = "/api/v1/boostrap";
  private static final String BEARER_PREFIX = "Bearer ";

  private final BootstrapTokenIssuer tokenIssuer;
  private final BootstrapPolicy bootstrapPolicy;
  private static final Logger log = LoggerFactory.getLogger(BootstrapAuthorizationFilter.class);

  public BootstrapAuthorizationFilter(
      BootstrapTokenIssuer tokenIssuer, BootstrapPolicy bootstrapPolicy) {
    this.tokenIssuer = tokenIssuer;
    this.bootstrapPolicy = bootstrapPolicy;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    if (!isBootstrapRequest(request)) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      String token = extractBearerToken(request);
      tokenIssuer.validateToken(token);
      bootstrapPolicy.ensureBootstrapAllowed();

      filterChain.doFilter(request, response);

    } catch (InvalidTokenException | IllegalStateException e) {
      log.warn("Invalid or missing bootstrap token", e);
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    } catch (BootstrapNotAllowedException e) {
      log.warn("Bootstrap not allowed", e);
      response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
    } catch (Exception e) {
      log.error("Unexpected error during bootstrap authorization", e);
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
    }
  }

  private boolean isBootstrapRequest(HttpServletRequest request) {
    return "POST".equalsIgnoreCase(request.getMethod())
        && request.getRequestURI().equals(BOOTSTRAP_PATH);
  }

  private String extractBearerToken(HttpServletRequest request) {
    String header = request.getHeader("Authorization");

    if (header == null || !header.startsWith(BEARER_PREFIX)) {
      throw new IllegalStateException("Missing or in valid Authorization header");
    }

    return header.substring(BEARER_PREFIX.length());
  }
}
