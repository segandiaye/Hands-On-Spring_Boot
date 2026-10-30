package com.example.securitydemo.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class ManagerService {

   @PreAuthorize("hasRole('MANAGER')")
   public String getManagerReport() {
	return "Rapport des managers";
   }

}
