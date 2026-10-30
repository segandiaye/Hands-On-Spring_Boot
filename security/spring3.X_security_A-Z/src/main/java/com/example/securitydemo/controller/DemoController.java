package com.example.securitydemo.controller;
import com.example.securitydemo.service.ManagerService;
import com.example.securitydemo.service.AdminService;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class DemoController {

    private final ManagerService managerService;
    private final AdminService adminService;

    public DemoController(AdminService adminService, ManagerService managerService) {
	this.adminService = adminService;
	this.managerService = managerService;
    }

    @GetMapping("/public")
	public String publicEndpoint() {
	    return "Bienvenue, visiteur !";
	}

    @GetMapping("/user")
	public String userEndpoint() {
	    return "Bienvenue, utilisateur !";
	}

    @GetMapping("/manager/report")
	public String getManagerReport() {
	    return managerService.getManagerReport();
	}

    @GetMapping("/admin")
	public String adminEndpoint() {
	    System.out.println("Je dans le controller admin");
	    return adminService.getAdminData();
	}


    @GetMapping("/admin/data")
	public String getAdminData() {
	    return adminService.getAdminData();
	}

    @GetMapping("/me")
	public ResponseEntity<?> me(Authentication authentication) {
	    return ResponseEntity.ok(
		    Map.of(
			"username", authentication.getName(),
			"roles", authentication.getAuthorities()
			.stream()
			.map(authority -> authority.getAuthority())
			.toList()
			)
		    );
	}
}
