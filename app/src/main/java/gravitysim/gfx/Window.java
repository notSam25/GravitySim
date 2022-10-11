package gravitysim.gfx;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import gravitysim.Global;

public class Window extends JFrame {
    private class Panel extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.dispose();
        }
    }

    private static final String m_WindowName = "GravitySim " + Global.g_ProjectVersion;
    private final Panel m_Panel = new Panel();

    public void render() throws InterruptedException {
        m_Panel.setDoubleBuffered(true);
        m_Panel.setPreferredSize(new Dimension(500, 500));

        this.setTitle(m_WindowName);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocation(new Point(1000, 400));

        this.setUndecorated(true);
        this.add(this.m_Panel);
        this.pack();
        this.setVisible(true);
        long now = System.nanoTime(), interval = 1000000000 / Global.g_FramesPerSecond, next = now + interval;

        int TPS = 0;

        while (true) {
            this.repaint();
            TPS++;

            double remainder = (next - System.nanoTime()) / 1000000000;
            
            if (remainder < 0)
                remainder = 0;
            
            if (System.nanoTime() >= now + 1000000000) {
                System.out.println(TPS);
                TPS = 0;
                now = System.nanoTime();
            }

            Thread.sleep((long) remainder);
            next += interval;
        }
    }
}
