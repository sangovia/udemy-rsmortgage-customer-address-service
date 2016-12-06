package com.rollingstone.api.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


import com.rollingstone.domain.Customer;
import com.rollingstone.domain.Address;
import com.rollingstone.exception.HTTP400Exception;
import com.rollingstone.service.RsMortgageCustomerAddressService;
/*
 * Demonstrates how to set up RESTful API endpoints using Spring MVC
 */

@RestController
@RequestMapping(value = "/rsmortgage-customer-address-service/v1/customer-address")
public class CustomerAddressController extends AbstractRestController {

    @Autowired
    private RsMortgageCustomerAddressService customerAddressService;
  
    @RequestMapping(value = "",
            method = RequestMethod.POST,
            consumes = {"application/json", "application/xml"},
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.CREATED)
    public void createCustomerAddress(@RequestBody Address address,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Address createdAddress = this.customerAddressService.createAddress(address);
        response.setHeader("Location", request.getRequestURL().append("/").append(createdAddress.getId()).toString());
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    public
    @ResponseBody
    Page<Address> getAllCustomersAddressByPage(@RequestParam(value = "page", required = true, defaultValue = DEFAULT_PAGE_NUM) Integer page,
                                      @RequestParam(value = "size", required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer size,
                                      HttpServletRequest request, HttpServletResponse response) {
        return this.customerAddressService.getAllAddresssByPage(page, size);
    }
    
    @RequestMapping(value = "/all",
            method = RequestMethod.GET,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    public
    @ResponseBody
    List<Address> getAllCustomerAddresss(@RequestParam(value = "page", required = true, defaultValue = DEFAULT_PAGE_NUM) Integer page,
                                      @RequestParam(value = "size", required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer size,
                                      HttpServletRequest request, HttpServletResponse response) {
        return this.customerAddressService.getAllAddresss();
    }
    
    @RequestMapping(value = "/all/{customerId}",
            method = RequestMethod.GET,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    public
    @ResponseBody
    List<Address> getAllCustomerAddresssForSingleCustomer(@RequestParam(value = "page", required = true, defaultValue = DEFAULT_PAGE_NUM) Integer page,
                                      @RequestParam(value = "size", required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer size,
                                      @PathVariable("id") Long id,
                                      HttpServletRequest request, HttpServletResponse response) {
        return this.customerAddressService.getAllAddresssForCustomer(new Customer());
    }

    
    @RequestMapping("/simple/{id}")
	public Address getSimpleCustomerAddress(@PathVariable("id") Long id) {
    	Address address = this.customerAddressService.getAddress(id);
         checkResourceFound(address);
         return address;
	}

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    public
    @ResponseBody
    Address getAddress(@PathVariable("id") Long id,
                             HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Address address = this.customerAddressService.getAddress(id);
        checkResourceFound(address);
        return address;
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.PUT,
            consumes = {"application/json", "application/xml"},
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCustomerAddress(@PathVariable("id") Long id, @RequestBody Address address,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        checkResourceFound(this.customerAddressService.getAddress(id));
        if (id != address.getId()) throw new HTTP400Exception("ID doesn't match!");
        this.customerAddressService.updateAddress(address);
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomerAddress(@PathVariable("id") Long id, HttpServletRequest request,
                                 HttpServletResponse response) {
        checkResourceFound(this.customerAddressService.getAddress(id));
        this.customerAddressService.deleteAddress(id);
    }
}
