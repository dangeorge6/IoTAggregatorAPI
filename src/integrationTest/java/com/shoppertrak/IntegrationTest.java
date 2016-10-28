package com.shoppertrak;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jayway.restassured.RestAssured;
import com.shoppertrak.domain.TrafficRecord;
import com.shoppertrak.domain.TrafficRecordSet;
import com.shoppertrak.domain.TrafficRecordAggregate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebIntegrationTest(randomPort = true)
public class IntegrationTest {

	private static final String ID = "{id}";
	private static final String startDate = "201603010000";
	private static final String endDate = "201603010030";
	private static final String badDate = "202341603010030";
	private static final Date expectedFirstIntervalDate = TestHelper.parseDate("201603010015");
	private static final Date expectedSecondIntervalDate = TestHelper.parseDate("201603010030");
	
	@Value("${local.server.port}")
	private int port;

	@Before
	public void init() {
		RestAssured.port = port;
		RestAssured.basePath = "/api/v1.0/traffic";
	}

	@After
	public void clean() {

	}
	
	
	@Test
	public void crudTest() {	
		TrafficRecord r1 = generateRecord(10001);
		put(r1);
		get(r1);
		
		TrafficRecord r2 = generateRecord(10002);
		put(r2);
		get(r2);
		
		r1.setEnters(5).setExits(10);
		post(r1);
		get(r1);
		
		getAll();
		
		delete(r1);
		getNotFound(r1);
		get(r2);
		
		delete(r2);
		getNotFound(r2);
	}

	@Test
	public void getClientTrafficWillReturnCorrectIncrementValues() {	
		populateData();
		String pathToTest = String.format("/client/300/startTime/%s/endTime/%s",startDate,endDate);
		TrafficRecordSet ret =  
				when()
			        .get(pathToTest)
		         .then()
		        	.statusCode(200)
		         .extract().body().as(TrafficRecordSet.class);
			    
			    TrafficRecordAggregate firstInterval = ret.getTraffic().get(0);
			    TrafficRecordAggregate secondInterval = ret.getTraffic().get(1);
			    
			    assertEquals((int) ret.getClientId(),300);
			    assertEquals(firstInterval.getInterval(),expectedFirstIntervalDate);
			    assertEquals(firstInterval.getEnters(),12);
			    assertEquals(firstInterval.getExits(),16);
			    assertEquals(secondInterval.getInterval(),expectedSecondIntervalDate);
			    assertEquals(secondInterval.getEnters(),30);
			    assertEquals(secondInterval.getExits(),18);
	}
	
	
	@Test
	public void getClientTrafficWillReturnZeroIncrementValuesForNonExistantClient() {	
		populateData();
		String pathToTest = String.format("/client/12434300/startTime/%s/endTime/%s",startDate,endDate);
		TrafficRecordSet ret =  
				when()
			        .get(pathToTest)
		         .then()
		        	.statusCode(200)
		         .extract().body().as(TrafficRecordSet.class);
			    
			    TrafficRecordAggregate firstInterval = ret.getTraffic().get(0);
			    TrafficRecordAggregate secondInterval = ret.getTraffic().get(1);
			    
			    assertEquals(firstInterval.getInterval(),expectedFirstIntervalDate);
			    assertEquals(firstInterval.getEnters(),0);
			    assertEquals(firstInterval.getExits(),0);
			    assertEquals(secondInterval.getInterval(),expectedSecondIntervalDate);
			    assertEquals(secondInterval.getEnters(),0);
			    assertEquals(secondInterval.getExits(),0);
	}
	
	
	@Test
	public void getClientTrafficWillReturn400ForBadDate() {	
		String pathToTest = "/client/300/startTime/201603013030/endTime/201603010000";
		when()
			.get(pathToTest)
		.then()
			.statusCode(400);
	}
	
	@Test
	public void getClientTrafficWillReturn400ForStartTimeAfterEndTime() {	
		String pathToTest = String.format("/client/300/startTime/%s/endTime/%s",endDate,startDate);
		when()
			.get(pathToTest)
		.then()
			.statusCode(400);
	}
	
	@Test
	public void getClientTrafficByStoreWillReturnCorrectIncrementValues() {	
		populateData();
		String pathToTest = String.format("/client/300/store/3000/startTime/%s/endTime/%s",startDate,endDate);
		
		TrafficRecordSet ret =  
				when()
			        .get(pathToTest)
		         .then()
		        	.statusCode(200)
		         .extract().body().as(TrafficRecordSet.class);
			    
			    TrafficRecordAggregate firstInterval = ret.getTraffic().get(0);
			    TrafficRecordAggregate secondInterval = ret.getTraffic().get(1);
			    
			    System.out.println(firstInterval);
			    System.out.println(secondInterval);
			    assertEquals((int) ret.getClientId(),300);
			    assertEquals((int) ret.getStoreId(),3000);
			    assertEquals(firstInterval.getInterval(),expectedFirstIntervalDate);
			    assertEquals(firstInterval.getEnters(),8);
			    assertEquals(firstInterval.getExits(),12);
			    assertEquals(secondInterval.getInterval(),expectedSecondIntervalDate);
			    assertEquals(secondInterval.getEnters(),17);
			    assertEquals(secondInterval.getExits(),9);
	}
	
	@Test
	public void getClientTrafficByStoreWillReturnZeroIncrementValuesForNonExistantStore() {	
		populateData();
		String pathToTest = String.format("/client/300/store/2342342/startTime/%s/endTime/%s",startDate,endDate);
		
		TrafficRecordSet ret =  
				when()
			        .get(pathToTest)
		         .then()
		        	.statusCode(200)
		         .extract().body().as(TrafficRecordSet.class);
			    
			    TrafficRecordAggregate firstInterval = ret.getTraffic().get(0);
			    TrafficRecordAggregate secondInterval = ret.getTraffic().get(1);
			    
			    assertEquals(firstInterval.getInterval(),expectedFirstIntervalDate);
			    assertEquals(firstInterval.getEnters(),0);
			    assertEquals(firstInterval.getExits(),0);
			    assertEquals(secondInterval.getInterval(),expectedSecondIntervalDate);
			    assertEquals(secondInterval.getEnters(),0);
			    assertEquals(secondInterval.getExits(),0);
	}
	
	@Test
	public void getClientTrafficByStoreWillReturn400ForBadDate() {
		String pathToTest = String.format("/client/300/store/3000/startTime/%s/endTime/%s",badDate,endDate);
		when()
			.get(pathToTest)
		.then()
			.statusCode(400);
	}
	

	void get(TrafficRecord rec) {       
	    TrafficRecord ret =  
		when()
	        .get(ID, rec.id)
         .then()
        	.statusCode(200)
        	.body("id", is(rec.id))
        	.body("clientId", is(rec.clientId))
        	.body("storeId", is(rec.storeId))
        	.body("enters", is(rec.enters))
        	.body("exits", is(rec.exits))
         .extract().body().as(TrafficRecord.class);
	    assertEquals(rec.min5_dt, ret.min5_dt);
	}

	@SuppressWarnings("unchecked")
	void getAll() {       
		List<TrafficRecord> ret =  
		when()
	        .get()
         .then()
        	.statusCode(200)
         .extract().body().as(List.class);
	    System.out.println(ret);
	    assertTrue(ret.size()>=2);
	}
	
	void post(TrafficRecord rec) {
		given()
		    .contentType("application/json")
	        .body(rec)	       
	     .when()
	        .post()
         .then()
        	.statusCode(200);
	}
	
	void put(TrafficRecord rec) {
		given()
		    .contentType("application/json")
	        .body(rec)	       
	     .when()
	        .post()
         .then()
        	.statusCode(200);
	}
	
	void getNotFound(TrafficRecord rec) {
	     when()
	        .get(ID, rec.id)
         .then()
        	.statusCode(404);
	}
		
	
	void delete(TrafficRecord r) {	       
	    when()
	        .delete(ID, r.id)
        .then()	
        	.statusCode(200);
	}
	
	TrafficRecord generateRecord(int id) {
		TrafficRecord rec = new TrafficRecord();
		rec.setId(id).setClientId(111).setStoreId(222).setEnters(2).setExits(2).setMin5_dt(new Date());
		return rec;
	}
	
	private void populateData() {
		//adding independent data for use in tests. Don't want to make assumption sample data will always be present
		List<TrafficRecord> l = TestHelper.getSampleData();
		for(TrafficRecord tr: l){
			put(tr);
		}
		       
	}
	
}
