package com.paris13.pls.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.paris13.pls.entities.User;
import com.paris13.pls.exception.ResourceNotFoundException;
import com.paris13.pls.repository.UserRepository;

import javax.validation.Valid;
import java.util.List;

/**
 * Créée par NDIAYE Sega 2019.
 */
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/users")
    public List<User> getAllNotes() {
        return userRepository.findAll();
    }

    @PostMapping("/users")
    public User createNote(@Valid @RequestBody User user) {
        return userRepository.save(user);
    }

    @GetMapping("/users/{id}")
    public User getNoteById(@PathVariable(value = "id") Long noteId) {
        return userRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", noteId));
    }

    @PutMapping("/users/{id}")
    public User updateNote(@PathVariable(value = "id") Long noteId,
                                           @Valid @RequestBody User noteDetails) {

        User user = userRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", noteId));

        user.setNom(noteDetails.getNom());
        user.setPrenom(noteDetails.getPrenom());
        user.setMail(noteDetails.getMail());
        user.setTel(noteDetails.getTel());
        user.setType(noteDetails.getType());
        user.setAdr_pro(noteDetails.getAdr_pro());
        user.setAdr_dom(noteDetails.getAdr_dom());

        User updatedNote = userRepository.save(user);
        return updatedNote;
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable(value = "id") Long noteId) {
        User user = userRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", noteId));

        userRepository.delete(user);

        return ResponseEntity.ok().build();
    }
}
