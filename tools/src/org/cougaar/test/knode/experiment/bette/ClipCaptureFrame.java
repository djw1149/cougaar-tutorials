package org.cougaar.test.knode.experiment.bette;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.DecimalFormat;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.cougaar.core.service.BlackboardService;

public class ClipCaptureFrame extends JFrame {
    private static final DecimalFormat f2_1 = new DecimalFormat("0.0");
    private static final DecimalFormat f3_0 = new DecimalFormat("000");
    private static final DateFormat dateFormatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.MEDIUM);
    private int frameWidth;
    private int frameHeight;
    private int imageWidth;
    private int imageHeight;
    private int xPos,yPos;
    private JLabel imgLabel;
    private JLabel timeLabel;
    private JButton captureButton;
    private JButton sendButton;
    private JButton clearButton;
    private boolean showSlides = true;
    private Quitable client;
    private BlackboardService blackBoard;
    private ClipCaptureState captureState;

	 
	 ClipCaptureFrame(String title,  
	                  String[] args, 
	                  Quitable client, 
	                  BlackboardService blackBoard,
	                  ClipCaptureState captureState)
	    {
		super(title);

		this.client = client;
		this.blackBoard=blackBoard;
		this.captureState = captureState;
		
		// Defaults
		frameWidth = 250;
		frameHeight = 250;
		imageWidth = 200;
		imageHeight = 200;
		xPos= 20;
		yPos= 20;
		showSlides = true;

		for (int i=0; i < args.length; i++) {
		    String arg = args[i];
		    if (arg.equals("-frame-width")) {
			frameWidth = Integer.parseInt(args[++i]);
		    } else if (arg.equals("-frame-height")) {
			frameHeight = Integer.parseInt(args[++i]);
		    } else if (arg.equals("-image-width")) {
			imageWidth = Integer.parseInt(args[++i]);
		    } else if (arg.equals("-image-height")) {
			imageHeight = Integer.parseInt(args[++i]);
		    } else if (arg.equals("-x-position")) {
		        xPos = Integer.parseInt(args[++i]);
		    } else if (arg.equals("-y-position")) {
		        yPos = Integer.parseInt(args[++i]);
		    } else if (arg.equals("-show-slides")) {
			showSlides = (args[++i].equalsIgnoreCase("true"));
		    }
		}

		// Make nice quit
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
			    ClipCaptureFrame.this.client.quit();
			}
		    });

		getContentPane().setLayout(new BorderLayout());
		addPictureArea();
		pack();

		setSize(frameWidth, frameHeight);
		setLocation(xPos, yPos);
		// This is being called in a setup subscription (no transaction needed)
		blackBoard.publishAdd(captureState);
	    }

	 
	    void quit() {
		dispose();
	    }



	    private void addPictureArea() {
	        GridBagLayout bag = new GridBagLayout();
	        JPanel imgPanel = new JPanel();
	        imgPanel.setLayout(bag);
	        GridBagConstraints c = new GridBagConstraints();
	        c.insets = new Insets(5,5,5,5);
	        c.fill = GridBagConstraints.BOTH;
	        
	        imgLabel = new JLabel();
		imgLabel.setSize(new Dimension(imageWidth, imageHeight));
		bag.setConstraints(imgLabel, c);
		imgPanel.add(imgLabel);
		
		c.gridwidth = GridBagConstraints.REMAINDER; // Make new row after this cell
		captureButton = new JButton("Capture");
		bag.setConstraints(captureButton, c);
                imgPanel.add(captureButton);
		
		timeLabel = new JLabel();
		timeLabel.setSize(new Dimension(50, 20));
		timeLabel.setText("Waiting for Image");
		bag.setConstraints(timeLabel, c);
		imgPanel.add(timeLabel);
		
		c.gridwidth = GridBagConstraints.REMAINDER; // Make new row after this cell
		sendButton = new JButton("Send");
		bag.setConstraints(sendButton, c);
		imgPanel.add(sendButton);

		c.gridwidth = GridBagConstraints.REMAINDER; // Make new row after this cell
		clearButton = new JButton("Clear");
		bag.setConstraints(clearButton, c);
		imgPanel.add(clearButton);
		
		setupButtonCallbacks();

		getContentPane().add(imgPanel, "Center");
		
		
	    }

            private void setupButtonCallbacks() {
                          
        }

	    void update(byte[] pixels, long count) {
		if (pixels == null) {
		    return;
		} else if (showSlides) {
		    imgLabel.setIcon(new ImageIcon(pixels));
		    timeLabel.setText(dateFormatter.format(count) + " " +
		                      f3_0.format(count %1000) + "ms");
		} else {
		    imgLabel.setText(Long.toString(count));
		    timeLabel.setText(f2_1.format((count%100000)/1000.0));
		}
		resize();
	    }

	    // We're not using this at the moment
	    private void resize() {
		Dimension now = getSize();
		Dimension pref = getPreferredSize();
		int newWidth = now.width;
		int newHeight = now.height;
		boolean change = false;
		if (now.width != pref.width) {
		    change = true;
		    newWidth = pref.width;
		}
		if (now.height != pref.height) {
		    change = true;
		    newHeight = pref.height;
		}
		if (change) {
		    setSize(new Dimension(newWidth, newHeight));
		    setLocation(xPos, yPos);
		}
	    }



}