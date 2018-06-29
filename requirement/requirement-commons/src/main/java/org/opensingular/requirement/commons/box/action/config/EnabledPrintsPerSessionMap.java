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

        Optional<Map.Entry<String, Long>> entryKey = map.entrySet()
                .parallelStream()
                .filter(f -> f.getValue().equals(requirementCod))
                .findFirst();

        if (entryKey.isPresent()) {
            return entryKey.get().getKey();
        }

        String randomUUID = UUID.randomUUID().toString();
        map.put(randomUUID, requirementCod);
        return randomUUID;
    }

    public Optional<Long> get(String uuid) {
        return Optional.ofNullable(map.get(uuid));
    }

}
