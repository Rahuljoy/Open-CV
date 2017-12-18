package sample;

import javafx.application.Platform;
import javafx.css.Size;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Controller implements Initializable {

    @FXML
    private Label statusLabel;

    @FXML
    private ImageView originalImageView;

    @FXML
    private ImageView modifiedImageView;

    private static VideoCapture videoCapture;

    public static VideoCapture getVideoCapture() {

        return videoCapture;
    }

    private static ScheduledExecutorService scheduledExecutorService;

    public static ScheduledExecutorService getScheduledExecutorService() {

        return scheduledExecutorService;
    }

    private Mat currentFrame;
    private Mat previousFrame;
    private Mat modifiedFrame;
    private Image image;
    private MatOfByte matOfByte;
    private List<Integer> rowList;
    private List<Integer> columnList;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        startCamera();

    }

    private void startCamera() {
        currentFrame = new Mat();
        previousFrame = new Mat();
        modifiedFrame = new Mat();
        matOfByte = new MatOfByte();

        videoCapture = new VideoCapture();
        videoCapture.open(0);

        startFrameGrabbing();
    }

    private void grabFrame()  {
        videoCapture.read(previousFrame);
        Imgcodecs.imencode(".png", previousFrame, matOfByte);
        image = new Image(new ByteArrayInputStream(matOfByte.toArray()));
        Platform.runLater(() -> originalImageView.setImage(image));

        videoCapture.read(currentFrame);
        Imgcodecs.imencode(".png", currentFrame, matOfByte);
        image = new Image(new ByteArrayInputStream(matOfByte.toArray()));

        Core.absdiff(currentFrame, previousFrame, modifiedFrame);
        Imgproc.GaussianBlur(modifiedFrame, modifiedFrame,new org.opencv.core.Size(3,3), 0);
        Imgproc.threshold(modifiedFrame, modifiedFrame, 50, 255, Imgproc.THRESH_BINARY);

        Imgproc.cvtColor(modifiedFrame, modifiedFrame, Imgproc.COLOR_RGB2GRAY);

        for(int row=0; row<modifiedFrame.height(); row++){
            for(int col=0; col<modifiedFrame.width(); col++){
                double [] a = modifiedFrame.get(row, col);

                if(a[0] > 127){
                    double data[] = {0,0,0};
                    currentFrame.put(row, col, data);
                   rowList = new ArrayList<>();
                   columnList = new ArrayList<>();
                   floodFill(modifiedFrame, row, col, a[0]);
                }
            }
        }

       /*Imgproc.rectangle(currentFrame, new Point(rowList.get(0),rowList.get(rowList.size()-1)),new Point(columnList.get(0)
               ,columnList.get(columnList.size()-1))
              , new Scalar(255,0,0));*/

        Imgcodecs.imencode(".png", currentFrame, matOfByte);
        image = new Image(new ByteArrayInputStream(matOfByte.toArray()));
        Platform.runLater(() -> modifiedImageView.setImage(image));

    }

    private void floodFill(Mat frame, int x, int y, double prevColor){

        if(x <0 || x >=480 || y<0 || y>=640)
            return;

        double a[] = frame.get(x, y);
        if(a[0] != prevColor)
            return;

        rowList.add(x);
        columnList.add(y);

        floodFill(frame, x+1, y, prevColor);
        floodFill(frame, x-1, y, prevColor);
        floodFill(frame, x, y+1, prevColor);
        floodFill(frame, x, y-1, prevColor);

    }

    private void startFrameGrabbing() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(() -> grabFrame(), 0, 33, TimeUnit.MILLISECONDS);

    }
}
