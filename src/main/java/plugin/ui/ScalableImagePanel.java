package plugin.ui;

import javax.swing.*;
import java.awt.*;

class ScalableImagePanel extends JPanel {
    private final Image image;

    public ScalableImagePanel(Image image) {
        this.image = image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        try {
            drawImage(g2);
        } finally {
            g2.dispose();
        }
    }

    /**
     * When re-sizing the window, the image is re-drawn proportionally, so that there might be either vertical or
     * horizontal empty areas. To make those areas not visible, panel background is set to the same color as the
     * background of the image.
     */
    @Override
    public Color getBackground() {
        //noinspection UseJBColor (if to change it to JBColor, it doesn't actually work just showing instead some default color)
        return Color.WHITE;
    }

    private void drawImage(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int cw = getWidth();
        int ch = getHeight();
        int iw = image.getWidth(this);
        int ih = image.getHeight(this);

        if (iw <= 0 || ih <= 0) return;

        double scale = Math.min((double) cw / iw, (double) ch / ih);
        int w = (int) (iw * scale);
        int h = (int) (ih * scale);
        int x = (cw - w) / 2;
        int y = (ch - h) / 2;

        g2.drawImage(image, x, y, w, h, this);
    }
}

