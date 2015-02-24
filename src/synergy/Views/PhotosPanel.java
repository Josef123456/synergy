package synergy.Views;


import synergy.models.Photo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;


public class PhotosPanel extends JPanel {
	private JPanel mainPanel;
	private JPanel mainImagePanel;
	private JPanel mainThumbnailPanel;
	private JLabel mainImage;
	private JScrollPane gridPanelPane;

    private JPanel mainGridPanel;
	private JPanel gridPanel;

    final File checkBoxFile = new File("check box icon.png");

    private TagPanel tagPanel;

    boolean isMainView;

	private ArrayList<Photo> photos = new ArrayList<> ();
	private Set<Integer> selectedIndexes = new HashSet<> ();

	private BufferedImage finalCheckBoxImage = null;

	public PhotosPanel() {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mainImage = new JLabel();
        mainImage.setPreferredSize(new Dimension(600, 480));
        isMainView = true;

        setGridImages();
        mainGridPanel.setVisible(false);

        setUpMainFrame();
        setMainImagePanel(null);

        tagPanel = new TagPanel(this);
        tagPanel.setPreferredSize(new Dimension(200, 480));
        add(tagPanel, BorderLayout.EAST);
        setVisible(true);

		try {
			finalCheckBoxImage = ImageIO.read(checkBoxFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

    public void setUpMainFrame() {
        mainPanel = new JPanel();
        mainImagePanel = new JPanel();
        mainImagePanel.add(mainImage);

        mainThumbnailPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(mainThumbnailPanel);
        scrollPane.setPreferredSize(new Dimension(120, 480));
        mainThumbnailPanel.setLayout(new GridLayout(2, 1));

        mainPanel.add(scrollPane, BorderLayout.WEST);
        mainPanel.add(mainImagePanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void setImportedImages() {
        mainThumbnailPanel.setLayout(new GridLayout(0, 1));
        setImagesToPanel(mainThumbnailPanel, 120, 120);
        setImagesToPanel(gridPanel, 200, 200);
        if (photos.size() > 0) {
            setMainImagePanel(photos.get(photos.size ()-1).getPath ());
	        selectedIndexes.add(photos.size()-1);
	        tagPanel.update();
        }
        mainThumbnailPanel.updateUI();
        gridPanel.updateUI();
    }

    public void setMainImagePanel(String fileName) {
        if (fileName == null) {
            mainImage.setText("Please import files");
        } else {
	        long t1 = System.currentTimeMillis();
            ImageIcon pic1Icon = new ImageIcon(fileName);
            Image pic1img = pic1Icon.getImage();
            Image newImg = pic1img.getScaledInstance(640, 480,java.awt.Image.SCALE_SMOOTH);
            pic1Icon = new ImageIcon(newImg);
            mainImage.setIcon (pic1Icon);
	        mainImage.setText("");
	        long t2 = System.currentTimeMillis();
	        System.out.println("For loading main image: " +
			        (t2 - t1) + " milliseconds" +
			        "{" + 640 + ", " + 480 + "}");
        }
    }

    public void setGridImages() {
        mainGridPanel = new JPanel();
        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(0, 3, 20, 20));
        setImagesToPanel(gridPanel, 200, 200);
        gridPanelPane = new JScrollPane(gridPanel);
        gridPanelPane.setPreferredSize(new Dimension(720, 480));
        mainGridPanel.add(gridPanelPane, BorderLayout.CENTER);
        add(mainGridPanel, BorderLayout.CENTER);
        mainGridPanel.updateUI();
    }

	private void addMouseListenerToPictureLabel(final JLabel pic, final int currentIndex, final Graphics g) {
		pic.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				setMainImagePanel(photos.get(currentIndex).getPath ());
				if ( isMainView ) {
					selectedIndexes.removeAll (selectedIndexes);
					selectedIndexes.add (currentIndex);
				}

				if ( isMainView == false ) {
					System.out.println(currentIndex);
					if ( !selectedIndexes.contains (currentIndex) ) {
						selectedIndexes.add (currentIndex);
						System.out.println ("Adding Selected Index List: " + selectedIndexes);
						g.drawImage (finalCheckBoxImage, 0, 0, 50, 50, null);
						g.dispose ();
						tagPanel.update ();
						pic.repaint ();
						pic.setText ("selected");
					} else {
						selectedIndexes.remove (currentIndex);
						System.out.println ("Removing Selected Index List: " + selectedIndexes);
						tagPanel.update ();
						pic.repaint ();
						pic.setText ("");
					}
					if ( arg0.getClickCount () == 2 ) {
						mainPanel.setVisible (true);
						mainGridPanel.setVisible (false);
						isMainView = true;
					}
				}
				tagPanel.update ();
			}
		});
	}

    public void setImagesToPanel(final JPanel panel, final int imageWidth, final int imageHeight) {

	    //TODO: refactor this mess
        for (int i = 0; i < photos.size(); ++i) {
	        final int currentIndex = i;
	        // TODO: get this into it's own class
	        Runnable runnable = new Runnable () {
		        @Override
		        public void run () {
			        final int index = currentIndex;
			        final JLabel pic = new JLabel ();
			        long t1 = System.currentTimeMillis ();
			        String fileName = photos.get (index).getPath ();
			        ImageIcon pic1Icon = new ImageIcon (fileName);
			        Image pic1img = pic1Icon.getImage ();
			        Image newImg = pic1img.getScaledInstance (imageWidth, imageHeight, java.awt.Image.SCALE_SMOOTH);
			        final BufferedImage finalImage = new BufferedImage (imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
			        Graphics g = finalImage.getGraphics ();
			        g.drawImage (newImg, 0, 0, imageWidth, imageHeight, null);
			        g.dispose ();
			        finalImage.flush ();
			        long t2 = System.currentTimeMillis ();
			        System.out.println ("For loading grid image (" + index + "):" +
					        (t2 - t1) + " milliseconds" +
					        "{" + imageHeight + ", " + imageWidth + "}");
			        addMouseListenerToPictureLabel (pic, index, g);
			        pic.setIcon (new ImageIcon (newImg));
			        panel.add (pic);
		        }
	        };
	        //TODO: god please...
	        new Thread(runnable).start ();

        }
    }

	public void setPhotos (ArrayList<Photo> photos) {
		this.photos = photos;
		setImportedImages ();
	}

    public void setIsMainView(boolean b) {
        if (isMainView && !b) {
            mainGridPanel.setVisible(true);
            mainPanel.setVisible(false);
            isMainView = false;
        }
        if (!isMainView && b) {
            mainPanel.setVisible(true);
            mainGridPanel.setVisible(false);
            isMainView = true;
        }
	    tagPanel.update ();
    }

	public ArrayList<Photo> getPhotos () {
		return photos;
	}

	public void setNoSelection() {
		selectedIndexes.removeAll (selectedIndexes);
	}

	public Integer[] getSelectedIndexesAsArray () {
		return selectedIndexes.toArray (new Integer[ selectedIndexes.size () ]);
	}
}





