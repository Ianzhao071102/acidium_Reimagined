package org.izdevs.acidium.networking;


import com.google.gson.Gson;
import com.google.re2j.Matcher;
import com.google.re2j.Pattern;
import org.izdevs.acidium.api.v1.Error;
import org.izdevs.acidium.security.AuthorizationContent;
import org.izdevs.acidium.serialization.API;
import org.izdevs.acidium.serialization.Resource;
import org.izdevs.acidium.serialization.ResourceFacade;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;


import static org.izdevs.acidium.AcidiumApplication.SQLConnection;
import static org.izdevs.acidium.AcidiumApplication.bcrypt;

@RestController
@RequestMapping("v1") //RESOURCE GETTER API V1
public class RestAPI {

    @GetMapping(path = "resources/{name}/{api}")
    public String getResource(@PathVariable(name = "name") String name, @PathVariable(name = "api",required = false) String api){
        for(int i=0;i<=ResourceFacade.getResources().size()-1;i++){
            Resource resource = ResourceFacade.getResources().get(i);
            if( (name.isEmpty() || name.isBlank()) && (api.isBlank() || api.isEmpty())){
                Gson gson = new Gson();
                return gson.toJson(ResourceFacade.getResources());
            }
            if(resource.getName().equals(name)){
                if(api.isEmpty() || api.isBlank()){
                    //no api pass directly
                    Gson gson = new Gson();
                    return gson.toJson(resource);
                }
                else{
                    //api specified check if satisfied
                    API destl = null;
                    for(int j=i;j<=ResourceFacade.getResources().size()-i;j++){
                        if (ResourceFacade.getResources().get(i).isApi() && ResourceFacade.getResources().get(i).getName().equals(api)) {
                            destl = (API) ResourceFacade.getResources().get(i);
                            break;
                        }
                    }
                    if(destl == null){
                        Gson gson = new Gson();
                        return gson.toJson(new Throwable("The API is NOT FOUND"));
                    }
                    else{
                        //pass the resource
                        if(resource.associatedApi.equals(destl)){
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
    public String login(@PathVariable(name="username") String username,@PathVariable(name="password") String password) throws SQLException {
        Pattern username_pattern = Pattern.compile("^([a-z]|[A-Z]|[0-9]|[\\-]|[\\_]){5,20}$"); //NOTE: USERNAME LENGTH MUST BE 5-20, ONLY CHARACTERS AND NUMBERS
        Matcher username_match = username_pattern.matcher(username);

        Pattern pwd_pattern = Pattern.compile("^[a-z]|[A-Z]|[0-9]{8,80}$"); //NOTE: PASSWORD LENGTH MUST BE 8-80, ONLY CHARACTERS AND NUMBERS
        Matcher pwd_match = pwd_pattern.matcher(password);

        if(username_match.matches() && pwd_match.matches()){
            //if username and password matches expression
            ResultSet result = SQLConnection.createStatement().executeQuery("SELECT uuid,username,passwordhash from users WHERE username = " + username);
            int rowcount = 0;
            if (result.last()) {
                rowcount = result.getRow();

                //move it back
                result.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
            }
            if(rowcount != 1) return new Gson().toJson(new Error(new IllegalArgumentException("invalid username")));
            else{
                //rowcount == 1
                String uuid = result.getString("uuid");
                String passwordHash = result.getString("passwordhash");
                String hashed = bcrypt(password);

                if(passwordHash.equals(hashed)){
                    //password satisfied
                    //return new Gson().toJson(new AuthorizationContent(uuid,UUI));
                }
            }

        }else{
            return new Gson().toJson(new Error(new IllegalArgumentException("username or password is invalid")));
        }
        return new Gson().toJson(new Error(new IllegalArgumentException("argument is suspiciously illegal")));
    }
}
