package gov.noaa.ncei.mgg.geosamples.ingest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gov.noaa.ncei.mgg.errorhandler.ApiException;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.RoleSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.RoleView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.PagedItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.security.Authorities;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesRoleAuthorityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesRoleEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.GeosamplesAuthorityRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.GeosamplesRoleRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest
@ActiveProfiles("it")
public class RoleServiceIT {

  @Autowired
  private TransactionTemplate transactionTemplate;

  @Autowired
  private RoleService roleService;

  @Autowired
  private GeosamplesRoleRepository geosamplesRoleRepository;

  @Autowired
  private GeosamplesAuthorityRepository geosamplesAuthorityRepository;

  private final String ROLE_NAME = "ROLE_TEST";

  @BeforeEach
  public void beforeEach() {
    cleanDB();
  }

  @AfterEach
  public void afterEach() {
    cleanDB();
  }

  @Test
  public void testCreateRole() {
    RoleView toCreate = new RoleView();
    toCreate.setRoleName(ROLE_NAME);
    List<String> authorities = new ArrayList<>();
    authorities.add(Authorities.ROLE_AGE_CREATE.name());
    authorities.add(Authorities.ROLE_AGE_READ.name());
    toCreate.setAuthorities(authorities);
    RoleView result = roleService.create(toCreate);
    assertEquals(toCreate.getRoleName(), result.getRoleName());
    assertEquals(
        new HashSet<>(result.getAuthorities()),
        new HashSet<>(toCreate.getAuthorities())
    );
    transactionTemplate.executeWithoutResult(s -> {
      GeosamplesRoleEntity resultEntity = geosamplesRoleRepository.findById(result.getId()).orElseThrow(
          () -> new RuntimeException("Role not found")
      );
      assertEquals(result.getId(), resultEntity.getId());
      assertEquals(result.getVersion(), resultEntity.getVersion());
      assertEquals(result.getRoleName(), resultEntity.getRoleName());
      assertEquals(
          new HashSet<>(resultEntity.getRoleAuthorities().stream().map(
              authority -> authority.getAuthority().getAuthorityName()
          ).collect(Collectors.toList())),
          new HashSet<>(result.getAuthorities())
      );
    });
  }

  @Test
  public void testCreateRoleNameAlreadyTaken() {
    transactionTemplate.executeWithoutResult(s -> {
      GeosamplesRoleEntity roleEntity = new GeosamplesRoleEntity();
      roleEntity.setRoleName(ROLE_NAME);
      geosamplesRoleRepository.save(roleEntity);
    });

    RoleView toCreate = new RoleView();
    toCreate.setRoleName(ROLE_NAME);
    List<String> authorities = new ArrayList<>();
    authorities.add(Authorities.ROLE_AGE_CREATE.name());
    authorities.add(Authorities.ROLE_AGE_READ.name());
    toCreate.setAuthorities(authorities);

    assertThrows(DataIntegrityViolationException.class, () -> roleService.create(toCreate));
  }

  @Test
  public void testGetRole() {
    final Long id = transactionTemplate.execute(s -> {
      GeosamplesRoleEntity roleEntity = new GeosamplesRoleEntity();
      roleEntity.setRoleName(ROLE_NAME);
      roleEntity = geosamplesRoleRepository.save(roleEntity);
      GeosamplesRoleAuthorityEntity roleAuthorityEntity = new GeosamplesRoleAuthorityEntity();
      roleAuthorityEntity.setRole(roleEntity);
      roleAuthorityEntity.setAuthority(geosamplesAuthorityRepository.findById(Authorities.ROLE_AGE_CREATE.name()).orElseThrow(
          () -> new RuntimeException("Authority not found")
      ));
      roleEntity.addRoleAuthority(roleAuthorityEntity);
      return geosamplesRoleRepository.save(roleEntity).getId();
    });

    RoleView result = roleService.get(id);
    assertEquals(id, result.getId());
    assertEquals(ROLE_NAME, result.getRoleName());
    assertEquals(
        Collections.singletonList(Authorities.ROLE_AGE_CREATE.name()),
        result.getAuthorities()
    );
  }

  @Test
  public void testGetRoleNotFound() {
    ApiException exception = assertThrows(ApiException.class, () -> roleService.get(1000L));
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testSearchRoleByRoleName() {
    final GeosamplesRoleEntity roleEntity1 = transactionTemplate.execute(s -> {
      GeosamplesRoleEntity role = new GeosamplesRoleEntity();
      role.setRoleName(ROLE_NAME);
      return geosamplesRoleRepository.save(role);
    });
    assertNotNull(roleEntity1);

    transactionTemplate.executeWithoutResult(s -> {
      GeosamplesRoleEntity role = new GeosamplesRoleEntity();
      role.setRoleName("not matching");
      geosamplesRoleRepository.save(role);
    });

    final RoleSearchParameters roleSearchParameters = new RoleSearchParameters();
    roleSearchParameters.setPage(1);
    roleSearchParameters.setItemsPerPage(10);
    roleSearchParameters.setRoleNameContains(Collections.singletonList(ROLE_NAME));

    PagedItemsView<RoleView> result = roleService.search(roleSearchParameters);
    assertEquals(1, result.getItems().size());
    assertEquals(1, result.getTotalItems());
    assertEquals(1, result.getTotalPages());
    assertEquals(1, result.getPage());
    assertEquals(10, result.getItemsPerPage());
    assertEquals(roleEntity1.getId(), result.getItems().get(0).getId());
    assertEquals(roleEntity1.getVersion(), result.getItems().get(0).getVersion());
    assertEquals(roleEntity1.getRoleName(), result.getItems().get(0).getRoleName());
  }

  @Test
  public void testUpdateRole() {
    final GeosamplesRoleEntity roleEntity = transactionTemplate.execute(s -> {
      GeosamplesRoleEntity role = new GeosamplesRoleEntity();
      role.setRoleName(ROLE_NAME);
      role = geosamplesRoleRepository.save(role);
      GeosamplesRoleAuthorityEntity roleAuthorityEntity = new GeosamplesRoleAuthorityEntity();
      roleAuthorityEntity.setRole(role);
      roleAuthorityEntity.setAuthority(geosamplesAuthorityRepository.findById(Authorities.ROLE_AGE_UPDATE.name()).orElseThrow(
          () -> new RuntimeException("Authority not found")
      ));
      role.addRoleAuthority(roleAuthorityEntity);
      return geosamplesRoleRepository.save(role);
    });
    assertNotNull(roleEntity);

    RoleView toUpdate = new RoleView();
    toUpdate.setId(roleEntity.getId());
    toUpdate.setVersion(roleEntity.getVersion());
    toUpdate.setRoleName(ROLE_NAME + "-2");
    List<String> authorities = new ArrayList<>();
    authorities.add(Authorities.ROLE_AGE_CREATE.name());
    authorities.add(Authorities.ROLE_AGE_READ.name());
    toUpdate.setAuthorities(authorities);

    RoleView result = roleService.update(toUpdate, roleEntity.getId());
    assertEquals(toUpdate.getId(), result.getId());
    assertEquals(toUpdate.getVersion(), result.getVersion());
    assertEquals(toUpdate.getRoleName(), result.getRoleName());
    assertEquals(
        new HashSet<>(toUpdate.getAuthorities()),
        new HashSet<>(result.getAuthorities())
    );

    transactionTemplate.executeWithoutResult(s -> {
      GeosamplesRoleEntity role = geosamplesRoleRepository.findById(roleEntity.getId()).orElseThrow(
          () -> new RuntimeException("Role not found")
      );
      assertEquals(toUpdate.getId(), role.getId());
      assertEquals(toUpdate.getVersion() + 1, role.getVersion());
      assertEquals(toUpdate.getRoleName(), role.getRoleName());
      assertEquals(
          new HashSet<>(toUpdate.getAuthorities()),
          new HashSet<>(role.getRoleAuthorities().stream().map(
              authority -> authority.getAuthority().getAuthorityName()
          ).collect(Collectors.toList()))
      );
    });
  }

  @Test
  public void testUpdateRoleNotFound() {
    RoleView toUpdate = new RoleView();
    toUpdate.setId(1000L);
    toUpdate.setVersion(0L);
    toUpdate.setRoleName(ROLE_NAME + "-2");
    List<String> authorities = new ArrayList<>();
    authorities.add(Authorities.ROLE_AGE_CREATE.name());
    authorities.add(Authorities.ROLE_AGE_READ.name());
    toUpdate.setAuthorities(authorities);

    ApiException exception = assertThrows(ApiException.class, () -> roleService.update(toUpdate, 1000L));
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), exception.getApiError().getFlashErrors().get(0));
  }

  @Test
  public void testDeleteRole() {
    GeosamplesRoleEntity roleEntity = transactionTemplate.execute(s -> {
      GeosamplesRoleEntity role = new GeosamplesRoleEntity();
      role.setRoleName(ROLE_NAME);
      role = geosamplesRoleRepository.save(role);
      GeosamplesRoleAuthorityEntity roleAuthorityEntity = new GeosamplesRoleAuthorityEntity();
      roleAuthorityEntity.setRole(role);
      roleAuthorityEntity.setAuthority(geosamplesAuthorityRepository.findById(Authorities.ROLE_AGE_UPDATE.name()).orElseThrow(
          () -> new RuntimeException("Authority not found")
      ));
      role.addRoleAuthority(roleAuthorityEntity);
      return geosamplesRoleRepository.save(role);
    });
    assertNotNull(roleEntity);

    RoleView result = roleService.delete(roleEntity.getId());
    assertEquals(roleEntity.getId(), result.getId());
    assertEquals(roleEntity.getVersion(), result.getVersion());
    assertEquals(roleEntity.getRoleName(), result.getRoleName());
    assertEquals(
        new HashSet<>(roleEntity.getRoleAuthorities().stream().map(
            authority -> authority.getAuthority().getAuthorityName()
        ).collect(Collectors.toList())),
        new HashSet<>(result.getAuthorities())
    );
  }

  @Test
  public void testDeleteRoleNotFound() {
    ApiException exception = assertThrows(ApiException.class, () -> roleService.delete(1000L));
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(0, exception.getApiError().getFormErrors().size());
    assertEquals(1, exception.getApiError().getFlashErrors().size());
    assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), exception.getApiError().getFlashErrors().get(0));
  }

  private void cleanDB() {
    transactionTemplate.executeWithoutResult(status -> geosamplesRoleRepository.findAll().stream()
        .filter(role -> role.getRoleName().contains(ROLE_NAME) || role.getRoleName().equals("not matching"))
        .findFirst().ifPresent(
            role -> geosamplesRoleRepository.delete(role)
        ));
  }

}
