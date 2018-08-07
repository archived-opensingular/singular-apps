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

package org.opensingular.requirement.module.flow.renderer;

import com.google.common.base.Throwables;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxEdgeLabelLayout;
import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.layout.orthogonal.mxOrthogonalLayout;
import com.mxgraph.model.mxICell;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;
import org.opensingular.flow.core.FlowDefinition;
import org.opensingular.flow.core.FlowMap;
import org.opensingular.flow.core.STask;
import org.opensingular.flow.core.STaskEnd;
import org.opensingular.flow.core.STransition;
import org.opensingular.flow.core.renderer.ExecutionHistoryForRendering;
import org.opensingular.flow.core.renderer.IFlowRenderer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JGraphFlowRenderer implements IFlowRenderer {

    @Nonnull
    public byte[] generatePng(@Nonnull FlowDefinition<?> definition, @Nullable ExecutionHistoryForRendering history) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            generatePng(definition, history, outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public void generatePng(@Nonnull FlowDefinition<?> definition, @Nullable ExecutionHistoryForRendering history,
            @Nonnull OutputStream out) throws IOException {
        mxGraph graph = renderGraph(definition);
        RenderedImage img = mxCellRenderer.createBufferedImage(graph, null, 1, Color.WHITE, false, null);
        ImageIO.write(img, "png", out);
    }

    private static void style(mxGraph graph) {
        final mxStylesheet foo = new mxStylesheet();

        final Map<String, Object> stil = new HashMap<>();
        stil.put(mxConstants.STYLE_ROUNDED, Boolean.TRUE);
        stil.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ORTHOGONAL);
        stil.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR);
        stil.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);
        stil.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);
        stil.put(mxConstants.STYLE_VERTICAL_LABEL_POSITION, mxConstants.ALIGN_BOTTOM);
        stil.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_BOTTOM);
        stil.put(mxConstants.STYLE_STROKECOLOR, "#6482B9");
        stil.put(mxConstants.STYLE_FONTCOLOR, "#446299");
        foo.setDefaultEdgeStyle(stil);

        addStyleIcon(foo, "TIMER", "timer.png");
        addStyleIcon(foo, "END", "terminate.png");
        addStyleIcon(foo, "MESSAGE", "message_intermediate.png");
        addStyleIcon(foo, "START", "start.png");
        addStyleIcon(foo, "JAVA", "gear.png");
        addStyleIcon(foo, "HUMAN", "pessoinha.png");

        graph.setStylesheet(foo);
    }

    private static void addStyleIcon(mxStylesheet foo, String styleName, String imageName) {
        final Map<String, Object> def = new HashMap<>();
        def.put(mxConstants.STYLE_SEGMENT, mxConstants.ALIGN_BOTTOM);
        def.put(mxConstants.STYLE_VERTICAL_LABEL_POSITION, mxConstants.ALIGN_BOTTOM);
        def.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_IMAGE);
        def.put(mxConstants.STYLE_IMAGE, String.format("/%s/%s", JGraphFlowRenderer.class.getPackage().getName().replace('.', '/'), imageName));
        foo.putCellStyle(styleName, def);
    }

    private static mxGraph renderGraph(FlowDefinition<?> definition) {

        final mxGraph graph  = new mxGraph();
        final Object  parent = graph.getDefaultParent();

        style(graph);

        graph.getModel().beginUpdate();
        graph.setAutoSizeCells(true);

        final FlowMap fluxo = definition.getFlowMap();

        final Map<String, Object> mapaVertice = new HashMap<>();
        for (final STask<?> task : fluxo.getTasks()) {
            final Object v = insertVertex(graph, task);
            mapaVertice.put(task.getAbbreviation(), v);
        }
        for (final STaskEnd task : fluxo.getEndTasks()) {
            final Object v = insertVertex(graph, task);
            mapaVertice.put(task.getAbbreviation(), v);
        }

        addStartTransition(graph, fluxo.getStart().getTask(), mapaVertice);

        for (final STask<?> task : fluxo.getTasks()) {
            //for (STransition transition : task.getTransitions()) {
            //    createTransition(graph, transition, mapaVertice);
            //}
            //The code bellow works around a bug of mxGraph. The commented code above should have been enough.
            Collection<List<STransition>> simplify = joinTransactionSameDestination(task.getTransitions());
            for (List<STransition> transitionBlock : simplify) {
                if (simplify.size() == 1) {
                    createTransition(graph, transitionBlock.get(0), mapaVertice);
                } else {
                    createTransition(graph, transitionBlock, mapaVertice);
                }
            }
        }

        final mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
        layout.setOrientation(SwingConstants.WEST);
        layout.setIntraCellSpacing(30);
        layout.setInterRankCellSpacing(50);
        layout.setDisableEdgeStyle(false);
        layout.execute(parent);
        final mxParallelEdgeLayout layoutParalelo = new mxParallelEdgeLayout(graph);
        layoutParalelo.execute(parent);
        final mxOrthogonalLayout mxOrthogonalLayout = new mxOrthogonalLayout(graph);
        mxOrthogonalLayout.execute(parent);
        final mxEdgeLabelLayout labelLayout = new mxEdgeLabelLayout(graph);
        labelLayout.execute(parent);
        graph.getModel().endUpdate();

        return graph;
    }

    private static Collection<List<STransition>> joinTransactionSameDestination(List<STransition> transitions) {
        Map<STask<?>,List<STransition>> groups = new HashMap<>();
        for(STransition t : transitions) {
            groups.computeIfAbsent(t.getDestination(), d -> new ArrayList<>()).add(t);
        }
        return groups.values();
    }

    private static void addStartTransition(mxGraph graph, STask<?> taskInicial, Map<String, Object> mapaVertice) {
        final Object v = graph.insertVertex(graph.getDefaultParent(), null, null, 20, 20, 20, 20);

        setStyle(v, "START");
        final Object destiny = mapaVertice.get(taskInicial.getAbbreviation());
        graph.insertEdge(graph.getDefaultParent(), null, null, v, destiny);
    }

    private static void createTransition(mxGraph graph, List<STransition> transitionBlock,
            Map<String, Object> mapNodes) {
        String name = transitionBlock.stream().map(t -> t.getName()).filter(t -> t != null).collect(
                Collectors.joining("\n"));
        createTransition(graph, transitionBlock.get(0), name, mapNodes);
    }

    private static void createTransition(mxGraph graph, STransition transition, Map<String, Object> mapNodes) {
        createTransition(graph, transition, transition.getName(), mapNodes);
    }

    private static void createTransition(mxGraph graph, STransition transition, String transitionName,
            Map<String, Object> mapNodes) {
        Object origin = mapNodes.get(transition.getOrigin().getAbbreviation());
        Object destiny = mapNodes.get(transition.getDestination().getAbbreviation());
        String name = transitionName;
        if (transition.getDestination().getName().equals(transitionName)) {
            name = null;
        }
        graph.insertEdge(graph.getDefaultParent(), null, formatName(name), origin, destiny);
    }

    private static Object insertVertex(mxGraph graph, STask<?> task) {
        final Object v = graph.insertVertex(graph.getDefaultParent(), task.getAbbreviation(), formatName(task.getName()),
                20, 20, 20, 20);
        graph.updateCellSize(v);
        if (task.isWait()) {
            setStyle(v, "TIMER");
        } else if (task.isEnd()) {
            setStyle(v, "END");
        } else if (task.isPeople()) {
            setStyle(v, "HUMAN");
        } else if (task.isJava()) {
            if (task.getName().startsWith("Notificar")) {
                setStyle(v, "MESSAGE");
            } else {
                setStyle(v, "JAVA");
            }
        }
        return v;
    }

    private static void setStyle(Object v, String style) {
        ((mxICell) v).setStyle(style);
    }

    @Nullable
    private static String formatName(@Nullable String name) {
        return name == null ? null : name.replace(' ', '\n');
    }

}
