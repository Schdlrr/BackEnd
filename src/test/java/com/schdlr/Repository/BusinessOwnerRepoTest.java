package com.schdlr.Repository;

import com.schdlr.model.BusinessOwner;
import com.schdlr.repo.BusinessOwnerRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@Testable
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class BusinessOwnerRepoTest {

	private BusinessOwnerRepo businessOwnerRepo;

	private BusinessOwner businessOwner1;
	private BusinessOwner businessOwner2;
	private BusinessOwner businessOwner3;

	public BusinessOwnerRepoTest(@Autowired BusinessOwnerRepo businessOwnerRepo){
		this.businessOwnerRepo = businessOwnerRepo;
	}

	@BeforeEach
	public void setUp(){
		businessOwner1 = BusinessOwner.builder()
				.userName("Erdi Syla").email("erdisyla6@gmail.com")
				.password("stop").number("045210022").build();
		businessOwner2 = BusinessOwner.builder()
				.userName("Idre Alys").email("erdisyla7@gmail.com")
				.password("go").number("045220022").build();
		businessOwner3 = BusinessOwner.builder()
				.userName("Drei Lasy").email("erdisyla8@gmail.com")
				.password("hold").number("045210021").build();
	}

	@Test
	public void BusinessOwnerRepoTest_testSave_ReturnsOwner(){
		BusinessOwner savedOwner = businessOwnerRepo.saveAndFlush(businessOwner1);

		assertThat(savedOwner).isNotNull();
		assertThat(savedOwner.getUserName()).isEqualTo(businessOwner1.getUserName());
		assertThat(savedOwner.getNumber()).isEqualTo(businessOwner1.getNumber());
		assertThat(savedOwner.getEmail()).isEqualTo(businessOwner1.getEmail());
		assertThat(savedOwner.getPassword()).isEqualTo(businessOwner1.getPassword());
		assertThat(savedOwner.getBusinessName()).isEqualTo(businessOwner1.getBusinessName());
		assertThat(savedOwner.getBusinessAddress()).isEqualTo(businessOwner1.getBusinessAddress());
	}
}
