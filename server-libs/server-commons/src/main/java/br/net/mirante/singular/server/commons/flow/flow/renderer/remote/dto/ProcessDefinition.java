package br.net.mirante.singular.server.commons.flow.flow.renderer.remote.dto;

import java.util.List;

public class ProcessDefinition {

    private List<Task> tasks;


    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}