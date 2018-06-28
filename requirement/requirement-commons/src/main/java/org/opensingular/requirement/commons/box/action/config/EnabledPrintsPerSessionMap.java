package org.opensingular.requirement.commons.box.action.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(WebApplicationContext.SCOPE_SESSION)
public class EnabledPrintsPerSessionMap {
    private Map<String, Long> map = new HashMap<>();

    public String put(Long requirementCod) {
        String randomUUID = UUID.randomUUID().toString();
        map.put(randomUUID, requirementCod);
        return randomUUID;
    }

    public Optional<Long> pop(String uuid) {
        return Optional.ofNullable(map.remove(uuid));
    }
}
