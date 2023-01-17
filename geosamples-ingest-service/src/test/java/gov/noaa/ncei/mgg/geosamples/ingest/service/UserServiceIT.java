package gov.noaa.ncei.mgg.geosamples.ingest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import gov.noaa.ncei.mgg.geosamples.ingest.api.model.UserSearchParameters;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.UserView;
import gov.noaa.ncei.mgg.geosamples.ingest.api.model.paging.PagedItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.GeosamplesUserEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.repository.GeosamplesUserRepository;
import java.util.Collections;
import java.util.List;
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
      GeosamplesUserEntity user = new GeosamplesUserEntity();
      user.setUserName("gabby");
      user.setDisplayName("Gabby Glacier");
      user.setVersion(1);
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
      GeosamplesUserEntity user = new GeosamplesUserEntity();
      user.setUserName("gabby");
      user.setDisplayName("Gabby Glacier");
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
      GeosamplesUserEntity user = new GeosamplesUserEntity();
      user.setUserName("gabby");
      user.setDisplayName("Gabby Glacier");
      user.setVersion(1);
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

  private void cleanDb() {
    transactionTemplate.executeWithoutResult(s -> geosamplesUserRepository.deleteAll());
  }

}
