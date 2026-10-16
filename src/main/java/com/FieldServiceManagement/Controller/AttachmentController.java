package com.FieldServiceManagement.Controller;

import java.io.InputStream;
import java.net.URL;
import org.springframework.core.io.InputStreamResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.FieldServiceManagement.Entity.Attachment;
import com.FieldServiceManagement.Service.AttachmentService;


@RestController
@RequestMapping("/attachment")
public class AttachmentController {
   @Autowired
   private AttachmentService attachmentService;
   
   @PostMapping("/upload")
   public ResponseEntity<Attachment>upload(@RequestParam MultipartFile file , @RequestParam String folder){
	   Attachment attach = attachmentService.upload(file,  folder);
	   return ResponseEntity.ok(attach);
   }
   
   @GetMapping("/{id}")
   public ResponseEntity<Attachment>getById(@PathVariable Long id){
	   return ResponseEntity.ok(attachmentService.getById(id));
   }
   
   @GetMapping("/cloudDownload/{id}")
   public ResponseEntity<InputStreamResource> download(
           @PathVariable Long id) throws Exception {

       Attachment attachment = attachmentService.getById(id);

       URL url = new URL(attachment.getStoragePath());
       InputStream inputStream = url.openStream();

       return ResponseEntity.ok()
               .contentType(MediaType.APPLICATION_OCTET_STREAM)
               .header(
                       HttpHeaders.CONTENT_DISPOSITION,
                       "attachment; filename=\"" + attachment.getFilename() + "\""
               )
               .body(new InputStreamResource(inputStream));
   }
   
   @GetMapping("/stream/{id}")
   public ResponseEntity<InputStreamResource> streamFile(
           @PathVariable Long id) throws Exception {

       Attachment attachment = attachmentService.getById(id);

       URL url = new URL(attachment.getStoragePath());
       InputStream inputStream = url.openStream();

       return ResponseEntity.ok()
               .contentType(MediaType.parseMediaType(
                       attachment.getContentType()
               ))
               .header(
                       HttpHeaders.CONTENT_DISPOSITION,
                       "inline; filename=\"" + attachment.getFilename() + "\""
               )
               .body(new InputStreamResource(inputStream));
   }
  
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String>deleteFile(@PathVariable Long id){
		attachmentService.delete(id);
		return ResponseEntity.ok("file deleted successfully");
	}

}













