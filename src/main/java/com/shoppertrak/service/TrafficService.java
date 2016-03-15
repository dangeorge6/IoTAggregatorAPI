package com.shoppertrak.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shoppertrak.dao.TrafficDao;
import com.shoppertrak.domain.TrafficRecord;

@Service
public class TrafficService {
	
	TrafficDao dao;
	 
	@Autowired
	public TrafficService(TrafficDao dao) {
		this.dao = dao;
	}
	
	public TrafficRecord get(int id) {
		return dao.get(id);
	}

	public void save(TrafficRecord r) {
		dao.save(r);
	}

	public Collection<TrafficRecord> getAll() {
		return dao.getAll();
	}

	public void delete(int id) {
		dao.delete(id);
	}
}
