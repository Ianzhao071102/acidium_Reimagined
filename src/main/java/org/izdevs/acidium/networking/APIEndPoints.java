package org.izdevs.acidium.networking;

import com.google.gson.Gson;
import com.google.re2j.Matcher;
import com.google.re2j.Pattern;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.websocket.Session;
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

    @PostMapping(path = "register",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> register(@RequestBody RegistrationPayload pld){
        if(User.password_regex.matches(pld.getPassword()) && User.username_regex.matches(pld.getUsername())){
            String password_hash = new BCryptPasswordEncoder().encode(pld.getPassword());
            UUID uuid = UUID.randomUUID();
            repository.save(new JoinedPlayer(pld.getUsername(), password_hash,uuid.toString()));
            return new ResponseEntity<>(new User(pld.getUsername(), password_hash,uuid),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(new User(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "inventory")
    public ResponseEntity<PlayerInventory> getInventoryOfPlayer(@RequestBody PlayerReference ref){
        if(ref.username().contains(" ") || !User.username_regex.matcher(ref.username()).matches()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = userRepository.findByUsername(ref.username());
        if(user != null){
            Optional<PlayerInventory> a = inventoryRepository.findById(user);
            if(a.isPresent()){
                return new ResponseEntity<>(a.get(),HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}