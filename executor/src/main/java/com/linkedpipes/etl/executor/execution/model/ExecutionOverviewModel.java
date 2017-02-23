package com.linkedpipes.etl.executor.execution.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.linkedpipes.etl.executor.pipeline.PipelineModel;
import com.linkedpipes.etl.rdf.utils.RdfFormatter;

import java.util.Date;

public class ExecutionOverviewModel {

    private final String executionIri;

    private String pipelineIri;

    private int numberOfComponentsToExecute;

    private int numberOfFinishedComponents;

    private String pipelineStarted;

    private String pipelineFinished;

    /**
     * Used only to read the status.
     */
    private final ExecutionStatusMonitor statusMonitor;

    private String lastChange;

    public ExecutionOverviewModel(String executionIri,
            ExecutionStatusMonitor statusMonitor) {
        onBeforeUpdate();
        this.executionIri = executionIri;
        this.statusMonitor = statusMonitor;
    }

    public void onExecutionBegin(Date date) {
        onBeforeUpdate();
        pipelineStarted = RdfFormatter.toXsdDate(date);
    }

    public void onPipelineLoaded(PipelineModel pipeline) {
        onBeforeUpdate();
        pipelineIri = pipeline.getIri();
        numberOfComponentsToExecute = 0;
        for (PipelineModel.Component component : pipeline.getComponents()) {
            if (component.shouldExecute()) {
                ++numberOfComponentsToExecute;
            }
        }
    }

    public void onComponentExecutionEnd() {
        onBeforeUpdate();
        ++numberOfFinishedComponents;
    }

    public void onExecutionCancelling() {
        onBeforeUpdate();
    }

    public void onExecutionEnd(Date date) {
        onBeforeUpdate();
        pipelineFinished = RdfFormatter.toXsdDate(date);
    }

    private void onBeforeUpdate() {
        this.lastChange = RdfFormatter.toXsdDate(new Date());
    }

    public ObjectNode toJson(ObjectMapper mapper) {
        final ObjectNode responseNode = mapper.createObjectNode();

        responseNode.put("pipeline", pipelineIri);
        responseNode.put("execution", executionIri);
        if (pipelineStarted != null) {
            responseNode.put("executionStarted", pipelineStarted);
        }
        if (pipelineFinished != null) {
            responseNode.put("executionFinished", pipelineFinished);
        }
        responseNode.put("status", statusMonitor.getStatus().getIri());
        responseNode.put("lastChange", lastChange);

        final ObjectNode executionProgressNode = mapper.createObjectNode();
        executionProgressNode.put("total", numberOfComponentsToExecute);
        executionProgressNode.put("current", numberOfFinishedComponents);

        responseNode.set("pipelineProgress", executionProgressNode);

        return responseNode;
    }

}
