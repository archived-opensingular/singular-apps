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

package org.opensingular.requirement.module.admin.healthsystem.panel;

import java.io.IOException;
import java.io.Writer;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.util.Date;
import javax.servlet.http.HttpServletResponse;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.IRequestHandler;
import org.opensingular.lib.commons.base.SingularException;

@SuppressWarnings("serial")
public class ThreadPanel extends Panel {


    public ThreadPanel(String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        add(new Button("threadDump") {

            @Override
            public void onSubmit() {
                getRequestCycle().scheduleRequestHandlerAfterCurrent(new IRequestHandler() {
                    @Override
                    public void respond(IRequestCycle requestCycle) {
                        try {
                            HttpServletResponse response = (HttpServletResponse) requestCycle.getResponse().getContainerResponse();
                            response.setContentType("text/plain");
                            response.setCharacterEncoding("UTF-8");
                            String filename = "ThreadDump_" + new Date() + ".txt";
                            filename = filename.replaceAll(" ", "_");
                            response.setHeader("Content-Disposition", "attachment; filename=" + filename);
                            Writer w = response.getWriter();

                            ThreadInfo[] threads = ManagementFactory.getThreadMXBean().dumpAllThreads(true, true);
                            for (ThreadInfo thread : threads) {
                                w.write(thread.toString());
                            }
                            w.flush();
                        } catch (IOException e) {
                            throw SingularException.rethrow(e.getMessage(), e);
                        }
                    }

                    @Override
                    public void detach(IRequestCycle requestCycle) {

                    }
                });

            }
        });

    }
}
