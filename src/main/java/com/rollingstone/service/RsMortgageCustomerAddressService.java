package com.rollingstone.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.rollingstone.dao.jpa.RsMortgageCustomerAddressRepository;
import com.rollingstone.domain.Account;
import com.rollingstone.domain.Address;
import com.rollingstone.domain.Customer;


/*
 * Service class to do CRUD for Customer Address through JPS Repository
 */
@Service
public class RsMortgageCustomerAddressService {

    private static final Logger log = LoggerFactory.getLogger(RsMortgageCustomerAddressService.class);

    @Autowired
    private RsMortgageCustomerAddressRepository customerAddressRepository;

    @Autowired
    CounterService counterService;

    @Autowired
    GaugeService gaugeService;
    
    @Autowired
  	private CustomerClient customerClient;

    public RsMortgageCustomerAddressService() {
    }

    @HystrixCommand(fallbackMethod = "createAddressWithoutCustomerValidation")
    public Address createAddress(Address address) throws Exception {
    	Address createdAddress = null;
    	if (address != null && address.getCustomer() != null){
    		
    		log.info("In service account create"+ address.getCustomer().getId());
    		if (customerClient == null){
        		log.info("In customerClient null got customer");
    		}
    		else {
    			log.info("In customerClient not null got customer");
    		}
    		
    		Customer customer = customerClient.getCustomer((new Long(address.getCustomer().getId())));
    		
    		if (customer != null){
    			log.info("Customer Validation Successful. Creating Address with valid customer");

    			createdAddress  = customerAddressRepository.save(address);
    		}else {
    			log.info("Invalid Customer");
    			throw new Exception("Invalid Customer");
    		}
    	}
    	else {
    			throw new Exception("Invalid Customer");
    	}
        return createdAddress;
    }
    
    public Address createAddressWithoutCustomerValidation(Address address) throws Exception {
    	Address createdAddress = null;
		log.info("Customer Validation Failed. Creating Address with validating customer");
    	createdAddress  = customerAddressRepository.save(address);
        return createdAddress;
    }

    public Address getAddress(long id) {
        return customerAddressRepository.findOne(id);
    }

    public void updateAddress(Address address) throws Exception {
    	Address createdAddress = null;
    	if (address != null && address.getCustomer() != null){
    		
    		log.info("In service account create"+ address.getCustomer().getId());
    		if (customerClient == null){
        		log.info("In customerClient null got customer");
    		}
    		else {
    			log.info("In customerClient not null got customer");
    		}
    		
    		Customer customer = customerClient.getCustomer((new Long(address.getCustomer().getId())));
    		
    		if (customer != null){
    			createdAddress  = customerAddressRepository.save(address);
    		}else {
    			log.info("Invalid Customer");
    			throw new Exception("Invalid Customer");
    		}
    	}
    	else {
    			throw new Exception("Invalid Customer");
    	}
    }

    public void deleteAddress(Long id) {
    	customerAddressRepository.delete(id);
    }

    public Page<Address> getAllAddresssByPage(Integer page, Integer size) {
        Page pageOfAddresss = customerAddressRepository.findAll(new PageRequest(page, size));
        
        // example of adding to the /metrics
        if (size > 50) {
            counterService.increment("com.rollingstone.getAll.largePayload");
        }
        return pageOfAddresss;
    }
    
    public List<Address> getAllAddresss() {
        Iterable<Address> pageOfAddresss = customerAddressRepository.findAll();
        
        List<Address> customerAddresss = new ArrayList<Address>();
        
        for (Address address : pageOfAddresss){
        	customerAddresss.add(address);
        }
    	log.info("In Real Service getAllAddresss  size :"+customerAddresss.size());

    	
        return customerAddresss;
    }
    
    public List<Address> getAllAddresssForCustomer(Customer customer) {
        Iterable<Address> pageOfAddresss = customerAddressRepository.findCustomerAddressesByCustomer(customer);
        
        List<Address> customerAddresss = new ArrayList<Address>();
        
        for (Address address : pageOfAddresss){
        	customerAddresss.add(address);
        }
    	log.info("In Real Service getAllAddresss  size :"+customerAddresss.size());

    	
        return customerAddresss;
    }
}
