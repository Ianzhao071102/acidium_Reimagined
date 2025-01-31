package org.izdevs.acidium.networking;

import com.google.gson.Gson;
import com.google.re2j.Matcher;
import com.google.re2j.Pattern;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.constraintvalidators.hv.UUIDValidator;
import org.izdevs.acidium.Metrics;
import org.izdevs.acidium.api.v1.Payload;
import org.izdevs.acidium.api.v1.Player;
import org.izdevs.acidium.api.v1.User;
import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.basic.InventoryRepository;
import org.izdevs.acidium.basic.UserRepository;
import org.izdevs.acidium.game.equipment.Equipment;
import org.izdevs.acidium.game.inventory.PlayerInventory;
import org.izdevs.acidium.networking.account.JoinedPlayer;
import org.izdevs.acidium.networking.account.OnlinePlayerRepository;
import org.izdevs.acidium.networking.game.payload.PlayerReference;
import org.izdevs.acidium.scheduling.DelayedTask;
import org.izdevs.acidium.scheduling.LoopManager;
import org.izdevs.acidium.security.AuthorizationContent;
import org.izdevs.acidium.security.RegistrationPayload;
import org.izdevs.acidium.security.SessionDetail;
import org.izdevs.acidium.security.SessionGenerator;
import org.izdevs.acidium.utils.NumberUtils;
import org.izdevs.acidium.utils.SpringBeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.util.*;

import static org.izdevs.acidium.AcidiumApplication.bcrypt;

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
            if(userRepository.findByUsername(pld.getUsername()) != null) {
                log.debug("username taken:{}", pld.getUsername());
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            log.debug("registration successful");
            String password_hash = new BCryptPasswordEncoder().encode(pld.getPassword());
            userRepository.save(new User(pld.getUsername(), password_hash));
            return new ResponseEntity<>(new User(pld.getUsername(), password_hash), HttpStatus.OK);
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