package org.izdevs.acidium.networking;


import com.google.gson.Gson;
import com.google.re2j.Matcher;
import com.google.re2j.Pattern;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.izdevs.acidium.api.v1.Error;
import org.izdevs.acidium.api.v1.Payload;
import org.izdevs.acidium.api.v1.Player;
import org.izdevs.acidium.api.v1.User;
import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.basic.UserRepository;
import org.izdevs.acidium.scheduling.DelayedTask;
import org.izdevs.acidium.scheduling.LoopManager;
import org.izdevs.acidium.security.AuthorizationContent;
import org.izdevs.acidium.security.SessionDetail;
import org.izdevs.acidium.security.SessionGenerator;
import org.izdevs.acidium.serialization.API;
import org.izdevs.acidium.serialization.Resource;
import org.izdevs.acidium.serialization.ResourceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static org.izdevs.acidium.AcidiumApplication.SQLConnection;
import static org.izdevs.acidium.AcidiumApplication.bcrypt;

@RestController
@RequestMapping("v1") //RESOURCE GETTER API V1
public class RestAPI {
    public static int playersOnline = 0;

    @Autowired
    public UserRepository repository;

    @GetMapping(path = "resources/{name}/{api}")
    public String getResource(@PathVariable(name = "name") String name, @PathVariable(name = "api", required = false) String api, HttpServletResponse response) {
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
    public ResponseEntity<Payload> login(@PathVariable(name = "username") String username, @PathVariable(name = "password") String password, HttpServletResponse response, HttpServletRequest request) throws SQLException, IOException {
        Pattern username_pattern = Pattern.compile("^([a-z]|[A-Z]|[0-9]|[\\-]|[\\_]){5,20}$"); //NOTE: USERNAME LENGTH MUST BE 5-20, ONLY CHARACTERS AND NUMBERS
        Matcher username_match = username_pattern.matcher(username);

        Pattern pwd_pattern = Pattern.compile("^[a-z]|[A-Z]|[0-9]{8,80}$"); //NOTE: PASSWORD LENGTH MUST BE 8-80, ONLY CHARACTERS AND NUMBERS
        Matcher pwd_match = pwd_pattern.matcher(password);

        if (username_match.matches() && pwd_match.matches()) {
            ResultSet rs = SQLConnection.createStatement().executeQuery("SELECT username,uuid,passwordhash FROM users WHERE username = " + username);

            int size = 0;
            if (rs != null) {
                rs.last();    // moves cursor to the last row
                size = rs.getRow(); // get row id
            }

            if (size != 1) {
                response.getWriter().write("illegal username or password");
                response.getWriter().flush();
                return new ResponseEntity<>(new Payload("illegal parameter"), HttpStatusCode.valueOf(403));
            } else {
                //username and password are only one row
                if (username.equals(rs.getString("username")) && bcrypt(password).equals(rs.getString("passwordhash"))) {
                    String playerUUID = rs.getString("uuid");
                    UUID sid = SessionGenerator.use();

                    User user = new User(username, playerUUID);

                    response.addCookie(new Cookie("session", String.valueOf(sid)));
                    response.addCookie(new Cookie("user", user.toString()));

                    //initial value of player
                    request.getSession().setAttribute("player", new Player(user, new Entity(username, 1, 20, 0, 20)));
                    request.getSession().setAttribute("session-id", sid);
                    return new ResponseEntity<>(
                            new Payload(
                                    new Gson().toJson(
                                            new AuthorizationContent(
                                                    playerUUID,
                                                    new SessionDetail(String.valueOf(sid))
                                            )
                                    ))
                            , HttpStatusCode.valueOf(200)
                    );
                } else {
                    response.getWriter().write("illegal username or password");
                    response.getWriter().flush();
                    return new ResponseEntity<>(new Payload("illegal parameter"), HttpStatusCode.valueOf(403));
                }
            }
        } else {
            //username or password is illegal
            response.getWriter().write("illegal username or password");
            response.getWriter().flush();
            return new ResponseEntity<>(new Payload("illegal parameter"), HttpStatusCode.valueOf(403));
        }
    }

    @PostMapping(path = "/operations/move/{degree}", params = "degree")
    public ResponseEntity<Payload> move(@PathVariable(name = "degree") double degree, HttpServletRequest request) {
        if (request.getSession().getAttribute("session-id") == null) {
            //fuck no shit
            return new ResponseEntity<>(HttpStatusCode.valueOf(403));

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

                        LoopManager.scheduleAsyncDelayedTask(1, task);
                        return new ResponseEntity<>(HttpStatus.OK);
                    } else {
                        //invalid session
                        return new ResponseEntity<>(new Payload("invalid session"), HttpStatusCode.valueOf(403));
                    }
                }
            } else {
                return new ResponseEntity<>(HttpStatusCode.valueOf(403));
            }
        }
        return new ResponseEntity<>(HttpStatusCode.valueOf(403));
    }
}

