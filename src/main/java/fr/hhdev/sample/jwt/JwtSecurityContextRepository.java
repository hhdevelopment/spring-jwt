package fr.hhdev.sample.jwt;

import java.security.PublicKey;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import reactor.core.publisher.Mono;

/**
 * From ServerWebExchange extract jwtToken to Claims and then to SecurityContextImpl
 */
@Component
public class JwtSecurityContextRepository implements ServerSecurityContextRepository {

  @Autowired
  private PublicKey publicKey;

  @Override
  public Mono<SecurityContext> load(ServerWebExchange swe) {
    return Mono.justOrEmpty(swe.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
        .filter((String authHeader) -> authHeader.startsWith("Bearer "))
        .map((String authHeader) -> authHeader.substring(7))
        .map((String authToken) -> decodeJwt(authToken))
        .map((Claims claims) -> getAuthentication(claims))
        .map(SecurityContextImpl::new);
  }

  private Authentication getAuthentication(Claims claims) {
    Collection<SimpleGrantedAuthority> autorities = getAuthorities(claims);
    Collection<String> groups = getGroups(claims);
    return new JwtAuthenticationToken(claims.getSubject(), autorities, groups);
  }

  private Claims decodeJwt(String jwt) {
    return Jwts.parser().verifyWith(publicKey).build().parseSignedClaims(jwt).getPayload();
  }

  @SuppressWarnings("unchecked")
  private Collection<SimpleGrantedAuthority> getAuthorities(Claims claims) {
    Collection<String> roles = claims.get("roles", List.class);
    return roles.stream().map((String r) -> "ROLE_" + r).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
  }

  @SuppressWarnings("unchecked")
  private Collection<String> getGroups(Claims claims) {
    return claims.get("groups", List.class);
  }

  /**
   * Not used, microservices are sessionless
   */
  @Override
  public Mono<Void> save(ServerWebExchange swe, SecurityContext sc) {
    return Mono.empty();
  }
}
