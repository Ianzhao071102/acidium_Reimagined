package org.izdevs.acidium.networking;

import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.izdevs.acidium.Metrics;
import org.izdevs.acidium.api.v1.Payload;
import org.izdevs.acidium.api.v1.User;
import org.izdevs.acidium.basic.InventoryRepository;
import org.izdevs.acidium.basic.UserRepository;
import org.izdevs.acidium.game.inventory.PlayerInventory;
import org.izdevs.acidium.networking.account.OnlinePlayerRepository;
import org.izdevs.acidium.security.RegistrationPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("v1") //RESOURCE GETTER API V1
@Slf4j
public class APIEndPoints {
    @Autowired
    @Qualifier(value = "credits")
    private String credits;

    @Autowired
    Metrics metrics;
    @Autowired
    OnlinePlayerRepository repository;

    @Autowired
    InventoryRepository inventoryRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping(path = "credits")
    public ResponseEntity<Payload> credits() {
        metrics.apiRequests.increment();
        return new ResponseEntity<>(new Payload(credits), HttpStatus.OK);
    }

    @PostMapping(path = "register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> register(@RequestBody RegistrationPayload pld) {
        if (User.password_regex.matches(pld.getPassword()) && User.username_regex.matches(pld.getUsername())) {
            if (userRepository.findByUsername(pld.getUsername()) != null) {
                log.debug("username taken:{}", pld.getUsername());
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            log.debug("registration successful");

            String password_hash = new BCryptPasswordEncoder().encode(pld.getPassword());
            User user = new User(pld.getUsername(), password_hash);

            inventoryRepository.save(new PlayerInventory(user));
            userRepository.save(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "inventory")
    public ResponseEntity<PlayerInventory> getInventoryOfPlayer(@PathParam("username") String username) {
        if (username.contains(" ") || !User.username_regex.matcher(username).matches()) {
            log.debug("username regex match failed");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = userRepository.findByUsername(username);
        if (user != null) {
            log.debug("user found");
            Optional<PlayerInventory> a = Optional.ofNullable(inventoryRepository.findInventoryByOwner(user));
            if (a.isPresent()) {
                log.debug("inventory found");
                return new ResponseEntity<>(a.get(), HttpStatus.OK);
            }
        }
        log.debug("user is not found");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}