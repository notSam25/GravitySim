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

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (Circle circle : getCircles()) {
                g.setColor(Color.blue);
                g.fillOval(circle.getPosition()[0] - circle.getRadius(), circle.getPosition()[1] - circle.getRadius(),
                        circle.getRadius() * 5, circle.getRadius() * 5);
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

    public void update(int tick) {
        double speed = 0.006f;
        double gravityConstant = 200;

        for (Circle curCircle : this.m_Panel.getCircles()) {
            for (Circle neighborCircle : this.m_Panel.getCircles()) {

                if (neighborCircle == curCircle /*|| this.m_Panel.getCircles().elementAt(0) == curCircle*/)
                    continue;

                // d = sqrt((x2 -x1)^2 + (y2 - y1)^2)
                double distance = Math.sqrt(Math.pow(neighborCircle.getPosition()[0] - curCircle.getPosition()[0], 2)
                        + Math.pow(neighborCircle.getPosition()[1] - curCircle.getPosition()[1], 2) + 1);

                // Ag = (G * Mass) / distance * distance^
                curCircle.m_AccelerationX += (gravityConstant * neighborCircle.getMass())
                        / Math.pow(distance, 2) * (neighborCircle.getPosition()[0] - curCircle.getPosition()[0]);

                curCircle.m_AccelerationY += (gravityConstant * neighborCircle.getMass()) / Math.pow(distance, 2)
                        * (neighborCircle.getPosition()[1] - curCircle.getPosition()[1]);
            }

            // Vf = Vi + A * t
            curCircle.m_VelocityX += curCircle.m_AccelerationX * speed;
            curCircle.m_VelocityY += curCircle.m_AccelerationY * speed;

            // pos = pos + Vf * t
            curCircle.m_X += (curCircle.m_VelocityX * speed) / 2;
            curCircle.m_Y += (curCircle.m_VelocityY * speed) / 2;

            // Vf = Vi + A * t
            curCircle.m_VelocityX += curCircle.m_AccelerationX * speed;
            curCircle.m_VelocityY += curCircle.m_AccelerationY * speed;

            curCircle.m_AccelerationX = 0;
            curCircle.m_AccelerationY = 0;
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

        //this.m_Panel.addCircle(new Circle(400, 400));
        //this.m_Panel.getCircles().elementAt(0).m_Mass = 800;

        while (true) {
            MouseListener.update();

            if (MouseListener.buttons[MouseListener.MouseButton.MOUSE_LEFT.ordinal()].pressed) {
                System.out.println("Circle Added");
                this.m_Panel.addCircle(new Circle(MouseListener.getMouseWindowPosition()));
            }
            
            update(TPS);
            
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
