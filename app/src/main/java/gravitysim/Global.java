package gravitysim;
import java.awt.Point;
import gravitysim.gfx.Window;

public class Global {
    private static final Window window = new Window();
    public static final double g_ProjectVersion = 1.6;
    public static int g_FramesPerSecond = 30;
    public static final int g_WindowHeight = 800, g_WindowWidth = 800;
    public static final Point g_WindowPosition = new Point(0, 0);

    public static Window getWindow() {
        return window;
    }
}
