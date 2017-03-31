package edu.buffalo.fractal;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class FractalPanel
  extends JPanel
{
  public static enum SaveFormat
  {
    GIF,  PNG,  JPG;
  }
  
  private static final Dimension DEFAULT_DIMENSION = new Dimension(512, 512);
  private IndexColorModel colorModel;
  private BufferedImage fractal;
  
  public FractalPanel()
  {
    this(DEFAULT_DIMENSION, getDefaultColorModel());
  }
  
  public FractalPanel(Dimension d, IndexColorModel cMod)
  {
    super(null);
    setSize(d);
    this.colorModel = cMod;
  }
  
  public void setSize(Dimension d)
  {
    super.setSize(d);
    setPreferredSize(d);
    this.fractal = new BufferedImage(d.width, d.height, 1);
    this.fractal.setAccelerationPriority(0.7F);
  }
  
  private static IndexColorModel getDefaultColorModel()
  {
    byte[] reds = new byte[101];
    byte[] greens = new byte[101];
    byte[] blues = new byte[101];
    for (int i = 0; i < reds.length - 1; i++)
    {
      int rgb = Color.HSBtoRGB(i / 100.0F, 0.6F, 1.0F);
      reds[i] = ((byte)((rgb & 0xFF0000) >> 16));
      greens[i] = ((byte)((rgb & 0xFF00) >> 8));
      blues[i] = ((byte)(rgb & 0xFF));
    }
    IndexColorModel retVal = new IndexColorModel(8, 101, reds, greens, blues);
    return retVal;
  }
  
  public boolean saveImage(SaveFormat format, String fileName)
  {
    String extension = format.name().toLowerCase();
    File outputFile = new File(fileName + "." + extension);
    try
    {
      ImageIO.write(this.fractal, extension, outputFile);
      return true;
    }
    catch (IOException e)
    {
      System.err.println("ERROR: Could not output fractal image");
      e.printStackTrace();
      System.err.println();
    }
    return false;
  }
  
  private Rectangle rect;
  
  public void paint(Graphics g)
  {
    if ((this.fractal.getWidth() != getWidth()) || (this.fractal.getHeight() != getHeight()))
    {
      Image drawMe = this.fractal.getScaledInstance(getWidth(), getHeight(), 4);
      g.drawImage(drawMe, 0, 0, Color.LIGHT_GRAY, null);
    }
    else
    {
      g.drawImage(this.fractal, 0, 0, Color.LIGHT_GRAY, null);
    }
    
    if(rect != null){
    	g.setColor(Color.RED);
    	g.draw3DRect(rect.x, rect.y, rect.width, rect.height, false);
    }
  }
  
  public void drawRectangle(Rectangle r){
	  rect = r;
	  repaint();
  }
  
  public void removeRect(){
	  rect = null;
  }
  
  public void setIndexColorModel(IndexColorModel newModel)
  {
    this.colorModel = newModel;
  }
  
  public int getImageWidth()
  {
    return this.fractal.getWidth();
  }
  
  public int getImageHeight()
  {
    return this.fractal.getHeight();
  }
  
  public void updateImage(int[][] escapeSteps)
  {
    int repeatX = this.fractal.getWidth() / escapeSteps.length;
    int r = 0;
    int[][] arrayOfInt;
    int j = (arrayOfInt = escapeSteps).length;
    for (int i = 0; i < j; i++)
    {
      int[] row = arrayOfInt[i];
      int repeatY = this.fractal.getHeight() / row.length;
      int c = 0;
      int[] arrayOfInt1;
      int m = (arrayOfInt1 = row).length;
      for (int k = 0; k < m; k++)
      {
        int value = arrayOfInt1[k];
        int rgb = this.colorModel.getRGB(value);
        for (int i1 = 0; i1 < repeatX; i1++) {
          for (int j1 = 0; j1 < repeatY; j1++) {
            this.fractal.setRGB(r + i1, c + j1, rgb);
          }
        }
        c += repeatY;
      }
      r += repeatX;
    }
    repaint();
  }
}
