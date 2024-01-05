package fr.hhdev.sample.jwt;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

/**
 * Very simple implementation of {@link org.springframework.security.core.Authentication}
 * which stores the username, the authorities and the groups.
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

  private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

  private final String principal;
  private final Collection<String> groups;

  public JwtAuthenticationToken(String username, Collection<? extends GrantedAuthority> authorities, Collection<String> groups) {
    super(authorities);
    this.principal = username;
    this.groups = groups;
    setAuthenticated(true);
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return this.principal;
  }

  public Collection<String> getGroups() {
    return this.groups;
  }

}
