package org.opensingular.server.commons.flow;

import org.junit.Assert;
import org.junit.Test;
import org.opensingular.flow.core.ProcessDefinition;
import org.opensingular.lib.commons.util.Loggable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public abstract class FlowRenderTest implements Loggable {

    private static final Object lock = new Object();

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
        JPanel       mainPanel = new JPanel(new BorderLayout());
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
