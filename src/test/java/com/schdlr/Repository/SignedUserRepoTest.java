package com.schdlr.Repository;

import com.schdlr.model.SignedUser;
import com.schdlr.repo.SignedUserRepo;
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
public class SignedUserRepoTest {

	private SignedUserRepo signedUserRepo;

	private SignedUser signedUser1;
	private SignedUser signedUser2;
	private SignedUser signedUser3;

	public SignedUserRepoTest(@Autowired SignedUserRepo signedUserRepo){
		this.signedUserRepo = signedUserRepo;
	}

	@BeforeEach
	public void setUp(){
		signedUser1 = SignedUser.builder()
				.userName("Erdi").email("erdisyla6@gmail.com")
				.password("stop").number("045210022").build();
		signedUser2 = SignedUser.builder()
				.userName("Idre").email("erdisyla7@gmail.com")
				.password("go").number("045220022").build();
		signedUser3 = SignedUser.builder()
				.userName("Drei").email("erdisyla8@gmail.com")
				.password("hold").number("045210021").build();
	}

	@Test
	public void SignedUserRepoTest_testSave_ReturnsUser(){
		SignedUser savedUser = signedUserRepo.saveAndFlush(signedUser1);

		assertThat(savedUser).isNotNull();
		assertThat(savedUser.getUserName()).isEqualTo(signedUser1.getUserName());
		assertThat(savedUser.getNumber()).isEqualTo(signedUser1.getNumber());
		assertThat(savedUser.getEmail()).isEqualTo(signedUser1.getEmail());
		assertThat(savedUser.getPassword()).isEqualTo(signedUser1.getPassword());
	}

	@Test
	public void SignedUserRepoTest_testUpdate_ReturnsUpdatedUser(){
		SignedUser savedUser = signedUserRepo.saveAndFlush(signedUser1);

		SignedUser updatedUser = signedUserRepo.findByEmail(savedUser.getEmail()).get();
		updatedUser.setUserName("Erdi Syla");
		updatedUser.setPassword("STOP");

		SignedUser finalUser =  signedUserRepo.saveAndFlush(updatedUser);

		assertThat(finalUser).isNotNull();
		assertThat(finalUser.getUserName()).isEqualTo("Erdi Syla");
		assertThat(finalUser.getPassword()).isEqualTo("STOP");
	}

	@Test
	public void SignedUserRepoTest_testDelete_ReturnsNothing(){
		SignedUser savedUser = signedUserRepo. saveAndFlush(signedUser1);

		signedUserRepo.delete(savedUser);
		Optional<SignedUser> deletedUser = signedUserRepo.findById(savedUser.getId());

		assertThat(deletedUser).isEmpty();
	}

	@Test
	public void SignedUserRepoTest_testFindUserByUsername_ReturnsUser(){
		signedUserRepo.saveAll(Arrays.asList(signedUser1,signedUser2,signedUser3));

		SignedUser ErdiUser = signedUserRepo.findByUserName("Erdi").get();

		assertThat(ErdiUser).isNotNull();
		assertThat(ErdiUser.getUserName()).isEqualTo(signedUser1.getUserName());
		assertThat(ErdiUser.getPassword()).isEqualTo(signedUser1.getPassword());
		assertThat(ErdiUser.getEmail()).isEqualTo(signedUser1.getEmail());
		assertThat(ErdiUser.getNumber()).isEqualTo(signedUser1.getNumber());
	}

	@Test
	public void SignedUserRepoTest_testFindByEmail_ReturnsUser(){
		signedUserRepo.saveAll(Arrays.asList(signedUser1,signedUser2,signedUser3));

		SignedUser ErdiUser = signedUserRepo.findByEmail("erdisyla8@gmail.com").get();

		assertThat(ErdiUser).isNotNull();
		assertThat(ErdiUser.getUserName()).isEqualTo(signedUser3.getUserName());
		assertThat(ErdiUser.getPassword()).isEqualTo(signedUser3.getPassword());
		assertThat(ErdiUser.getEmail()).isEqualTo(signedUser3.getEmail());
		assertThat(ErdiUser.getNumber()).isEqualTo(signedUser3.getNumber());
	}
}
