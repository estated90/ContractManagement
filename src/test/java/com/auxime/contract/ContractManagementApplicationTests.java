package com.auxime.contract;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ContractManagementApplicationTests {

	@Test
	void contextLoads() {
		ContractManagementApplication.main(new String[] {});
		assertTrue(true);
	}

}
