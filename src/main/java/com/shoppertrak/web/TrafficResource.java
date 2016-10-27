package com.shoppertrak.web;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.shoppertrak.domain.DateRange;
import com.shoppertrak.domain.TrafficRecord;
import com.shoppertrak.domain.TrafficRecordSet;
import com.shoppertrak.exception.RecordNotFound;
import com.shoppertrak.service.TrafficService;


@RestController
@RequestMapping("api/v1.0/traffic")
public class TrafficResource {
	
	
	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	TrafficService service;

	@ApiOperation(value = "Get Traffic Record")
	@ApiResponses(value = {@ApiResponse(code = 404, message = "Record is not found") })
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public @ResponseBody TrafficRecord get(@ApiParam(value = "Record Id", required = true) @PathVariable int id) {

		TrafficRecord  rec = service.get(id);
		if (rec == null) {
			logger.info("Record is not found for macAddress:" + id);
			throw new RecordNotFound(id);
		}
		return rec;
	}

	@ApiOperation(value = "Delete Traffic Record")
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	public void delete(@ApiParam(value = "Record Id", required = true) @PathVariable int id) {
		service.delete(id);
	}
	
	@ApiOperation(value = "Get All Traffic Records")
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody Collection<TrafficRecord> getAllRecords() {
		return service.getAll();
	}
	
	@ApiOperation(value = "Save Traffic Records")
	@RequestMapping(method = RequestMethod.POST)
	public void save(@RequestBody TrafficRecord record) {
		service.save(record);
	}
	
	
	/**
     * GET  /getClientTraffic : get all client traffic for all stores combined in 15 minute intervals.
     *
     * @return the ResponseEntity with status 200 (OK) and the client traffic in body
     */
	@ApiOperation(value = "get all client traffic for all stores combined in 15 min intervals")
    @RequestMapping(value = "/client/{clientId}/startTime/{startTime}/endTime/{endTime}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
  
    public ResponseEntity getClientTraffic(
    		@ApiParam(value = "Client Id", required = true) @PathVariable("clientId") int clientId, 
    		@ApiParam(value = "Start Time", required = true) @PathVariable("startTime") String startTime, 
    		@ApiParam(value = "End Time", required = true) @PathVariable("endTime") String endTime ) {
		
			logger.info("in getClientTraffic");
			logger.info("start time is " + startTime);
	        //use custom DateRange class to determine if the dates are valid and chronological
	        DateRange dr = new DateRange(startTime,endTime);
	        if(dr.hasError()){
	        	return new ResponseEntity<>(dr.getErrorMsg(),HttpStatus.BAD_REQUEST);
	        }
	        
	        TrafficRecordSet clientTraffic = service.getTrafficByClient(clientId,dr);
	        
	        if(clientTraffic.hasError()){
	        	//return a 404 when client doesn't exist
	        	//I think it's a bit more robust than just returning intervals with no data
	        	return new ResponseEntity<>(clientTraffic.getErrorMsg(),HttpStatus.BAD_REQUEST);
	        }
	        
	        return Optional.ofNullable(clientTraffic)
	                .map(result -> new ResponseEntity<>(
	                    result,
	                    HttpStatus.OK))
	                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
   

    /**
     * GET  /getClientTrafficByStore : get all client traffic for specific store in 15 minute intervals.
     *
     * @return the ResponseEntity with status 200 (OK) and the client traffic in body
     */
	
	@ApiOperation(value = "get all client traffic for specific store in 15 minute intervals.")
    @RequestMapping(value = "/client/{clientId}/store/{storeId}/startTime/{startTime}/endTime/{endTime}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    
    public ResponseEntity getClientTrafficByStore(
    		@ApiParam(value = "Client Id", required = true) @PathVariable("clientId") int clientId, 
    		@ApiParam(value = "Store Id", required = true) @PathVariable("storeId") int storeId, 
    		@ApiParam(value = "Start Time", required = true) @PathVariable("startTime") String startTime, 
    		@ApiParam(value = "End Time", required = true) @PathVariable("endTime") String endTime ){
	        
			logger.info("in getClientTrafficByStore");
	        //use custom DateRange class to determine if the dates are valid and chronological
	        DateRange dr = new DateRange(startTime,endTime);
	        if(dr.hasError()){
	        	return new ResponseEntity<>(dr.getErrorMsg(),HttpStatus.BAD_REQUEST);
	        }
	        
	        TrafficRecordSet clientTraffic = service.getTrafficByClientForStore(clientId,storeId,dr);
	        
	        if(clientTraffic.hasError()){
	        	//return a 404 when client doesn't exist or store doesn't exist
	        	//I think it's a bit more robust than just returning intervals with no data
	        	return new ResponseEntity<>(clientTraffic.getErrorMsg(),HttpStatus.BAD_REQUEST);
	        }
	        
	        return Optional.ofNullable(clientTraffic)
	                .map(result -> new ResponseEntity<>(
	                    result,
	                    HttpStatus.OK))
	                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
