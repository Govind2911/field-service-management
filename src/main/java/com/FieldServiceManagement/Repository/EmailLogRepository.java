package com.FieldServiceManagement.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.FieldServiceManagement.Entity.EmailLog;

@Repository
public interface EmailLogRepository extends JpaRepository<EmailLog,Long>{
	
	

}