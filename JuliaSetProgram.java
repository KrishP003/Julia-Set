import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.text.*;
import java.util.*;
import java.io.*;
import javax.swing.JButton;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class JuliaSetProgram extends JPanel implements AdjustmentListener, ActionListener
{

	JFrame frame;
	JButton circle, quadratic, cubic, save, clear;
	JScrollBar aBar,bBar,zoomBar,hueBar,satBar,brightBar,maxIterBar;
	JPanel scrollPanel,labelPanel,bigPanel,buttonPanel;
	JLabel aLabel,bLabel,zoomLabel,hueLabel,satLabel,brightLabel,maxIterLabel;
	float aVal, bVal, maxIterations;
	double zoom = 1.0;
	float hue, saturation, brightness;
	String option = "Circle";
	JFileChooser fileChooser;
	BufferedImage juliaImage;

	public JuliaSetProgram()
	{
		frame=new JFrame("Julia Set Program");
		frame.add(this);
		frame.setSize(1000,600);

		String currDir=System.getProperty("user.dir");
		fileChooser=new JFileChooser(currDir);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
							//orientation,starting value,doodad size,min value,max value
		aBar=new JScrollBar(JScrollBar.HORIZONTAL,0,0,-2000,2000);
		aVal=aBar.getValue() / 1000.0f;
		aBar.addAdjustmentListener(this);

		bBar=new JScrollBar(JScrollBar.HORIZONTAL,0,0,-2000,2000);
		bVal=bBar.getValue() / 1000.0f;
		bBar.addAdjustmentListener(this);

		zoomBar=new JScrollBar(JScrollBar.HORIZONTAL,10,0,0,100);
		zoom=zoomBar.getValue() / 10.0f;
		zoomBar.addAdjustmentListener(this);

		maxIterBar=new JScrollBar(JScrollBar.HORIZONTAL,300,0,10,500);
		maxIterations=maxIterBar.getValue();
		maxIterBar.addAdjustmentListener(this);

		hueBar=new JScrollBar(JScrollBar.HORIZONTAL,700,0,0,1000);
		hue=hueBar.getValue() / 1000.0f;
		hueBar.addAdjustmentListener(this);

		satBar=new JScrollBar(JScrollBar.HORIZONTAL,500,0,0,1000);
		saturation=satBar.getValue() / 1000.0f;
		satBar.addAdjustmentListener(this);

		brightBar=new JScrollBar(JScrollBar.HORIZONTAL,1000,0,0,1000);
		brightness=brightBar.getValue() / 1000.0f;
		brightBar.addAdjustmentListener(this);

		GridLayout grid=new GridLayout(10,1);
		aLabel=new JLabel("A: "+aVal);
		bLabel=new JLabel("B: "+bVal);
		zoomLabel=new JLabel("Zoom: "+zoom);
		maxIterLabel=new JLabel("Max Iterations: "+maxIterations);
		hueLabel=new JLabel("Hue: "+hue);
		satLabel=new JLabel("Saturation: "+saturation);
		brightLabel=new JLabel("Brightness: "+brightness);

		circle=new JButton("Circular");
		quadratic=new JButton("Quadratic");
		cubic=new JButton("Cubic");
		clear=new JButton("Clear");
		save=new JButton("Save");

		circle.addActionListener(this);
		quadratic.addActionListener(this);
		cubic.addActionListener(this);
		clear.addActionListener(this);
		save.addActionListener(this);

		labelPanel=new JPanel();
		labelPanel.setLayout(grid);
		labelPanel.add(aLabel);
		labelPanel.add(bLabel);
		labelPanel.add(zoomLabel);
		labelPanel.add(maxIterLabel);
		labelPanel.add(hueLabel);
		labelPanel.add(satLabel);
		labelPanel.add(brightLabel);

		scrollPanel=new JPanel();
		scrollPanel.setLayout(grid);
		scrollPanel.add(aBar);
		scrollPanel.add(bBar);
		scrollPanel.add(zoomBar);
		scrollPanel.add(maxIterBar);
		scrollPanel.add(hueBar);
		scrollPanel.add(satBar);
		scrollPanel.add(brightBar);
		scrollPanel.add(circle);
		scrollPanel.add(quadratic);
		scrollPanel.add(cubic);

		buttonPanel=new JPanel();
		buttonPanel.setLayout(grid);
		buttonPanel.add(clear);
		buttonPanel.add(save);

		bigPanel=new JPanel();
		bigPanel.setLayout(new BorderLayout());
		bigPanel.add(labelPanel,BorderLayout.WEST);
		bigPanel.add(scrollPanel,BorderLayout.CENTER);
		bigPanel.add(buttonPanel,BorderLayout.EAST);

		frame.add(bigPanel,BorderLayout.SOUTH);

		frame.setVisible(true);
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		/*g.setColor(Color.MAGENTA);
		g.setColor(new Color(red,green,blue));
		g.fillRect(0,0,frame.getWidth(),frame.getHeight());*/
		g.drawImage(drawJulia(option),0,0,null);
	}

	public BufferedImage drawJulia(/*Graphics g*/String equationType)
	{
		//put your drawJulia image code in here
		int w = frame.getWidth();
		int h = frame.getHeight();
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

		for(int x = 0; x < w; x++)
		{
			for(int y = 0; y < h; y++)
			{
			 	maxIterations = 300f;
				double zx = 1.5 * ((x - w / 2.0) / (zoom * w / 2.0));
				double zy = ((y - h / 2.0) / (zoom * h / 2.0));

				if(equationType.equalsIgnoreCase("Circle"))
				{
					while((zx * zx) + (zy * zy) < 6 && maxIterations > 0)
					{
						double temp = (zx * zx) - (zy * zy) + aVal;
						zy = 2.0 * zx * zy + bVal;
						zx = temp;
						maxIterations--;
					}
				}

				if(equationType.equalsIgnoreCase("Quadratic"))
				{
					while((zx * zx) + (zy) < 6 && maxIterations > 0)
					{
						double temp = (zx * zx) - (zy) + aVal;
						zy = (2 * zx * zy) + bVal;
						zx = temp;
						maxIterations--;
					}
				}

				if(equationType.equalsIgnoreCase("Cubic"))
				{
					while((zx * zx * zx) + (zy) < 6 && maxIterations > 0)
					{
						double temp = (zx * zx * zx) - (zy) + aVal;
						zy = (2 * zx * zy) + bVal;
						zx = temp;
						maxIterations--;
					}
				}

				int c;
				//hue 0=red 	0.33=green		0.66=blue		1=red
				if(maxIterations > 0)
					c = Color.HSBtoRGB(hue * (maxIterations / 300f) % 1, saturation, brightness);
				else
					c = Color.HSBtoRGB(hue, 1, 1);
				image.setRGB(x,y,c);
			}
		}
		juliaImage = image;
		return juliaImage;
	}

   public void actionPerformed(ActionEvent e)
   {
	   if (e.getSource() == circle)
	   	option = "Circle";
	   if (e.getSource() == quadratic)
	   	option = "Quadratic";
	   if (e.getSource() == cubic)
	   	option = "Cubic";
	   if (e.getSource() == clear)
		clearPanel();
	   if (e.getSource() == save)
		saveImage();
	   	repaint();
   }

	public void adjustmentValueChanged(AdjustmentEvent e)
	{
		if(e.getSource()==aBar)
			aVal=aBar.getValue() / 1000.0f;
			aLabel.setText("A: "+aVal);
		if(e.getSource()==bBar)
			bVal=bBar.getValue() / 1000.0f;
			bLabel.setText("B: "+bVal);
		if(e.getSource()==zoomBar)
			zoom=zoomBar.getValue() / 10.0;
			zoomLabel.setText("Zoom: "+zoom);
		if(e.getSource()==maxIterBar)
			maxIterations=maxIterBar.getValue();
			maxIterLabel.setText("Max Iterations: "+maxIterations);
		if(e.getSource()==hueBar)
			hue=hueBar.getValue() / 1000.0f;
			hueLabel.setText("Hue: "+hue);
		if(e.getSource()==satBar)
			saturation=satBar.getValue() / 1000.0f;
			satLabel.setText("Saturation: "+saturation);
		if(e.getSource()==brightBar)
			brightness=brightBar.getValue() / 1000.0f;
			brightLabel.setText("Brightness: "+brightness);
		repaint();
	}

	public void clearPanel()
	{
		option = "Circle";
		aVal= 0 / 1000.0f;
		aBar.setValue(0);

		bVal=0 / 1000.0f;
		bBar.setValue(0);

		zoom=10 / 10.0f;
		zoomBar.setValue(10);

		maxIterations= 300;
		maxIterBar.setValue(300);

		hue=700 / 1000.0f;
		hueBar.setValue(700);

		saturation=500 / 1000.0f;
		satBar.setValue(500);

		brightness=1000 / 1000.0f;
		brightBar.setValue(1000);
	}

	public void saveImage()
	{
		if(juliaImage!=null)   //juliaImage is the BufferedImage I declared globally (and used in the drawJulia method)
		{
			FileFilter filter=new FileNameExtensionFilter("*.png","png");
			fileChooser.setFileFilter(filter);
			if(fileChooser.showSaveDialog(null)==JFileChooser.APPROVE_OPTION)
			{
				File file = fileChooser.getSelectedFile();
				try
				{
					String st=file.getAbsolutePath();
					if(st.indexOf(".png")>=0)
						st=st.substring(0,st.length()-4);
					ImageIO.write(juliaImage,"png",new File(st+".png"));
				}catch(IOException e)
				{
				}

			}
		}
	}

	public static void main(String[] args)
	{
		JuliaSetProgram app=new JuliaSetProgram();
	}

}