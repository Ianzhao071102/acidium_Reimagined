package org.izdevs.acidium.networking;

import com.google.gson.Gson;
import com.google.re2j.Matcher;
import com.google.re2j.Pattern;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.izdevs.acidium.Metrics;
import org.izdevs.acidium.api.v1.Error;
import org.izdevs.acidium.api.v1.Payload;
import org.izdevs.acidium.api.v1.Player;
import org.izdevs.acidium.api.v1.User;
import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.basic.UserRepository;
import org.izdevs.acidium.game.equipment.Equipment;
import org.izdevs.acidium.game.inventory.Inventory;
import org.izdevs.acidium.game.inventory.InventoryType;
import org.izdevs.acidium.scheduling.DelayedTask;
import org.izdevs.acidium.scheduling.LoopManager;
import org.izdevs.acidium.security.AuthorizationContent;
import org.izdevs.acidium.security.SessionDetail;
import org.izdevs.acidium.security.SessionGenerator;
import org.izdevs.acidium.serialization.API;
import org.izdevs.acidium.serialization.Resource;
import org.izdevs.acidium.serialization.ResourceFacade;

import org.izdevs.acidium.utils.SpringBeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static org.izdevs.acidium.AcidiumApplication.bcrypt;

@RestController
@RequestMapping("v1") //RESOURCE GETTER API V1
public class APIEndPoints {
    public static int playersOnline = 0;

    @Autowired
    public UserRepository repository;


    @Autowired
    @Qualifier(value = "credits")
    private String credits;

    @GetMapping(path = "resources/{name}/{api}")
    public String getResource(@PathVariable(name = "name") String name, @PathVariable(name = "api", required = false) String api) {
        Metrics.apiRequests.increment();
        for (int i = 0; i <= ResourceFacade.getResources().size() - 1; i++) {
            Resource resource = ResourceFacade.getResources().get(i);
            if ((name.isEmpty() || name.isBlank()) && (api.isBlank() || api.isEmpty())) {
                Gson gson = new Gson();
                return gson.toJson(ResourceFacade.getResources());
            }
            if (resource.getName().equals(name)) {
                if (api.isEmpty() || api.isBlank()) {
                    //no api pass directly
                    Gson gson = new Gson();
                    return gson.toJson(resource);
                } else {
                    //api specified check if satisfied
                    API destl = null;
                    for (int j = i; j <= ResourceFacade.getResources().size() - i; j++) {
                        if (ResourceFacade.getResources().get(i).isApi() && ResourceFacade.getResources().get(i).getName().equals(api)) {
                            destl = (API) ResourceFacade.getResources().get(i);
                            break;
                        }
                    }
                    if (destl == null) {
                        Gson gson = new Gson();
                        return gson.toJson(new Throwable("The API is NOT FOUND"));
                    } else {
                        //pass the resource
                        if (resource.associatedApi.equals(destl)) {
                            Gson gson = new Gson();
                            return gson.toJson(resource);
                        }
                    }
                }
            }
        }
        Gson gson = new Gson();
        return gson.toJson(new Error(new Throwable("Internal Server Caused an Exception")));
    }

    @GetMapping(path = "/login/{username}/{password}")
    public ResponseEntity<Payload> login(@PathVariable(name = "username") String username, @PathVariable(name = "password") String password, HttpServletResponse response, HttpServletRequest request) {
        Metrics.apiRequests.increment();
        Pattern username_pattern = Pattern.compile("^[a-zA-Z0-9](?:[._]?[a-zA-Z0-9]){5,17}[a-zA-Z0-9]$"); //NOTE: USERNAME LENGTH MUST BE 5-20, ONLY CHARACTERS AND NUMBERS
        Matcher username_match = username_pattern.matcher(username);

        Pattern pwd_pattern = Pattern.compile("^[a-zA-Z0-9](?:[._]?[a-zA-Z0-9]){6,30}[a-zA-Z0-9]$"); //NOTE: PASSWORD LENGTH MUST BE 8-80, ONLY CHARACTERS AND NUMBERS
        Matcher pwd_match = pwd_pattern.matcher(password);

        if (username_match.matches() && pwd_match.matches()) {
            //query is safe

            User user = repository.findUserByUsername(username);
            if(user == null){
                return new ResponseEntity<>(new Payload("user is not found"),HttpStatus.UNPROCESSABLE_ENTITY);
            }
            else if(!Objects.equals(user.getPasswordHash(), bcrypt(password))){
                return new ResponseEntity<>(new Payload("incorrect password or username"),HttpStatus.UNAUTHORIZED);
            }
            response.addCookie(new Cookie("user", user.toString()));

            UUID sid = SessionGenerator.use();
            UUID playerUUID = user.getUuid();
            //initial value of player
            request.getSession().setAttribute("player", new Player(user, new Entity(username, 1, 20, 0, 20)));
            request.getSession().setAttribute("session-id", sid);
            return new ResponseEntity<>(
                    new Payload(
                            new Gson().toJson(
                                    new AuthorizationContent(
                                            playerUUID.toString(),
                                            new SessionDetail(String.valueOf(sid))
                                    )
                            )), HttpStatusCode.valueOf(200)
            );

        } else {
            return new ResponseEntity<>(new Payload("illegal parameter"), HttpStatusCode.valueOf(403));
        }
    }

    @PostMapping(path = "/operations/move/{degree}", params = "degree")
    public ResponseEntity<Payload> move(@PathVariable(name = "degree") double degree, HttpServletRequest request) {
        Metrics.apiRequests.increment();
        if (request.getSession().getAttribute("session-id") == null) {
            //fuck no shit
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        }

        if (!(request.getSession().getAttribute("session-id") instanceof UUID uuid)) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(911));
        }
        Matcher matcher = Pattern.compile("^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$").matcher(uuid.toString());

        if (matcher.matches()) {
            //uuid follows the uuid schema
            if (SessionGenerator.validate(uuid)) {
                //session is valid
                if (request.getSession().getAttribute("player") == null) {
                    //if attribute is not present
                    return new ResponseEntity<>(HttpStatusCode.valueOf(911));
                } else {
                    //session is preset
                    if (request.getSession().getAttribute("player") instanceof Player player) {
                        double add_x = player.getMovementSpeed() * Math.cos(degree);
                        double add_y = player.getMovementSpeed() * Math.sin(degree);

                        //schedules task to execute after 1 tick
                        DelayedTask task = new DelayedTask(() -> {
                            player.setX(player.getX() + add_x);
                            player.setY(player.getY() + add_y);
                        }, 1, true);

                        Object _mgr = SpringBeanUtils.getBean("loopManager");
                        assert _mgr instanceof LoopManager;

                        LoopManager manager = (LoopManager) _mgr;

                        manager.scheduleAsyncDelayedTask(task);
                        return new ResponseEntity<>(HttpStatus.OK);
                    } else {
                        //invalid session
                        return new ResponseEntity<>(new Payload("invalid session"), HttpStatus.FORBIDDEN);
                    }
                }
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }


    @PostMapping(path = "/operations/update")
    public ResponseEntity<Payload> update(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Metrics.apiRequests.increment();
        if (!(request.getSession().getAttribute("session-id") instanceof UUID session) || !(request.getSession().getAttribute("player") instanceof Player)) {
            //handle invalid session
            request.getSession().setAttribute("session-id", SessionGenerator.use().toString());
            response.getWriter().println("invalid session, new session is set");
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } else {
            if (SessionGenerator.validate(session)) {
                Gson gson = new Gson();
                Map<String, String> entityMap = new HashMap<>();

                //the data required to be updated
                entityMap.put("session-id", session.toString());
                entityMap.put("timestamp", Instant.now().toString());

                Payload response_payload = new Payload(gson.toJson(entityMap));
                return new ResponseEntity<>(response_payload, HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }
    }

    @PostMapping(consumes = "application/json", path = "/operations/inventory/move")
    public ResponseEntity<Payload> SwapInventory(HttpServletRequest request) {
        Metrics.apiRequests.increment();
        if (request.getSession().getAttribute("session-id") != null && request.getSession().getAttribute("player") != null) {
            String _op_slot = request.getParameter("op_slot");
            String _dest_slot = request.getParameter("dest_slot");
            String _inv_type = request.getParameter("op_inv_type");
            String _dest_inv_type = request.getParameter("dest_inv_type");
            //session id
            if (request.getSession().getAttribute("uuid") instanceof UUID session_id && request.getSession().getAttribute("player") instanceof Player player) {
                if (!SessionGenerator.validate(session_id)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                else if (_op_slot != null && _dest_slot != null && _inv_type != null) {
                    try {
                        int op_slot = Integer.parseInt(_op_slot);
                        int dest_slot = Integer.parseInt(_dest_slot);

                        InventoryType op_inv_type = null;
                        InventoryType dest_inv_type = null;
                        for (InventoryType type : InventoryType.values()) {
                            if (type.toString().equalsIgnoreCase(_inv_type)) {
                                op_inv_type = type;
                            } else if (type.toString().equalsIgnoreCase(_dest_inv_type)) {
                                dest_inv_type = type;
                            }
                        }


                        if (op_inv_type == null && dest_inv_type == null) {
                            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
                        } else {
                            assert op_inv_type != null && dest_inv_type != null;
                            AtomicReference<Inventory> op_inv = player.getInventory().getInventoryReference(op_inv_type);
                            AtomicReference<Inventory> dest_inv = player.getInventory().getInventoryReference(dest_inv_type);

                            Inventory new_op_inv = op_inv.get();
                            Inventory new_dest_inv = dest_inv.get();
                            Equipment op_eq = new_op_inv.getItems().get(op_slot);
                            Equipment dest_eq = new_dest_inv.getItems().get(dest_slot);

                            Equipment third;

                            //swap them
                            third = op_eq;
                            op_eq = dest_eq;
                            dest_eq = third;

                            new_op_inv.items.set(op_slot,op_eq);
                            new_dest_inv.items.set(dest_slot,dest_eq);

                            //commit the changes
                            op_inv.set(new_op_inv);
                            dest_inv.set(new_dest_inv);
                        }
                    } catch (Exception e) {
                        return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
                    }
                    return new ResponseEntity<>(HttpStatus.ACCEPTED);

                } else return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            } else {
                return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping(path = "/tests/echo")
    public ResponseEntity<Payload> echo() {
        Metrics.apiRequests.increment();
        return new ResponseEntity<>(new Payload("success"), HttpStatus.OK);
    }


    @GetMapping(path = "credits")
    public ResponseEntity<Payload> credits() {
        Metrics.apiRequests.increment();
        return new ResponseEntity<>(new Payload(credits), HttpStatus.OK);
    }

    @GetMapping(path = "hello")
    public ResponseEntity<Payload> hello() {
        Metrics.apiRequests.increment();
        return new ResponseEntity<>(new Payload("hello from: " + credits), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Payload> register(HttpServletRequest request) {
        if (request.getParameter("username") != null && request.getParameter("password") != null) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            Pattern username_pattern = Pattern.compile("^[a-zA-Z0-9](?:[._]?[a-zA-Z0-9]){5,17}[a-zA-Z0-9]$"); //NOTE: USERNAME LENGTH MUST BE 5-20, ONLY CHARACTERS AND NUMBERS
            Matcher username_match = username_pattern.matcher(username);

            Pattern pwd_pattern = Pattern.compile("^[a-zA-Z0-9](?:[._]?[a-zA-Z0-9]){6,30}[a-zA-Z0-9]$"); //NOTE: PASSWORD LENGTH MUST BE 8-80, ONLY CHARACTERS AND NUMBERS
            Matcher pwd_match = pwd_pattern.matcher(password);

            if (repository.findUserByUsername(username) != null) {
                return new ResponseEntity<>(new Payload("username taken"), HttpStatus.NOT_ACCEPTABLE);
            } else {
                if(username_match.matches() && pwd_match.matches()){
                    User user = new User(username, bcrypt(password));
                    repository.save(user);
                    return new ResponseEntity<>(new Payload(new Gson().toJson(user)), HttpStatus.ACCEPTED);
                }else{
                    return new ResponseEntity<>(new Payload("illegally formatted username or/and password"),HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
    }

}