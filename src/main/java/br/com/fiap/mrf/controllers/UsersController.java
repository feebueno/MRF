package br.com.fiap.mrf.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.mrf.exception.RestNotFoundException;
import br.com.fiap.mrf.models.Users;
import br.com.fiap.mrf.repository.UsersRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/user")
@Slf4j
public class UsersController {

    @Autowired
    UsersRepository repository; //IoD

    @GetMapping
    public List<Users> index(){
        return repository.findAll();
    }

    @PostMapping
    public ResponseEntity<Users> create(
            @RequestBody @Valid Users user, 
            BindingResult result
        ){
        log.info("cadastrando conta: " + user);
        repository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("{id}")
    public ResponseEntity<Users> show(@PathVariable Long id){
        log.info("buscando conta: " + id);
        return ResponseEntity.ok(getUsers(id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Users> destroy(@PathVariable Long id){
        log.info("apagando conta: " + id);
        repository.delete(getUsers(id));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<Users> update(
        @PathVariable Long id, 
        @RequestBody @Valid Users user
    ){
        log.info("atualizando conta: " + id);
        getUsers(id);
        user.setId(id);
        repository.save(user);
        return ResponseEntity.ok(user);
    }

    private Users getUsers(Long id) {
        return repository.findById(id).orElseThrow(
            () -> new RestNotFoundException("conta não encontrada"));
    }
    
}
