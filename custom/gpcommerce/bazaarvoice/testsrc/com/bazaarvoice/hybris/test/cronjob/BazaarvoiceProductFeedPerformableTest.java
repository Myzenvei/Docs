package com.bazaarvoice.hybris.test.cronjob;

import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * Created with IntelliJ IDEA. User: Artem
 */
@IntegrationTest
//public class BazaarvoiceProductFeedPerformableTest extends HybrisJUnit4TransactionalTest {
public class BazaarvoiceProductFeedPerformableTest extends ServicelayerTransactionalTest
{

	/** Edit the local|project.properties to change logging behaviour (properties log4j.*). */
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(BazaarvoiceProductFeedPerformableTest.class.getName());

	@Before
	public void setUp()
	{

		// implement here code executed before each test
	}

	@After
	public void tearDown()
	{
		// implement here code executed after each test
	}

	/**
	 * This is a sample test method.
	 */
	@Test
	public void testBazaarvoice()
	{
		final boolean testTrue = true;
		assertTrue("true is not true", testTrue);
	}
}
