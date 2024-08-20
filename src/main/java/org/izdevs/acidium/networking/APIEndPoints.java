package org.izdevs.acidium.networking;

import com.google.gson.Gson;
import com.google.re2j.Matcher;
import com.google.re2j.Pattern;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.websocket.Session;
import org.izdevs.acidium.Metrics;
import org.izdevs.acidium.api.v1.Payload;
import org.izdevs.acidium.api.v1.Player;
import org.izdevs.acidium.api.v1.User;
import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.basic.UserRepository;
import org.izdevs.acidium.game.equipment.Equipment;
import org.izdevs.acidium.game.inventory.PlayerInventory;
import org.izdevs.acidium.scheduling.DelayedTask;
import org.izdevs.acidium.scheduling.LoopManager;
import org.izdevs.acidium.security.AuthorizationContent;
import org.izdevs.acidium.security.SessionDetail;
import org.izdevs.acidium.security.SessionGenerator;
import org.izdevs.acidium.utils.NumberUtils;
import org.izdevs.acidium.utils.SpringBeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static org.izdevs.acidium.AcidiumApplication.bcrypt;

@RestController
@RequestMapping("v1") //RESOURCE GETTER API V1
public class APIEndPoints {
    @Autowired
    ApplicationEventPublisher publisher;
    @Autowired
    SessionGenerator sessionGenerator;
    public static int playersOnline = 0;
    @Autowired
    public UserRepository repository;
    @Autowired
    LoopManager manager;
    @Autowired
    @Qualifier(value = "credits")
    private String credits;

    @GetMapping(path = "credits")
    public ResponseEntity<Payload> credits() {
        Metrics.apiRequests.increment();
        return new ResponseEntity<>(new Payload(credits), HttpStatus.OK);
    }
}