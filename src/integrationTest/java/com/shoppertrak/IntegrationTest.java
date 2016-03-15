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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebIntegrationTest(randomPort = true)
public class IntegrationTest {

	private static final String ID = "{id}";
	
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
	
}
