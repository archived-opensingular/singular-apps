package org.opensingular.server.commons.flow;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.opensingular.flow.core.ProcessDefinition;
import org.opensingular.lib.commons.context.SingularContextSetup;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public abstract class FlowRenderTest implements Loggable {

    private static final Object lock = new Object();

    @Before
    public void setUp(){
        SingularContextSetup.reset();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(){
            @Override
            public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
                return Mockito.mock(requiredType);
            }

            @Override
            public  <T> T getBean(Class<T> requiredType) throws BeansException {
                return Mockito.mock(requiredType);
            }
        };
        context.refresh();
        new ApplicationContextProvider().setApplicationContext(context);
    }


    /**
     * Método para ser sobrescrito para a geração do gráfico para
     * o desenvolvedor
     * Deve ser ignorado pois impede a execução dos testes em ferramentas de
     * build
     */
    public void render() {
        openJFrame(renderImage(getInstanceToRender()));
    }

    @Test
    public void testRendering() {
        Assert.assertNotNull(renderImage(getInstanceToRender()));
    }

    protected abstract ProcessDefinition<?> getInstanceToRender();

    protected byte[] renderImage(ProcessDefinition<?> instanceToRender) {
        return JGraphFlowRenderer.INSTANCE.generateImage(instanceToRender);
    }


    protected void openJFrame(byte[] image) {
        final JFrame frame     = new JFrame();
        JPanel       mainPanel = new JPanel(new BorderLayout(5,5));
        JLabel       lblimage  = new JLabel(new ImageIcon(image));
        mainPanel.add(lblimage);
        frame.add(mainPanel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) {
                synchronized (lock) {
                    frame.setVisible(false);
                    lock.notify();
                }
            }

        });
        synchronized (lock) {
            while (frame.isVisible())
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    getLogger().error(e.getMessage(), e);
                }
        }
    }
}
