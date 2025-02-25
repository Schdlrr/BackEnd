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

import java.util.Arrays;
import java.util.Optional;

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

	@Test
	public void BusinessOwnerRepoTest_testUpdate_ReturnsUpdatedOwner(){
		BusinessOwner savedOwner = businessOwnerRepo.saveAndFlush(businessOwner1);

		BusinessOwner updatedOwner = businessOwnerRepo.findByEmail(savedOwner.getEmail()).get();
		updatedOwner.setUserName("Alexander Syla");
		updatedOwner.setPassword("STOP");

		BusinessOwner finalUser =  businessOwnerRepo.saveAndFlush(updatedOwner);

		assertThat(finalUser).isNotNull();
		assertThat(finalUser.getUserName()).isEqualTo("Alexander Syla");
		assertThat(finalUser.getPassword()).isEqualTo("STOP");
	}

	@Test
	public void BusinessOwnerTest_testDelete_ReturnsNothing(){
		BusinessOwner savedOwner = businessOwnerRepo. saveAndFlush(businessOwner1);

		businessOwnerRepo.delete(savedOwner);
		Optional<BusinessOwner> deletedUser = businessOwnerRepo.findById(savedOwner.getId());

		assertThat(deletedUser).isEmpty();
	}

	@Test
	public void BusinessOwnerRepoTest_testFindOwnerByUsername_ReturnsOwner(){
		businessOwnerRepo.saveAll(Arrays.asList(businessOwner1,businessOwner2,businessOwner3));

		BusinessOwner ErdiSylaOwner = businessOwnerRepo.findByUserName("Erdi Syla").get();

		assertThat(ErdiSylaOwner).isNotNull();
		assertThat(ErdiSylaOwner.getUserName()).isEqualTo(businessOwner1.getUserName());
		assertThat(ErdiSylaOwner.getPassword()).isEqualTo(businessOwner1.getPassword());
		assertThat(ErdiSylaOwner.getEmail()).isEqualTo(businessOwner1.getEmail());
		assertThat(ErdiSylaOwner.getNumber()).isEqualTo(businessOwner1.getNumber());
	}

	@Test
	public void BusinessOwnerRepoTest_testFindByEmail_ReturnsOwner(){
		businessOwnerRepo.saveAll(Arrays.asList(businessOwner1,businessOwner2,businessOwner3));

		BusinessOwner DreiLasyOwner = businessOwnerRepo.findByEmail("erdisyla8@gmail.com").get();

		assertThat(DreiLasyOwner).isNotNull();
		assertThat(DreiLasyOwner.getUserName()).isEqualTo(businessOwner3.getUserName());
		assertThat(DreiLasyOwner.getPassword()).isEqualTo(businessOwner3.getPassword());
		assertThat(DreiLasyOwner.getEmail()).isEqualTo(businessOwner3.getEmail());
		assertThat(DreiLasyOwner.getNumber()).isEqualTo(businessOwner3.getNumber());
		assertThat(DreiLasyOwner.getBusinessName()).isEqualTo(businessOwner3.getBusinessName());
		assertThat(DreiLasyOwner.getBusinessAddress()).isEqualTo(businessOwner3.getBusinessAddress());
	}

}
