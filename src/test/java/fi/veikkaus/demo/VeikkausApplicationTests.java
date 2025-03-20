package fi.veikkaus.demo;

import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.jdbc.core.JdbcTemplate;


@SpringBootTest(properties = "spring.profiles.active=test",
                webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.liquibase.enabled=false",
                                  "spring.datasource.url=jdbc:h2:mem:testdb",
                                  "spring.datasource.driver-class-name=org.h2.Driver",
                                  "spring.jpa.hibernate.ddl-auto=none"})
@AutoConfigureMockMvc
class VeikkausApplicationTests {
  @MockitoBean
  private DataSource dataSource;
  
  @MockitoBean
  private JdbcTemplate jdbcTemplate;
  
  @Test
  void contextLoads() {
  }
  
  
}
