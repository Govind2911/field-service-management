package com.FieldServiceManagement.Service;

import java.util.List;

import com.FieldServiceManagement.Entity.Site;

public interface SiteService {
     public Site createSite(Site site);
     public Site updateSite(Long id, Site siteDetails);
     public Site getSite(Long id);
     public List<Site>getSiteByCustomer(Long customerId);
     public void deleteSite(Long id);
}
