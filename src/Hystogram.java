import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Hystogram extends JFrame {
  int hystR[];
  int hystG[];
  int hystB[];
  int hystL[];
  BufferedImage sample;
  JPanel canvasPanel;
  boolean[][] canvasMatrix = new boolean[1280][512];

  private int round(double d) {
    double dAbs = Math.abs(d);
    int i = (int) dAbs;
    double result = dAbs - (double) i;
    if (result < 0.5) {
      return d < 0 ? -i : i;
    } else {
      return d < 0 ? -(i + 1) : i + 1;
    }
  }

  public Hystogram() {
    super("Гистограмма");
    setPreferredSize(new Dimension(1280, 512));
    setResizable(false);

    try {
      sample = ImageIO.read(new File("sample.png"));
      hystR = new int[256];
      hystG = new int[256];
      hystB = new int[256];
      hystL = new int[256];
      canvasPanel = new JPanel() {
        public void paintComponent(Graphics g) {
          super.paintComponent(g);
          this.setBackground(Color.BLACK);

          int maxColor = 0;
          for (int c : hystR) {
            if (c > maxColor) {
              maxColor = c;
            }
          }
          for (int c : hystG) {
            if (c > maxColor) {
              maxColor = c;
            }
          }
          for (int c : hystB) {
            if (c > maxColor) {
              maxColor = c;
            }
          }
          int maxL = 0;
          for (int c : hystL) {
            if (c > maxL) {
              maxL = c;
            }
          }

          //red
          for (int i = 0; i < 256; ++i) {
            g.setColor(Color.RED);
            g.drawLine(i, 512 - hystR[i] * 512 / maxColor, i, 511);
          }
          //green
          for (int i = 0; i < 256; ++i) {
            g.setColor(Color.GREEN);
            g.drawLine(i + 256, 512 - hystG[i] * 512 / maxColor, i + 256, 511);
          }
          //blue
          for (int i = 0; i < 256; ++i) {
            g.setColor(Color.BLUE);
            g.drawLine(i + 512, 512 - hystB[i] * 512 / maxColor, i + 512, 511);
          }

          for (int i = 0; i < 256; ++i) {
            g.setColor(Color.WHITE);
            g.drawLine(i * 2 + 768, 512 - hystL[i] * 512 / maxL, i * 2 + 768, 511);
            g.drawLine(i * 2 + 769, 512 - hystL[i] * 512 / maxL, i * 2 + 769, 511);
          }
        }
      };
      for (int i = 0; i < sample.getWidth(); ++i) {
        for (int j = 0; j < sample.getHeight(); ++j) {
          int argb = sample.getRGB(i, j);
          int alpha = (argb >> 24) & 0xff;
          int red = (argb >> 16) & 0xff;
          int green = (argb >> 8) & 0xff;
          int blue = (argb) & 0xff;
          ++hystR[red];
          ++hystG[green];
          ++hystB[blue];
          ++hystL[round(0.3 * red + 0.59 * green + 0.11 * blue)];
        }
      }
      canvasPanel.repaint();
    } catch (IOException e) {
      e.printStackTrace();
    }

    setLayout(new BorderLayout());
    add(canvasPanel);
    setFocusableWindowState(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(this);
    pack();
    setVisible(true);
  }
}
