package com.shoppertrak;

import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.shoppertrak.domain.TrafficRecord;
import com.shoppertrak.service.TrafficService;

import static org.junit.Assert.assertTrue;

//Ideally I'd like to mock out the TrafficDAO, but running low on time

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
public class TrafficServiceTest {

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
	}
	
	@Test
	public void getTrafficByClientReturnsZeroIntervalsForNonExistantClient() {	
		populateData();
	}
	
	@Test
	public void getTrafficByClientForStoreReturnsValidIntervals() {	
		populateData();
	}
	
	@Test
	public void getTrafficByClientForStoreReturnsZeroIntervalsForNonExistantClient() {	
		populateData();
	}
	
	private void populateData() {
		//adding independent data for use in tests. Don't want to make assumption sample data will always be present
		List<TrafficRecord> l = TestHelper.getSampleData();
		for(TrafficRecord tr: l){
			target.save(tr);
		}
		       
	}
	
}
