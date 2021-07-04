package com.org.lob.project;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;

public class BaseMockitoTest {

	private AutoCloseable closeable;

	@BeforeEach
	public void openMocks() {
		closeable = MockitoAnnotations.openMocks(this);
	}

	@AfterEach
	public void releaseMocks() throws Exception {
		closeable.close();
	}
}
