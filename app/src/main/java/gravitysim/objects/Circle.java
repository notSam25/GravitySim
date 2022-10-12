package gravitysim.objects;

import java.awt.Point;

public class Circle {
    public Circle() {}

    public Circle(int x, int y) {
        this.m_X = x;
        this.m_Y = y;
    }

    public Circle(Point point) {
        this.m_X = (int)point.x;
        this.m_Y = (int)point.y;
    }

    public void setPosition(int x, int y) {
        this.m_X = x;
        this.m_Y = y;
    }

    public int[] getPosition() {
        return new int[] { m_X, m_Y} ;
    }

    public int getMass() {
        return m_Mass;
    }

    public int getRadius() {
        return this.m_Radius;
    }

    public int m_X = 0, m_Y = 0, m_Mass = ((int)(Math.random() * 450) + 50), m_Radius = (int)Math.sqrt(this.m_Mass / 3);
    public double m_AccelerationX = 0, m_AccelerationY = 0, m_VelocityX, m_VelocityY;
}
