package com.FieldServiceManagement.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.FieldServiceManagement.Entity.Customer;
import com.FieldServiceManagement.Entity.Site;
import com.FieldServiceManagement.Repository.CustomerRepository;
import com.FieldServiceManagement.Repository.SiteRepository;

@Service
public class SiteServiceImpl implements SiteService {

	@Autowired
	private SiteRepository siteRepo;
	
	@Autowired
	private CustomerRepository customerRepo;
	
	
	@Override
	public Site createSite(Site site) {
	Customer customer = customerRepo.findById(site.getCustomer().getId())
			.orElseThrow(()-> new RuntimeException("Customer not found"));
	      site.setCustomer(customer);
		return siteRepo.save(site);
	}

	@Override
	public Site updateSite(Long id, Site siteDetails) {
		Site existingSite = siteRepo.findById(id).orElseThrow(()-> new RuntimeException("Site not found"));
	 
		
		existingSite.setSiteName(siteDetails.getSiteName());
		existingSite.setAppartmentName(siteDetails.getAppartmentName());
		existingSite.setFloorNo(siteDetails.getFloorNo());
		existingSite.setAddressDetails(siteDetails.getAddressDetails());
		existingSite.setCity(siteDetails.getCity());
		existingSite.setState(siteDetails.getState());
		existingSite.setCountry(siteDetails.getCountry());
		existingSite.setZipCode(siteDetails.getZipCode());
		
		if(siteDetails.getCustomer()!=null && siteDetails.getCustomer().getId()!=null) {
			Customer customer = customerRepo.findById(siteDetails.getCustomer().getId()).orElseThrow(()->new RuntimeException("Customer not found"));
		  existingSite.setCustomer(customer);
		}
		
		return siteRepo.save(existingSite);
		
	}

	@Override
	public Site getSite(Long id) {
		
		return null;
	}

	@Override
	public List<Site> getSiteByCustomer(Long customerId) {
		
		return null;
	}

	@Override
	public void deleteSite(Long id) {
		
		
	}

}
