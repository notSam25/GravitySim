package gravitysim.gfx;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Vector;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import gravitysim.Global;
import gravitysim.objects.Circle;

public class Window extends JFrame {
    private class Panel extends JPanel {
        public Panel() {
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (Circle circle : getCircles()) {
                g.setColor(Color.blue);
                g.fillOval(circle.getPosition()[0], circle.getPosition()[1], circle.getRadius() * 5, circle.getRadius() * 5);
            }

            g.dispose();
        }

        public void addCircle(Circle circle) {
            this.m_Circles.add(circle);
        }

        public Vector<Circle> getCircles() {
            return this.m_Circles;
        }

        private Vector<Circle> m_Circles = new Vector<>();
    }

    public void update() {
        double speed = 0.05;
        int gravity = 10;

        for (Circle curCircle : this.m_Panel.getCircles()) {
            curCircle.m_AccelerationX = 0;
            curCircle.m_AccelerationY = 0;

            for (Circle neighborCircle : this.m_Panel.getCircles()) {

                if(neighborCircle == curCircle)
                    continue;

                // get the difference in positions from neighbor
                int Dx = neighborCircle.getPosition()[0] - curCircle.getPosition()[0],
                        Dy = neighborCircle.getPosition()[1] - curCircle.getPosition()[1];

                // calculate acceleration relative to the neighbor
                // D = sqrt((Dx)^2 + (Dy)^2)
                // modified it because I didn't want to type all the code for finding diffrence
                // again.
                double distance = Math.sqrt(Math.pow(Dx, 2) + Math.pow(Dy, 2) + Math.pow(12, 2));

                // calculate acceleration due to gravity
                // A = GM / r^2
                curCircle.m_AccelerationX += (gravity * neighborCircle.getMass() * Dx / Math.pow(distance, 2));
                curCircle.m_AccelerationY += (gravity * neighborCircle.getMass() * Dy / Math.pow(distance, 2));
            }

            // Calculate velocity, replace time with a smoothing value
            // v = u + at
            // velocity = initialVelocity + time * acceleration
            curCircle.m_VelocityX += curCircle.m_AccelerationX * (speed / 2);
            curCircle.m_VelocityY += curCircle.m_AccelerationY * (speed / 2);

            // calculate position
            curCircle.m_X += curCircle.m_VelocityX * speed;
            curCircle.m_Y += curCircle.m_VelocityY * speed;
        }
    }

    public void render() throws InterruptedException {
        m_Panel.setDoubleBuffered(true);
        m_Panel.setPreferredSize(new Dimension(Global.g_WindowWidth, Global.g_WindowHeight));

        this.setTitle(m_WindowName);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocation(Global.g_WindowPosition);
        this.setUndecorated(false);
        this.add(this.m_Panel);
        this.pack();
        this.setVisible(true);
        this.addMouseListener(MouseListener.getListener());

        long now = System.nanoTime(), interval = 1000000000 / Global.g_FramesPerSecond, next = now + interval;

        int TPS = 0;

        while (true) {
            MouseListener.update();

            if (MouseListener.buttons[MouseListener.MouseButton.MOUSE_LEFT.ordinal()].pressed) {
                System.out.println("Circle Added");
                this.m_Panel.addCircle(new Circle(MouseListener.getMouseWindowPosition()));
            }
                

            update();

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

    private static final String m_WindowName = "GravitySim " + Global.g_ProjectVersion;
    private final Panel m_Panel = new Panel();
}
