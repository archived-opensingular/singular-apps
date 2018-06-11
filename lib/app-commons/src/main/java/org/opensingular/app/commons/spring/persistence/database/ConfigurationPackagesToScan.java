package org.opensingular.app.commons.spring.persistence.database;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ConfigurationPackagesToScan {

    private Map<String, Boolean> packagesToScan = new HashMap<>();

    public void addPackageToScan(String packageToScan, boolean execute) {
        packagesToScan.put(packageToScan, execute);
    }

    public void addPackageToScan(String packageToScan) {
        packagesToScan.put(packageToScan, true);
    }

    Set<String> getPackagesToScan() {
        return packagesToScan
                .entrySet()
                .parallelStream()
                .filter(s -> Boolean.TRUE.equals(s.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

    }

    Set<String> getAllPackagesToScan() {
        return packagesToScan.keySet();
    }

}
