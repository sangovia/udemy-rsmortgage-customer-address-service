package com.rollingstone.dao.jpa;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.rollingstone.domain.Address;
import com.rollingstone.domain.Customer;



public interface RsMortgageCustomerAddressRepository extends PagingAndSortingRepository<Address, Long> {
    List<Address> findCustomerAddressesByCustomer(Customer customer);

    Page findAll(Pageable pageable);
}
