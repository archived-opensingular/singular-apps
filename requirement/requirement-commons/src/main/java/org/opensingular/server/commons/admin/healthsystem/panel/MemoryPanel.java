/*
 *
 *  * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.opensingular.server.commons.admin.healthsystem.panel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.util.lang.Bytes;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.opensingular.lib.wicket.util.util.Shortcuts.*;

@SuppressWarnings("serial")
public class MemoryPanel extends Panel {

    private ListModel<String> heap     = new ListModel<>(new ArrayList<>(0));
    private ListModel<String> gc       = new ListModel<>(new ArrayList<>(0));
    private ListModel<String> memPools = new ListModel<>(new ArrayList<>(0));

    public MemoryPanel(String id) {
        super(id);
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();

        heap.getObject().clear();
        MemoryUsage mu   = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        MemoryUsage muNH = ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage();
        heap.getObject().add("Init: " + Bytes.bytes(mu.getInit()));
        heap.getObject().add("Max: " + Bytes.bytes(mu.getMax()));
        heap.getObject().add("Used: " + Bytes.bytes(mu.getUsed()));
        heap.getObject().add("Commited: " + Bytes.bytes(mu.getCommitted()));
        heap.getObject().add("Init NonHeap: " + Bytes.bytes(muNH.getInit()));
        heap.getObject().add("Max NonHeap: " + (muNH.getMax() > 0 ? Bytes.bytes(muNH.getMax()) : muNH.getMax()));
        heap.getObject().add("Used NonHeap: " + Bytes.bytes(muNH.getUsed()));
        heap.getObject().add("Commited NonHeap: " + Bytes.bytes(muNH.getCommitted()));


        gc.getObject().clear();
        List<GarbageCollectorMXBean> gcList = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean tmpGC : gcList) {

            gc.getObject().add("<b>Name: " + tmpGC.getName() + "</b>");
            gc.getObject().add("&nbsp;&nbsp;&nbsp;&nbsp; Collection count: " + tmpGC.getCollectionCount());
            gc.getObject().add("&nbsp;&nbsp;&nbsp;&nbsp; Collection time: " + tmpGC.getCollectionTime());
            StringBuilder pools = new StringBuilder("&nbsp;&nbsp;&nbsp;&nbsp; Memory Pools: ");

            String[] memoryPoolNames = tmpGC.getMemoryPoolNames();
            for (String mpnTmp : memoryPoolNames) {
                pools.append(", ").append(mpnTmp);
            }
            gc.getObject().add(pools.toString());
        }


        memPools.getObject().clear();
        List<MemoryPoolMXBean> memoryList = ManagementFactory.getMemoryPoolMXBeans();
        for (MemoryPoolMXBean tmpMem : memoryList) {

            memPools.getObject().add("<b>Memory Pool Info: " + tmpMem.getName() + "</b>");
            memPools.getObject().add("&nbsp;&nbsp;&nbsp;&nbsp; Usage: " + tmpMem.getUsage());
            memPools.getObject().add("&nbsp;&nbsp;&nbsp;&nbsp; Collection Usage: " + tmpMem.getCollectionUsage());
            memPools.getObject().add("&nbsp;&nbsp;&nbsp;&nbsp; Peak Usage: " + tmpMem.getPeakUsage());
            memPools.getObject().add("&nbsp;&nbsp;&nbsp;&nbsp; Type: " + tmpMem.getType());


            StringBuilder manager = new StringBuilder("&nbsp;&nbsp;&nbsp;&nbsp; Memory Manager Names: ");


            String[] memManagerNames = tmpMem.getMemoryManagerNames();
            for (String mmnTmp : memManagerNames) {
                manager.append(", ").append(mmnTmp);
            }
            memPools.getObject().add(manager.toString());
        }
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new RefreshingView<String>("heap") {
            @Override
            protected Iterator<IModel<String>> getItemModels() {
                return heap.getObject().stream().map(this::toModel).iterator();
            }

            private IModel<String> toModel(String s) {
                return $m.ofValue(s);
            }

            @Override
            protected void populateItem(Item<String> item) {
                Label label = new Label("stat", item.getModel());
                label.setEscapeModelStrings(false);
                item.add(label);
            }
        });

        add(new RefreshingView<String>("gc") {
            @Override
            protected Iterator<IModel<String>> getItemModels() {
                return gc.getObject().stream().map(this::toModel).iterator();
            }

            private IModel<String> toModel(String s) {
                return $m.ofValue(s);
            }

            @Override
            protected void populateItem(Item<String> item) {
                Label label = new Label("stat", item.getModel());
                label.setEscapeModelStrings(false);
                item.add(label);
            }
        });

        add(new RefreshingView<String>("memPools") {
            @Override
            protected Iterator<IModel<String>> getItemModels() {
                return memPools.getObject().stream().map(this::toModel).iterator();
            }

            private IModel<String> toModel(String s) {
                return $m.ofValue(s);
            }

            @Override
            protected void populateItem(Item<String> item) {
                Label label = new Label("stat", item.getModel());
                label.setEscapeModelStrings(false);
                item.add(label);
            }
        });
    }
}
