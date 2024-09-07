package org.izdevs.acidium.basic;

import org.izdevs.acidium.world.World;
import org.izdevs.acidium.world.generater.WorldController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
@Component
public class EntityWorldProxyInvocationController implements InvocationHandler {
    @Autowired
    WorldController controller;
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Entity entity = (Entity) proxy;
        for(World curr: controller.worlds){
            if(curr.getName().equals(entity.world_name)){
                return method.invoke(curr,args);
            }
        }
        throw new RuntimeException("world name is specified is invalid: " + entity.world_name);
    }
}
