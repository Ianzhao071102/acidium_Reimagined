package org.izdevs.acidium.networking;


import com.google.gson.Gson;
import com.google.re2j.Matcher;
import com.google.re2j.Pattern;
import jakarta.servlet.http.HttpServletResponse;
import org.izdevs.acidium.api.v1.Error;
import org.izdevs.acidium.api.v1.Payload;
import org.izdevs.acidium.serialization.API;
import org.izdevs.acidium.serialization.Resource;
import org.izdevs.acidium.serialization.ResourceFacade;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.izdevs.acidium.AcidiumApplication.SQLConnection;

@RestController
@RequestMapping("v1") //RESOURCE GETTER API V1
public class RestAPI {
    public static int playersOnline = 0;

    @GetMapping(path = "resources/{name}/{api}")
    public String getResource(@PathVariable(name = "name") String name, @PathVariable(name = "api", required = false) String api,HttpServletResponse response) {
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

    @PostMapping(path = "/login/{username}/{password}")
    //SPRING WILL AUTOMATICALLY PASS HTTPSERVLETRESPONSE BEAN TO THIS METHOD IT WILL WORK FOR SURE
    public ResponseEntity<Payload> login(@PathVariable(name = "username") String username, @PathVariable(name = "password") String password, HttpServletResponse response) throws SQLException, IOException {
        Pattern username_pattern = Pattern.compile("^([a-z]|[A-Z]|[0-9]|[\\-]|[\\_]){5,20}$"); //NOTE: USERNAME LENGTH MUST BE 5-20, ONLY CHARACTERS AND NUMBERS
        Matcher username_match = username_pattern.matcher(username);

        Pattern pwd_pattern = Pattern.compile("^[a-z]|[A-Z]|[0-9]{8,80}$"); //NOTE: PASSWORD LENGTH MUST BE 8-80, ONLY CHARACTERS AND NUMBERS
        Matcher pwd_match = pwd_pattern.matcher(password);

        if (username_match.matches() && pwd_match.matches()) {
            ResultSet rs = SQLConnection.createStatement().executeQuery("SELECT username,uuid,passwordhash FROM users WHERE username = " + username);

            //TODO FINISH USERNAME AND PASSWORD HASH MATCHING AND ALONGSIDE COOKIE SETTING
        } else {
            //username or password is illegal
            response.getWriter().write("illegal username or password");
            response.getWriter().flush();
            return new ResponseEntity<>(new Payload("illegal parameter"), HttpStatusCode.valueOf(403));
        }
        return new ResponseEntity<>(new Payload("illegal parameter"), HttpStatusCode.valueOf(403));
    }

    @PostMapping(path = "/operations/move/{degree}", params = "degree")
    public ResponseEntity<Payload> move(@PathVariable(name = "degree") double degree, HttpServletResponse response) throws IOException {
        //TODO FINISH TESTING AND FINISH THIS ALGORITHM
        return null;
    }
}
