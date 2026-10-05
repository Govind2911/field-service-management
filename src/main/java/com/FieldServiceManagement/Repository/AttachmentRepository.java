package com.FieldServiceManagement.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.FieldServiceManagement.Entity.Attachment;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment ,Long> {
	Optional<Attachment>findById(Long id);
}
