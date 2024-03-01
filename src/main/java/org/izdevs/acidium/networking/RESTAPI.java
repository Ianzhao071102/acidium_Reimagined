package org.izdevs.acidium.networking;


import org.izdevs.acidium.api.v1.Error;
import org.izdevs.acidium.serialization.API;
import org.izdevs.acidium.serialization.Resource;
import org.izdevs.acidium.serialization.ResourceFacade;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1") //RESOURCE GETTER API V1
public class RESTAPI {
    @GetMapping(value = "/resources/v1",produces = "application/json")
    public String getResources(){
        return ResourceFacade.getResources().toString();
    }

    @GetMapping(value = "/resources/v1",produces = "application/json")
    public Resource getResource(@RequestParam(name = "name") String name,@RequestParam(required = false) String api){
        for(int i=0;i<=ResourceFacade.getResources().size();i++){
            Resource resource = ResourceFacade.getResources().get(i);
            if(resource.getName().equals(name)){
                if(api.isEmpty() || api.isBlank()){
                    //no api pass directly
                    return resource;
                }
                else{
                    //api specified check if satisfied
                    API destl = null;
                    for(int j=i;j<=ResourceFacade.getResources().size()-i;j++){
                        if(ResourceFacade.getResources().get(i).isApi() && ResourceFacade.getResources().get(i).getName().equals(api)){
                            destl = (API) ResourceFacade.getResources().get(i);
                        }
                    }
                    if(destl == null){
                        return new Error(new Throwable("The API is NOT FOUND"));
                    }
                    else{
                        //pass the resource
                        if(resource.associatedApi.equals(destl)){
                            return resource;
                        }
                    }
                }
            }
        }
        return new Error(new Throwable("The code never works"));
    }
}
