package gov.noaa.ncei.mgg.geosamples.ingest.service;

import gov.noaa.ncei.mgg.errorhandler.ApiError;
import gov.noaa.ncei.mgg.errorhandler.ApiException;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.JwtView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.security.Authorities;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesAuthorityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesRoleAuthorityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesTokenEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesUserEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.GeosamplesUserRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.TokenRepository;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TokenService {

  private final PasswordEncoder passwordEncoder;
  private final TokenRepository tokenRepository;
  private final GeosamplesUserRepository userRepository;
  private final TokenGenerator tokenGenerator;

  @Autowired
  public TokenService(@Qualifier("searchableHashEncoder") PasswordEncoder passwordEncoder, TokenRepository tokenRepository,
      GeosamplesUserRepository userRepository, TokenGenerator tokenGenerator) {
    this.passwordEncoder = passwordEncoder;
    this.tokenRepository = tokenRepository;
    this.userRepository = userRepository;
    this.tokenGenerator = tokenGenerator;
  }

  private static Set<String> getAuthorities(GeosamplesUserEntity user) {
    Set<String> authorities = user.getUserRole().getRoleAuthorities().stream()
        .map(GeosamplesRoleAuthorityEntity::getAuthority)
        .map(GeosamplesAuthorityEntity::getAuthorityName)
        .collect(Collectors.toSet());
    authorities.add(Authorities.ROLE_AUTHENTICATED_USER.toString());
    return authorities;
  }

  private static Optional<TokenAuthorization> fromTokenEntity(GeosamplesTokenEntity entity) {
    GeosamplesUserEntity user = entity.getUser();
    return Optional.of(new TokenAuthorization(user.getUserName(), getAuthorities(user)));
  }

  public Optional<TokenAuthorization> authenticate(String token) {
    return tokenRepository.findByTokenHash(passwordEncoder.encode(token)).flatMap(TokenService::fromTokenEntity);
  }

  public String createToken(String userName, String alias) {
    Optional<GeosamplesUserEntity> maybeUser = userRepository.findById(userName);
    if(!maybeUser.isPresent()) {
      throw new ApiException(HttpStatus.NOT_FOUND, ApiError.builder().error("Unable to find user").build());
    }
    String trimmedAlias = alias.trim();
    GeosamplesUserEntity user = maybeUser.get();
    if(user.getTokens().stream().map(GeosamplesTokenEntity::getAlias).collect(Collectors.toSet()).contains(trimmedAlias)) {
      throw new ApiException(HttpStatus.NOT_FOUND, ApiError.builder().error("Token alias " + trimmedAlias + " already exists").build());
    }
    String token = tokenGenerator.generateToken();
    GeosamplesTokenEntity tokenEntity = new GeosamplesTokenEntity();
    tokenEntity.setTokenHash(passwordEncoder.encode(token));
    tokenEntity.setAlias(trimmedAlias);
    user.addToken(tokenEntity);
    userRepository.save(user);
    return token;
  }

  public void deleteToken(String userName, String alias) {
    Optional<GeosamplesUserEntity> maybeUser = userRepository.findById(userName);
    if(!maybeUser.isPresent()) {
      throw new ApiException(HttpStatus.NOT_FOUND, ApiError.builder().error("Unable to find user").build());
    }
    Optional<GeosamplesTokenEntity> maybeToken = tokenRepository.findByUserAndAlias(maybeUser.get(), alias);
    if(maybeToken.isPresent()) {
      tokenRepository.delete(maybeToken.get());
    } else {
      throw new ApiException(HttpStatus.NOT_FOUND, ApiError.builder().error("Unable to find token").build());
    }
  }

  public JwtView createJwt(String username) {
    JwtView jwtView = new JwtView();
    jwtView.setToken(tokenGenerator.generateJwt(username));
    return jwtView;
  }

}
