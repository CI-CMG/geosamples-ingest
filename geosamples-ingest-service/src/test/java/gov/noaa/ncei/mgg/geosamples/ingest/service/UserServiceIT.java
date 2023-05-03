package gov.noaa.ncei.mgg.geosamples.ingest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.AttachedFacilityVIew;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.UserSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.UserView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.PagedItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.security.Authorities;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesAuthorityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesRoleAuthorityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesRoleEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesUserEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.CuratorsFacilityRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.GeosamplesAuthorityRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.GeosamplesRoleRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.GeosamplesUserRepository;
import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest
@ActiveProfiles("it")
public class UserServiceIT {

  @Autowired
  private GeosamplesUserRepository geosamplesUserRepository;

  @Autowired
  private TransactionTemplate transactionTemplate;

  @Autowired
  private UserService userService;

  @Autowired
  private GeosamplesRoleRepository geosamplesRoleRepository;

  @Autowired
  private GeosamplesAuthorityRepository geosamplesAuthorityRepository;

  @Autowired
  private CuratorsFacilityRepository curatorsFacilityRepository;

  @BeforeEach
  public void beforeEach() {
    cleanDb();
  }

  @AfterEach
  public void afterEach() {
    cleanDb();
  }

  @Test
  public void testSearchByUsernameContains() {
    transactionTemplate.executeWithoutResult(s -> {
      GeosamplesRoleEntity role = new GeosamplesRoleEntity();
      role.setRoleName("ROLE_USER");
      role = geosamplesRoleRepository.save(role);

      GeosamplesUserEntity user = new GeosamplesUserEntity();
      user.setUserName("gabby");
      user.setDisplayName("Gabby Glacier");
      user.setVersion(1);
      user.setUserRole(role);
      geosamplesUserRepository.save(user);
    });

    UserSearchParameters searchParameters = new UserSearchParameters();
    searchParameters.setUserNameContains(Collections.singletonList("g"));
    PagedItemsView<UserView> pagedItemsView = userService.search(searchParameters);
    assertEquals(1, pagedItemsView.getTotalItems());
    assertEquals(searchParameters.getItemsPerPage(), pagedItemsView.getItemsPerPage());
    assertEquals(1, pagedItemsView.getTotalPages());
    assertEquals(searchParameters.getPage(), pagedItemsView.getPage());
    List<UserView> users = pagedItemsView.getItems();
    assertEquals(1, users.size());

    UserView userView = users.get(0);
    assertEquals("gabby", userView.getUserName());
    assertEquals("Gabby Glacier", userView.getDisplayName());
  }

  @Test
  public void testSearchByUsernameEquals() {
    transactionTemplate.executeWithoutResult(s -> {
      GeosamplesRoleEntity role = new GeosamplesRoleEntity();
      role.setRoleName("ROLE_USER");
      role = geosamplesRoleRepository.save(role);

      GeosamplesUserEntity user = new GeosamplesUserEntity();
      user.setUserName("gabby");
      user.setDisplayName("Gabby Glacier");
      user.setUserRole(role);
      user.setVersion(1);
      geosamplesUserRepository.save(user);
    });

    UserSearchParameters searchParameters = new UserSearchParameters();
    searchParameters.setUserNameEquals(Collections.singletonList("gabby"));
    PagedItemsView<UserView> pagedItemsView = userService.search(searchParameters);
    assertEquals(1, pagedItemsView.getTotalItems());
    assertEquals(searchParameters.getItemsPerPage(), pagedItemsView.getItemsPerPage());
    assertEquals(1, pagedItemsView.getTotalPages());
    assertEquals(searchParameters.getPage(), pagedItemsView.getPage());
    List<UserView> users = pagedItemsView.getItems();
    assertEquals(1, users.size());

    UserView userView = users.get(0);
    assertEquals("gabby", userView.getUserName());
    assertEquals("Gabby Glacier", userView.getDisplayName());
  }

  @Test
  public void testSearchByDisplayNameContains() {
    transactionTemplate.executeWithoutResult(s -> {
      GeosamplesRoleEntity role = new GeosamplesRoleEntity();
      role.setRoleName("ROLE_USER");
      role = geosamplesRoleRepository.save(role);

      GeosamplesUserEntity user = new GeosamplesUserEntity();
      user.setUserName("gabby");
      user.setDisplayName("Gabby Glacier");
      user.setVersion(1);
      user.setUserRole(role);
      geosamplesUserRepository.save(user);
    });

    UserSearchParameters searchParameters = new UserSearchParameters();
    searchParameters.setDisplayNameContains(Collections.singletonList("g"));
    PagedItemsView<UserView> pagedItemsView = userService.search(searchParameters);
    assertEquals(1, pagedItemsView.getTotalItems());
    assertEquals(searchParameters.getItemsPerPage(), pagedItemsView.getItemsPerPage());
    assertEquals(1, pagedItemsView.getTotalPages());
    assertEquals(searchParameters.getPage(), pagedItemsView.getPage());
    List<UserView> users = pagedItemsView.getItems();
    assertEquals(1, users.size());

    UserView userView = users.get(0);
    assertEquals("gabby", userView.getUserName());
    assertEquals("Gabby Glacier", userView.getDisplayName());
  }

  @Test
  public void testGetUserAuthoritiesRoleDefined() {
    transactionTemplate.executeWithoutResult(s -> {
      GeosamplesRoleEntity role = new GeosamplesRoleEntity();
      role.setRoleName("ROLE_USER");
      role = geosamplesRoleRepository.save(role);

      for (GeosamplesAuthorityEntity authority : geosamplesAuthorityRepository.findAll()) {
        GeosamplesRoleAuthorityEntity roleAuthority = new GeosamplesRoleAuthorityEntity();
        roleAuthority.setAuthority(authority);
        role.addRoleAuthority(roleAuthority);
      }

      GeosamplesUserEntity user = new GeosamplesUserEntity();
      user.setUserName("gabby");
      user.setDisplayName("Gabby Glacier");
      user.setVersion(1);
      user.setUserRole(role);
      geosamplesUserRepository.save(user);
    });

    List<String> authorities = userService.getUserAuthorities("gabby");
    assertEquals(geosamplesAuthorityRepository.count() + 1, authorities.size());

    Set<String> expectedAuthorities = geosamplesAuthorityRepository.findAll().stream().map(GeosamplesAuthorityEntity::getAuthorityName).collect(Collectors.toSet());
    expectedAuthorities.add(Authorities.ROLE_AUTHENTICATED_USER.toString());
    assertEquals(
        new HashSet<>(authorities),
        expectedAuthorities
    );
  }

  @Test
  public void testGetUserAuthoritiesRoleNotDefined() {
    transactionTemplate.executeWithoutResult(s -> {
      GeosamplesUserEntity user = new GeosamplesUserEntity();
      user.setUserName("gabby");
      user.setDisplayName("Gabby Glacier");
      user.setVersion(1);
      geosamplesUserRepository.save(user);
    });

    List<String> authorities = userService.getUserAuthorities("gabby");
    assertEquals(1, authorities.size());
    assertEquals(Authorities.ROLE_AUTHENTICATED_USER.toString(), authorities.get(0));
  }

  @Test
  public void testSaveUserWithFacility() {
    GeosamplesRoleEntity roleEntity = transactionTemplate.execute(s -> {
      GeosamplesRoleEntity role = new GeosamplesRoleEntity();
      role.setRoleName("ROLE_USER");
      return geosamplesRoleRepository.save(role);
    });
    assertNotNull(roleEntity);

    CuratorsFacilityEntity facilityEntity = transactionTemplate.execute(s -> {
      CuratorsFacilityEntity facility = new CuratorsFacilityEntity();
      facility.setFacilityCode("TEST");
      facility.setFacility("Test Facility");
      facility.setInstCode("TST");
      facility.setLastUpdate(Instant.now());
      return curatorsFacilityRepository.save(facility);
    });
    assertNotNull(facilityEntity);

    UserView userView = new UserView();
    userView.setUserName("gabby");
    userView.setDisplayName("Gabby Glacier");
    userView.setRole(roleEntity.getRoleName());

    AttachedFacilityVIew attachedFacilityVIew = new AttachedFacilityVIew();
    attachedFacilityVIew.setId(facilityEntity.getId());
    attachedFacilityVIew.setFacilityCode(facilityEntity.getFacilityCode());
    userView.setFacility(attachedFacilityVIew);

    UserView savedUser = userService.create(userView);
    assertEquals(facilityEntity.getFacilityCode(), savedUser.getFacility().getFacilityCode());
    assertEquals(facilityEntity.getId(), savedUser.getFacility().getId());

    transactionTemplate.executeWithoutResult(s -> {
      GeosamplesUserEntity userEntity = geosamplesUserRepository.findById("gabby").orElse(null);
      assertNotNull(userEntity);
      assertEquals(facilityEntity.getId(), userEntity.getFacility().getId());
      assertEquals(facilityEntity.getFacilityCode(), userEntity.getFacility().getFacilityCode());
    });
  }

  @Test
  public void testUpdateUserWithFacility() {
    GeosamplesRoleEntity roleEntity = transactionTemplate.execute(s -> {
      GeosamplesRoleEntity role = new GeosamplesRoleEntity();
      role.setRoleName("ROLE_USER");
      return geosamplesRoleRepository.save(role);
    });
    assertNotNull(roleEntity);

    CuratorsFacilityEntity facilityEntity = transactionTemplate.execute(s -> {
      CuratorsFacilityEntity facility = new CuratorsFacilityEntity();
      facility.setFacilityCode("TEST");
      facility.setFacility("Test Facility");
      facility.setInstCode("TST");
      facility.setLastUpdate(Instant.now());
      return curatorsFacilityRepository.save(facility);
    });
    assertNotNull(facilityEntity);

    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity user = new GeosamplesUserEntity();
      user.setUserName("gabby");
      user.setDisplayName("Gabby Glacier");
      user.setVersion(1);
      user.setUserRole(roleEntity);
      return geosamplesUserRepository.save(user);
    });
    assertNotNull(userEntity);

    UserView userView = new UserView();
    userView.setUserName(userEntity.getUserName());
    userView.setDisplayName(userEntity.getDisplayName());
    userView.setRole(roleEntity.getRoleName());

    AttachedFacilityVIew attachedFacilityVIew = new AttachedFacilityVIew();
    attachedFacilityVIew.setId(facilityEntity.getId());
    attachedFacilityVIew.setFacilityCode(facilityEntity.getFacilityCode());
    userView.setFacility(attachedFacilityVIew);

    UserView savedUser = userService.update(userView, userEntity.getUserName());
    assertEquals(facilityEntity.getFacilityCode(), savedUser.getFacility().getFacilityCode());
    assertEquals(facilityEntity.getId(), savedUser.getFacility().getId());

    transactionTemplate.executeWithoutResult(s -> {
      GeosamplesUserEntity user = geosamplesUserRepository.findById("gabby").orElse(null);
      assertNotNull(user);
      assertEquals(facilityEntity.getId(), user.getFacility().getId());
      assertEquals(facilityEntity.getFacilityCode(), user.getFacility().getFacilityCode());
    });
  }

  @Test
  public void testUpdateUserChangeFacility() {
    GeosamplesRoleEntity roleEntity = transactionTemplate.execute(s -> {
      GeosamplesRoleEntity role = new GeosamplesRoleEntity();
      role.setRoleName("ROLE_USER");
      return geosamplesRoleRepository.save(role);
    });
    assertNotNull(roleEntity);

    CuratorsFacilityEntity facilityEntity = transactionTemplate.execute(s -> {
      CuratorsFacilityEntity facility = new CuratorsFacilityEntity();
      facility.setFacilityCode("TEST");
      facility.setFacility("Test Facility");
      facility.setInstCode("TST");
      facility.setLastUpdate(Instant.now());
      return curatorsFacilityRepository.save(facility);
    });
    assertNotNull(facilityEntity);

    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity user = new GeosamplesUserEntity();
      user.setUserName("gabby");
      user.setDisplayName("Gabby Glacier");
      user.setVersion(1);
      user.setUserRole(roleEntity);
      user.setFacility(facilityEntity);
      return geosamplesUserRepository.save(user);
    });
    assertNotNull(userEntity);

    CuratorsFacilityEntity newFacility = transactionTemplate.execute(s -> {
      CuratorsFacilityEntity facility = new CuratorsFacilityEntity();
      facility.setFacilityCode("TEST2");
      facility.setFacility("Test Facility 2");
      facility.setInstCode("TS2");
      facility.setLastUpdate(Instant.now());
      return curatorsFacilityRepository.save(facility);
    });
    assertNotNull(newFacility);

    UserView userView = new UserView();
    userView.setUserName(userEntity.getUserName());
    userView.setDisplayName(userEntity.getDisplayName());
    userView.setRole(roleEntity.getRoleName());

    AttachedFacilityVIew attachedFacilityVIew = new AttachedFacilityVIew();
    attachedFacilityVIew.setId(newFacility.getId());
    attachedFacilityVIew.setFacilityCode(newFacility.getFacilityCode());
    userView.setFacility(attachedFacilityVIew);

    UserView savedUser = userService.update(userView, userEntity.getUserName());
    assertEquals(newFacility.getFacilityCode(), savedUser.getFacility().getFacilityCode());
    assertEquals(newFacility.getId(), savedUser.getFacility().getId());

    transactionTemplate.executeWithoutResult(s -> {
      GeosamplesUserEntity user = geosamplesUserRepository.findById("gabby").orElse(null);
      assertNotNull(user);
      assertEquals(newFacility.getId(), user.getFacility().getId());
      assertEquals(newFacility.getFacilityCode(), user.getFacility().getFacilityCode());
    });
  }

  @Test
  public void testSearchUserByFacility() {
    GeosamplesRoleEntity roleEntity = transactionTemplate.execute(s -> {
      GeosamplesRoleEntity role = new GeosamplesRoleEntity();
      role.setRoleName("ROLE_USER");
      return geosamplesRoleRepository.save(role);
    });
    assertNotNull(roleEntity);

    CuratorsFacilityEntity facilityEntity = transactionTemplate.execute(s -> {
      CuratorsFacilityEntity facility = new CuratorsFacilityEntity();
      facility.setFacilityCode("TEST");
      facility.setFacility("Test Facility");
      facility.setInstCode("TST");
      facility.setLastUpdate(Instant.now());
      return curatorsFacilityRepository.save(facility);
    });
    assertNotNull(facilityEntity);

    GeosamplesUserEntity userEntity = transactionTemplate.execute(s -> {
      GeosamplesUserEntity user = new GeosamplesUserEntity();
      user.setUserName("gabby");
      user.setDisplayName("Gabby Glacier");
      user.setVersion(1);
      user.setUserRole(roleEntity);
      user.setFacility(facilityEntity);
      return geosamplesUserRepository.save(user);
    });
    assertNotNull(userEntity);

    CuratorsFacilityEntity willNotMatchFacility = transactionTemplate.execute(s -> {
      CuratorsFacilityEntity facility = new CuratorsFacilityEntity();
      facility.setFacilityCode("TEST2");
      facility.setFacility("Test Facility 2");
      facility.setInstCode("TS2");
      facility.setLastUpdate(Instant.now());
      return curatorsFacilityRepository.save(facility);
    });
    assertNotNull(willNotMatchFacility);

    GeosamplesUserEntity willNotMatchUser = transactionTemplate.execute(s -> {
      GeosamplesUserEntity user = new GeosamplesUserEntity();
      user.setUserName("gabby2");
      user.setDisplayName("Gabby Glacier 2");
      user.setVersion(1);
      user.setUserRole(roleEntity);
      user.setFacility(willNotMatchFacility);
      return geosamplesUserRepository.save(user);
    });
    assertNotNull(willNotMatchUser);

    UserSearchParameters searchParameters = new UserSearchParameters();
    searchParameters.setFacilityCode(Collections.singletonList(facilityEntity.getFacilityCode()));
    searchParameters.setPage(1);
    searchParameters.setItemsPerPage(10);
    PagedItemsView<UserView> result = userService.search(searchParameters);

    assertEquals(1, result.getItems().size());
    assertEquals(1, result.getTotalItems());
    assertEquals(1, result.getTotalPages());
    assertEquals(1, result.getPage());
    assertEquals(10, result.getItemsPerPage());
    assertEquals(userEntity.getUserName(), result.getItems().get(0).getUserName());
    assertEquals(userEntity.getDisplayName(), result.getItems().get(0).getDisplayName());
    assertEquals(roleEntity.getRoleName(), result.getItems().get(0).getRole());
    assertEquals(facilityEntity.getFacilityCode(), result.getItems().get(0).getFacility().getFacilityCode());
    assertEquals(facilityEntity.getId(), result.getItems().get(0).getFacility().getId());
  }

  private void cleanDb() {
    transactionTemplate.executeWithoutResult(s -> {
      if (geosamplesUserRepository.existsById("gabby")) {
        geosamplesUserRepository.deleteById("gabby");
      }
      if (geosamplesUserRepository.existsById("gabby2")) {
        geosamplesUserRepository.deleteById("gabby2");
      }
      geosamplesRoleRepository.getByRoleName("ROLE_USER").ifPresent(geosamplesRoleEntity -> geosamplesRoleRepository.delete(geosamplesRoleEntity));
      curatorsFacilityRepository.findByFacilityCode("TEST").ifPresent(curatorsFacilityEntity -> curatorsFacilityRepository.delete(curatorsFacilityEntity));
      curatorsFacilityRepository.findByFacilityCode("TEST2").ifPresent(curatorsFacilityEntity -> curatorsFacilityRepository.delete(curatorsFacilityEntity));
    });
  }

}
