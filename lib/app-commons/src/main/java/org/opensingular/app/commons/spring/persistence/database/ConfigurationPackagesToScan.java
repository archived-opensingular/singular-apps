package org.opensingular.app.commons.spring.persistence.database;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ConfigurationPackagesToScan {

    /**
     * A map that contains key = Package to Scan
     * value = True for create the entities in the package, False don't create the entities.
     */
    private Map<String, Boolean> packagesToScan = new HashMap<>();

    public void addPackageToScan(String packageToScan, boolean execute) {
        packagesToScan.put(packageToScan, execute);
    }

    public void addPackageToScan(String packageToScan) {
        packagesToScan.put(packageToScan, Boolean.TRUE);
    }

    /**
     * This method return just the Packages that want to crete the entities. Just that have TRUE in the value of Map.
     * This use default scope (package private), because just the Singular Persinstence should see this.
     * @return A Set of string containg the packages.
     */
    Set<String> getPackagesToScan() {
        return packagesToScan
                .entrySet()
                .parallelStream()
                .filter(s -> Boolean.TRUE.equals(s.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

    }

    /**
     * This method return all packages.
     * This use default scope (package private), because just the Singular Persinstence should see this.
     * @return A Set of String containg all packages.
     */
    Set<String> getAllPackagesToScan() {
        return packagesToScan.keySet();
    }

}
