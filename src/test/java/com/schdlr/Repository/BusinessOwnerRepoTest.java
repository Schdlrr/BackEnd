package com.schdlr.Repository;

import com.schdlr.model.BusinessOwner;
import com.schdlr.repo.BusinessOwnerRepo;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
}
