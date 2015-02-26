package synergy.Views;


import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;

import synergy.models.Photo;


public class PhotosPanel extends JPanel {
    private JPanel mainPanel;
    private JPanel mainImagePanel;
    private ThumbnailView mainThumbnailPanel;
    private JLabel mainImage;

    private JPanel mainGridPanel;
    private PhotoGridView gridPanel;

    final File checkBoxFile = new File("check box icon.png");
    final Border greenBorder = BorderFactory.createLineBorder(Color.green);


    private TagPanel tagPanel;

    boolean isMainView;

    private ArrayList<Photo> photos = new ArrayList<>();
    private Set<Integer> selectedIndexes = new HashSet<>();

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
        mainPanel.setLayout(new BorderLayout());
        mainImagePanel = new JPanel();
        mainImagePanel.add(mainImage);

        mainThumbnailPanel = new ThumbnailView(this);

        mainThumbnailPanel.setLayout(new GridLayout(1, 0));

        mainPanel.add(mainImagePanel, BorderLayout.CENTER);
        mainPanel.add(mainThumbnailPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void setImportedImages() {

        mainThumbnailPanel.setLayout(new GridLayout(1, 0));
        mainThumbnailPanel.getMainPanel().removeAll();

        gridPanel.getMainPanel().removeAll();
        gridPanel.getZoomedOutPanel().removeAll();

        mainThumbnailPanel.setMainImageToPanel(120, 120);
        gridPanel.setMainImageToPanel(200, 200);
        gridPanel.setGridImageSize(50, 50);

        if (photos.size() > 0) {
            setMainImagePanel(photos.get(photos.size() - 1).getPath());
            selectedIndexes.removeAll(selectedIndexes);
            selectedIndexes.add(photos.size() - 1);
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
            Image newImg = pic1img.getScaledInstance(640, 480, java.awt.Image.SCALE_SMOOTH);
            pic1Icon = new ImageIcon(newImg);
            mainImage.setIcon(pic1Icon);
            mainImage.setText("");
            long t2 = System.currentTimeMillis();
            System.out.println("For loading main image: " +
                    (t2 - t1) + " milliseconds" +
                    "{" + 640 + ", " + 480 + "}");
        }
    }

    public void setGridImages() {
        mainGridPanel = new JPanel();
        gridPanel = new PhotoGridView(this);
        gridPanel.getMainPanel().setLayout(new GridLayout(0, 3, 5, 5));
        mainGridPanel.add(gridPanel, BorderLayout.CENTER);
        add(mainGridPanel, BorderLayout.CENTER);
    }

    public void addMouseListenerToPictureLabel(final JLabel pic, final int currentIndex, final
    Image finalImage, final int imageWidth, final int imageHeight) {
        pic.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent arg0) {
                setMainImagePanel(photos.get(currentIndex).getPath());
                if (isMainView) {
                    selectedIndexes.removeAll(selectedIndexes);
                    selectedIndexes.add(currentIndex);
                }

                if (isMainView == false) {
                    System.out.println(currentIndex);
                    if (!selectedIndexes.contains(currentIndex)) {
                        selectedIndexes.add(currentIndex);
                        System.out.println("Adding Selected Index List: " + selectedIndexes);
                        /*final BufferedImage finalBufferedImage = new BufferedImage(imageWidth,
                                imageHeight, BufferedImage.TYPE_INT_RGB);
                        Graphics g = finalBufferedImage.getGraphics();
                        g.drawImage(finalImage, 0, 0, imageWidth, imageHeight, null);
                        g.drawImage(finalCheckBoxImage, 0, 0, 50, 50, null);
                        g.dispose();
                        pic.setIcon(new ImageIcon(finalBufferedImage));
                        finalBufferedImage.flush();*/
                        pic.setBorder(greenBorder);
                        tagPanel.update();
                        pic.repaint();
                    } else {
                        selectedIndexes.remove(currentIndex);
                        System.out.println("Removing Selected Index List: " + selectedIndexes);
                        /*final BufferedImage finalBufferedImage = new BufferedImage(imageWidth,
                                imageHeight, BufferedImage.TYPE_INT_RGB);
                        Graphics g = finalBufferedImage.getGraphics();
                        g.drawImage(finalImage, 0, 0, imageWidth, imageHeight, null);
                        g.dispose();
                        pic.setIcon(new ImageIcon(finalBufferedImage));
                        finalBufferedImage.flush();*/
                        pic.setBorder(null);
                        tagPanel.update();
                        pic.repaint();
                    }
                    if (arg0.getClickCount() == 2) {
                        mainPanel.setVisible(true);
                        mainGridPanel.setVisible(false);
                        isMainView = true;
                    }
                }
                tagPanel.update();
            }
        });
    }

    public void setImagesToPanel(final JPanel panel, final int imageWidth, final int imageHeight) {
        //TODO: refactor this mess
        for (int i = 0; i < photos.size(); ++i) {
            final int currentIndex = i;
            // TODO: get this into it's own class
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    final int index = currentIndex;
                    final JLabel picLabel = new JLabel();
                    String fileName = photos.get(index).getPath();
                    ImageIcon pic1Icon = new ImageIcon(fileName);
                    Image pic1img = pic1Icon.getImage();
                    Image newImg = pic1img.getScaledInstance(imageWidth, imageHeight, java.awt
                            .Image.SCALE_SMOOTH);
                    addMouseListenerToPictureLabel(picLabel, index, newImg, imageWidth, imageHeight);
                    picLabel.setIcon(new ImageIcon(newImg));
                    panel.add(picLabel);
                }
            };
            //TODO: god please...
            new Thread(runnable).start();
        }
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = (ArrayList<Photo>) photos;
        setImportedImages();
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
        tagPanel.update();
    }

    public ArrayList<Photo> getPhotos() {
        return photos;
    }

    public void setNoSelection() {
        selectedIndexes.removeAll(selectedIndexes);
    }

    public Integer[] getSelectedIndexesAsArray() {
        return selectedIndexes.toArray(new Integer[selectedIndexes.size()]);
    }
}





