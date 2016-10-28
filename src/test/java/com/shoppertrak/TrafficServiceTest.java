package com.shoppertrak;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.shoppertrak.domain.DateRange;
import com.shoppertrak.domain.TrafficRecord;
import com.shoppertrak.domain.TrafficRecordAggregate;
import com.shoppertrak.domain.TrafficRecordSet;
import com.shoppertrak.service.TrafficService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

//Ideally I'd like to mock out the TrafficDAO, but running low on time

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
public class TrafficServiceTest {

	private static final String startDate = "201603010000";
	private static final String endDate = "201603010030";
	private static final String endDateConstricted = "201603010015";
	private static final DateRange testRange = new DateRange(startDate,endDate);
	private static final DateRange testRangeConstricted = new DateRange(startDate,endDateConstricted);
	private static final int testClientId = 300;
	private static final int testStoreId = 3000;
	private static final int badClientId = 303453450;
	private static final Date expectedFirstIntervalDate = TestHelper.parseDate("201603010015");
	private static final Date expectedSecondIntervalDate = TestHelper.parseDate("201603010030");
	
	@Autowired
	TrafficService target;
	
	@Before
	public void init() {}

	@After
	public void clean() {}
	
	@Test
	public void getAllTest() {	
		Collection<TrafficRecord> ret = target.getAll();
		assertTrue(ret.size()>0);
	}
	
	@Test
	public void getTrafficByClientReturnsValidIntervals() {	
		populateData();
		TrafficRecordSet result = target.getTrafficByClient(testClientId, testRange);
		
		
		assertTrue(result.getTraffic().size() < 3);
		TrafficRecordAggregate firstInterval = result.getTraffic().get(0);
	    TrafficRecordAggregate secondInterval = result.getTraffic().get(1);
	
		assertEquals((int) result.getClientId(),testClientId);
	    assertEquals(firstInterval.getInterval(),expectedFirstIntervalDate);
	    assertEquals(firstInterval.getEnters(),12);
	    assertEquals(firstInterval.getExits(),16);
	    assertEquals(secondInterval.getInterval(),expectedSecondIntervalDate);
	    assertEquals(secondInterval.getEnters(),30);
	    assertEquals(secondInterval.getExits(),18);
	}
	
	@Test
	public void getTrafficByClientReturnsValidIntervalConstrictedDate() {	
		populateData();
		TrafficRecordSet result = target.getTrafficByClient(testClientId, testRangeConstricted);
		
		assertTrue(result.getTraffic().size() < 2);
		TrafficRecordAggregate firstInterval = result.getTraffic().get(0);
	    
		assertEquals((int) result.getClientId(),testClientId);
	    assertEquals(firstInterval.getInterval(),expectedFirstIntervalDate);
	    assertEquals(firstInterval.getEnters(),12);
	    assertEquals(firstInterval.getExits(),16);

	}
	
	@Test
	public void getTrafficByClientReturnsZeroIntervalsForNonExistantClient() {	
		populateData();
		TrafficRecordSet result = target.getTrafficByClient(badClientId, testRange);
		
		assertTrue(result.getTraffic().size() < 3);
		TrafficRecordAggregate firstInterval = result.getTraffic().get(0);
	    TrafficRecordAggregate secondInterval = result.getTraffic().get(1);
	    
		assertEquals((int) result.getClientId(),badClientId);
	    assertEquals(firstInterval.getInterval(),expectedFirstIntervalDate);
	    assertEquals(firstInterval.getEnters(),0);
	    assertEquals(firstInterval.getExits(),0);
	    assertEquals(secondInterval.getInterval(),expectedSecondIntervalDate);
	    assertEquals(secondInterval.getEnters(),0);
	    assertEquals(secondInterval.getExits(),0);
	}
	
	@Test
	public void getTrafficByClientForStoreReturnsValidIntervals() {	
		populateData();
		TrafficRecordSet result = target.getTrafficByClientForStore(testClientId, testStoreId, testRange);
		
		assertTrue(result.getTraffic().size() < 3);
		TrafficRecordAggregate firstInterval = result.getTraffic().get(0);
	    TrafficRecordAggregate secondInterval = result.getTraffic().get(1);
	    
		assertEquals((int) result.getStoreId(),testStoreId);
		assertEquals((int) result.getClientId(),testClientId);
	    assertEquals(firstInterval.getInterval(),expectedFirstIntervalDate);
	    assertEquals(firstInterval.getEnters(),8);
	    assertEquals(firstInterval.getExits(),12);
	    assertEquals(secondInterval.getInterval(),expectedSecondIntervalDate);
	    assertEquals(secondInterval.getEnters(),17);
	    assertEquals(secondInterval.getExits(),9);
	}
	
	@Test
	public void getTrafficByClientForStoreReturnsValidIntervalConstrictedDate() {	
		populateData();
		TrafficRecordSet result = target.getTrafficByClientForStore(testClientId, testStoreId, testRangeConstricted);
		
		assertTrue(result.getTraffic().size() < 2);
		TrafficRecordAggregate firstInterval = result.getTraffic().get(0);
	    
		assertEquals((int) result.getStoreId(),testStoreId);
		assertEquals((int) result.getClientId(),testClientId);
	    assertEquals(firstInterval.getInterval(),expectedFirstIntervalDate);
	    assertEquals(firstInterval.getEnters(),8);
	    assertEquals(firstInterval.getExits(),12);

	}
	
	@Test
	public void getTrafficByClientForStoreReturnsZeroIntervalsForNonExistantClient() {	
		populateData();
		TrafficRecordSet result = target.getTrafficByClientForStore(badClientId, testStoreId, testRange);
		
		assertTrue(result.getTraffic().size() < 3);
		TrafficRecordAggregate firstInterval = result.getTraffic().get(0);
	    TrafficRecordAggregate secondInterval = result.getTraffic().get(1);
	    
		assertEquals((int) result.getStoreId(),testStoreId);
		assertEquals((int) result.getClientId(),badClientId);
	    assertEquals(firstInterval.getInterval(),expectedFirstIntervalDate);
	    assertEquals(firstInterval.getEnters(),0);
	    assertEquals(firstInterval.getExits(),0);
	    assertEquals(secondInterval.getInterval(),expectedSecondIntervalDate);
	    assertEquals(secondInterval.getEnters(),0);
	    assertEquals(secondInterval.getExits(),0);
	}
	
	private void populateData() {
		//adding independent data for use in tests. Don't want to make assumption sample data will always be present
		List<TrafficRecord> l = TestHelper.getSampleData();
		for(TrafficRecord tr: l){
			target.save(tr);
		}
		       
	}
	
}
