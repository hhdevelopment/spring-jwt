package fr.hhdev.sample;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("test")
public class TestController {
  @GetMapping("isAuthenticated")
  @PreAuthorize("isAuthenticated()")
  Mono<Authentication> principal(Authentication authentication) {
    return Mono.just(authentication);
  }

  @GetMapping("hasRole(ADMIN)")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  Mono<Authentication> principal2(Authentication authentication) {
    return Mono.just(authentication);
  }

  @GetMapping("denied")
  @PreAuthorize("hasRole('ROLE_UNKNOWN')")
  Mono<Authentication> principalNot(Authentication authentication) {
    return Mono.just(authentication);
  }}
