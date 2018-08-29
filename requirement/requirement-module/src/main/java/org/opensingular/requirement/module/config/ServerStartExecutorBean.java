/*
 * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensingular.requirement.module.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.SmartLifecycle;

/**
 * Bean que tem o método {@link #start()} chamado assim que o contexto
 * do Spring é completamente levantado.
 *
 * Todas as classes interessadas em serem executadas podem chamar o método
 * {@link #register(Runnable)} para executar algum código.
 * Para outros beans do Spring a forma mais fácil é se auto-registrar
 * utilizando @{@link javax.annotation.PostConstruct}
 */
public class ServerStartExecutorBean implements SmartLifecycle {

    private volatile boolean isRunning = false;

    private List<Runnable> executaveis;

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable r) {
        r.run();
        isRunning = false;
    }

    @Override
    public void start() {
        getExecutaveis().forEach(Runnable::run);
        isRunning = true;
    }

    @Override
    public void stop() {
        isRunning = false;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public int getPhase() {
        return 1;
    }

    /**
     * Método utilizado para registrar código
     * que deve ser executado assim que o Singular Server
     * terminar sua inicialização.
     *
     * @param runnable - callback a ser executado
     */
    public void register(Runnable runnable) {
        getExecutaveis().add(runnable);
    }

    private List<Runnable> getExecutaveis() {
        if (executaveis == null) {
            executaveis = new ArrayList<>();
        }

        return executaveis;
    }

}