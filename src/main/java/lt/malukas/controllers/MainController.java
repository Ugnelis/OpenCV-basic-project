package main.java.lt.malukas.controllers;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    ImageView imageView1;

    @FXML
    Button openFileButton;

    @FXML
    Button runButton;

    private BufferedImage bufferedImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("View is now loaded!");
    }

    public void handleOpenFileButtonAction(ActionEvent actionEvent) throws FileNotFoundException {
        Node node = (Node) actionEvent.getSource();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File file = fileChooser.showOpenDialog(node.getScene().getWindow());

        if (file != null) {
            try {
                bufferedImage = ImageIO.read(file);
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                imageView1.setImage(image);

                runButton.setDisable(false);
            } catch (IOException ex) {
                throw new FileNotFoundException();
            }
        }
    }

    public void handleRunButtonAction(ActionEvent actionEvent) {

        byte[] data = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
        Mat mat = new Mat(bufferedImage.getHeight(), bufferedImage.getWidth(), CvType.CV_8UC3);
        mat.put(0, 0, data);

        Mat mat1 = new Mat(bufferedImage.getHeight(), bufferedImage.getWidth(), CvType.CV_8UC3);
        Imgproc.cvtColor(mat, mat1, Imgproc.COLOR_RGB2HSV);

        byte[] data1 = new byte[mat1.rows() * mat1.cols() * (int) (mat1.elemSize())];
        mat1.get(0, 0, data1);
        BufferedImage bufferedImage1 = new BufferedImage(mat1.cols(), mat1.rows(), 5);
        bufferedImage1.getRaster().setDataElements(0, 0, mat1.cols(), mat1.rows(), data1);

        Image image = SwingFXUtils.toFXImage(bufferedImage1, null);
        imageView1.setImage(image);
    }
}
