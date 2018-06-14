package org.opensingular.app.commons.spring.persistence.database;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PackageScanConfiguration {

    /**
     * A map that contains key = Package to Scan
     * value = True for create the entities in the package, False don't create the entities.
     */
    private Map<String, Boolean> packagesToScan = new HashMap<>();

    /**
     * Configures the given {@param packagesToScan} as package to be scanned by the hibernate session factory in
     * search of entities.
     * It is possible to configure if the entities found in the given package must be used by the hibernate to
     * database ddl generation using the param {@param ddlEnabled}
     *
     * @param packageToScan The package for example: "org.opensingular"
     * @param ddlEnabled    True to create the entities in the package.
     *                      False don't create the entities.
     */
    public void addPackageToScan(String packageToScan, boolean ddlEnabled) {
        packagesToScan.put(packageToScan, ddlEnabled);
    }

    /**
     * The same as {@link #addPackageToScan(String, boolean)} but the ddl is enabled by default in this method
     *
     * @param packageToScan
     */
    public void addPackageToScan(String packageToScan) {
        this.addPackageToScan(packageToScan, Boolean.TRUE);
    }

    /**
     * This method return just the Packages that want to crete the entities. Just that have TRUE in the value of Map.
     * This use default scope (package private), because just the Singular Persinstence should see this.
     *
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
     *
     * @return A Set of String containg all packages.
     */
    Set<String> getAllPackagesToScan() {
        return packagesToScan.keySet();
    }

}
