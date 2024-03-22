package org.izdevs.acidium.networking;


import com.google.gson.Gson;
import org.izdevs.acidium.api.v1.Error;
import org.izdevs.acidium.serialization.API;
import org.izdevs.acidium.serialization.Resource;
import org.izdevs.acidium.serialization.ResourceFacade;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1") //RESOURCE GETTER API V1
public class RestAPI {

    @GetMapping(path = "resources/{name}/{api}")
    public String getResource(@PathVariable(name = "name") String name, @PathVariable(name = "api",required = false) String api){
        for(int i=0;i<=ResourceFacade.getResources().size();i++){
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


}
