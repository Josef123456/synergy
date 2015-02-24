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

	private ArrayList<Photo> photos;
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

        tagPanel = new TagPanel(photos, this);
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

    public void setImportedImages() {
        mainThumbnailPanel.setLayout(new GridLayout(0, 1));
        setImagesToPanel(mainThumbnailPanel, 120, 120);
        setImagesToPanel(gridPanel, 200, 200);
        if (photos.size() > 0) {
            setMainImagePanel(photos.get(photos.size ()-1).getPath ());
        }
        mainThumbnailPanel.updateUI();
        gridPanel.updateUI();
    }

    public void setMainImagePanel(String fileName) {
        if (fileName == null) {
            mainImage.setText("Please import files");
        } else {
            ImageIcon pic1Icon = new ImageIcon(fileName);
            Image pic1img = pic1Icon.getImage();
            Image newImg = pic1img.getScaledInstance(mainImage.getWidth(), mainImage.getHeight(),
                    java.awt.Image.SCALE_SMOOTH);
            pic1Icon = new ImageIcon(newImg);
            mainImage.setIcon(pic1Icon);
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

	private void addMouseListenerToPictureLabel(final JLabel pic, final int currentIndex, final Graphics g,
	                                            final int width, final int height) {
		pic.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				setMainImagePanel(photos.get(currentIndex).getPath ());
				selectedIndexes.add (currentIndex);
				tagPanel.update ();

				if ( isMainView == false ) {
					if ( !selectedIndexes.contains (currentIndex) ) {
						selectedIndexes.add (currentIndex);
						System.out.println ("Selected Index List: " + selectedIndexes);
						g.drawImage (finalCheckBoxImage, 0, 0, 50, 50, null);
						g.dispose ();
						tagPanel.update ();
						pic.repaint ();
					} else {
						selectedIndexes.remove (currentIndex);
						System.out.println ("Selected Index List: " + selectedIndexes);
						tagPanel.update ();
						pic.repaint ();

					}
					if ( arg0.getClickCount () == 2 ) {
						mainPanel.setVisible (true);
						mainGridPanel.setVisible (false);
						isMainView = true;
					}
				}
			}
		});
	}

    public void setImagesToPanel(JPanel panel, int imageWidth, int imageHeight) {
        final int width = imageWidth;
        final int height = imageHeight;

        for (int i = 0; i < photos.size(); i++) {
            final int currentIndex = i;

            final JLabel pic = new JLabel();

            File imageFile = new File(photos.get(i).getPath ());
            BufferedImage bufferedImage = null;
            try {
                bufferedImage = ImageIO.read(imageFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            final BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_BGR);
            Graphics g = image.getGraphics();
            g.drawImage(bufferedImage, 0, 0, imageWidth, imageHeight, null);
            g.dispose();
            image.flush();
	        addMouseListenerToPictureLabel(pic, currentIndex,g, imageWidth, imageHeight);


            pic.setIcon(new ImageIcon(photos.get(i).getPath ()));
            panel.add(pic);
        }
    }

    public TagPanel getTagPanel() {
        return tagPanel;
    }

	public void setPhotos (ArrayList<Photo> photos) {
		this.photos = photos;
	}

    public void setIsMainView(boolean b) {
        if (isMainView && !b) {
            mainGridPanel.setVisible(true);
            mainPanel.setVisible(false);
            isMainView = false;
            tagPanel.update ();
        }
        if (!isMainView && b) {
            mainPanel.setVisible(true);
            mainGridPanel.setVisible(false);
            isMainView = true;
            tagPanel.update ();
        }
    }

	public Integer[] getSelectedIndexesAsArray () {
		return selectedIndexes.toArray (new Integer[ selectedIndexes.size () ]);
	}
}





