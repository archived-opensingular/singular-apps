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

package org.opensingular.server.commons.flow;

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
import org.opensingular.flow.core.FlowMap;
import org.opensingular.flow.core.FlowDefinition;
import org.opensingular.flow.core.STask;
import org.opensingular.flow.core.STaskEnd;
import org.opensingular.flow.core.STransition;
import org.opensingular.flow.core.renderer.IFlowRenderer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public enum JGraphFlowRenderer implements IFlowRenderer {

    INSTANCE;

    private static byte[] renderGraphImpl(FlowDefinition<?> definicao) {
        final mxGraph       graph = renderGraph(definicao);
        final RenderedImage img   = mxCellRenderer.createBufferedImage(graph, null, 1, Color.WHITE, false, null);

        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            ImageIO.write(img, "png", out);
        } catch (final IOException e) {
            throw Throwables.propagate(e);
        }

        return out.toByteArray();
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

        addStyleIcone(foo, "TIMER", "timer.png");
        addStyleIcone(foo, "END", "terminate.png");
        addStyleIcone(foo, "MESSAGE", "message_intermediate.png");
        addStyleIcone(foo, "START", "start.png");
        addStyleIcone(foo, "JAVA", "gear.png");
        addStyleIcone(foo, "HUMAN", "pessoinha.png");

        graph.setStylesheet(foo);
    }

    private static void addStyleIcone(mxStylesheet foo, String nomeEstilo, String nomeImagem) {
        final Map<String, Object> def = new HashMap<>();
        def.put(mxConstants.STYLE_SEGMENT, mxConstants.ALIGN_BOTTOM);
        def.put(mxConstants.STYLE_VERTICAL_LABEL_POSITION, mxConstants.ALIGN_BOTTOM);
        def.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_IMAGE);
        def.put(mxConstants.STYLE_IMAGE, String.format("/%s/%s", JGraphFlowRenderer.class.getPackage().getName().replace('.', '/'), nomeImagem));
        foo.putCellStyle(nomeEstilo, def);
    }

    private static mxGraph renderGraph(FlowDefinition<?> definicao) {

        final mxGraph graph  = new mxGraph();
        final Object  parent = graph.getDefaultParent();

        style(graph);

        graph.getModel().beginUpdate();
        graph.setAutoSizeCells(true);

        final FlowMap fluxo = definicao.getFlowMap();

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
            for (final STransition transicao : task.getTransitions()) {
                createTransition(graph, transicao, mapaVertice);
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

    private static void addStartTransition(mxGraph graph, STask<?> taskInicial, Map<String, Object> mapaVertice) {
        final Object v = graph.insertVertex(graph.getDefaultParent(), null, null, 20, 20, 20, 20);

        setStyle(v, "START");
        final Object destino = mapaVertice.get(taskInicial.getAbbreviation());
        graph.insertEdge(graph.getDefaultParent(), null, null, v, destino);
    }

    private static void createTransition(mxGraph graph, STransition transicao, Map<String, Object> mapaVertice) {
        final Object origem  = mapaVertice.get(transicao.getOrigin().getAbbreviation());
        final Object destino = mapaVertice.get(transicao.getDestination().getAbbreviation());
        String       nome    = transicao.getName();
        if (transicao.getDestination().getName().equals(nome)) {
            nome = null;
        } else {
            nome = formatarNome(nome);
        }

        graph.insertEdge(graph.getDefaultParent(), null, nome, origem, destino);
    }

    private static Object insertVertex(mxGraph graph, STask<?> task) {
        final Object v = graph.insertVertex(graph.getDefaultParent(), task.getAbbreviation(), formatarNome(task.getName()),
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

    private static String formatarNome(String nome) {
        return nome.replace(' ', '\n');
    }

    @Override
    public byte[] generateImage(FlowDefinition<?> definicao) {
        return renderGraphImpl(definicao);
    }
}
