package org.cardanofoundation.configs.springconfigs.scope;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.springframework.context.support.SimpleThreadScope;
import org.springframework.beans.factory.ObjectFactory;
import java.util.Map;
import java.util.Objects;

public class RemoteDriverScope extends SimpleThreadScope {

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory){
        Object obj = super.get(name, objectFactory);
        SessionId sessionId = ((RemoteWebDriver)obj).getSessionId();
        if (Objects.isNull(sessionId)){
            super.remove(name);
            obj = super.get(name, objectFactory);
        }
        return obj;
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback){

    }

}
