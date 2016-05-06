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
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
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

    public void handleOpenFileButtonAction(ActionEvent actionEvent) {
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
                //throw new FileNotFoundException();
            }
        }
    }

    public void handleRunButtonAction(ActionEvent actionEvent) {
        CascadeClassifier faceDetector = new CascadeClassifier(getClass().getResource("/haarcascade_frontalface_alt.xml").getFile().substring(1));

        byte[] data = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
        Mat mat = new Mat(bufferedImage.getHeight(), bufferedImage.getWidth(), CvType.CV_8UC3);
        mat.put(0, 0, data);

        // Detect faces in the image.
        // MatOfRect is a special container class for Rect.
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(mat, faceDetections);
        System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));

        // Draw a bounding box around each face.
        for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(mat, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 3);
        }

        // Converting BGR to RGB
        Mat mat1 = new Mat(bufferedImage.getHeight(), bufferedImage.getWidth(), CvType.CV_8UC3);
        Imgproc.cvtColor(mat, mat1, Imgproc.COLOR_RGB2BGR);

        // Converting Mat to BufferedImage
        data = new byte[bufferedImage.getHeight() * bufferedImage.getWidth() * (int) mat.elemSize()];
        mat1.get(0, 0, data);

        BufferedImage resultBufferedImage = new BufferedImage(mat.cols(), mat.rows(), BufferedImage.TYPE_3BYTE_BGR);
        resultBufferedImage.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), data);

        Image image = SwingFXUtils.toFXImage(resultBufferedImage, null);
        imageView1.setImage(image);
    }
}
