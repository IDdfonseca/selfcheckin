package com.idlogistics.selfcheckin.scandit;

import androidx.annotation.VisibleForTesting;

import com.scandit.datacapture.core.capture.DataCaptureContext;
import com.scandit.datacapture.core.source.Camera;
import com.scandit.datacapture.id.capture.DriverLicense;
import com.scandit.datacapture.id.capture.IdCapture;
import com.scandit.datacapture.id.capture.IdCaptureDocument;
import com.scandit.datacapture.id.capture.IdCaptureSettings;
import com.scandit.datacapture.id.capture.IdCard;
import com.scandit.datacapture.id.capture.Passport;
import com.scandit.datacapture.id.capture.SingleSideScanner;
import com.scandit.datacapture.id.data.IdCaptureRegion;

import java.util.Arrays;
import java.util.List;

/*
 * Initializes DataCapture.
 */
public final class DataCaptureManager {

    @VisibleForTesting
    public static String SCANDIT_LICENSE_KEY = "AgSVxSIHBLRtCK1RStHT+W8Wrwi9AW2iYgJykjco1xcBb1ohvVUKRpNC9UlvfFwrpCvH139LDiGNWW0B4XQ5zYl2a7+3ENBl+2t7Welt3ijYNr0hSHwDtF5nBPm9YPA/D0asgHRwX+v5UHm5OCM47OUyZVThQnE/XUUJw2dQ4tYTEWzEOGjuOOh3MQkzV1baiCmAbNdsVHEzfTHT5GVrk7QWwPolDlljhGsjGhR7IxV9esmu9Tm5L19uxipiN7ZU1HYXIoVcyBiIcDnMiyG6XD8fJ/4dPPWR0FzsMkx6HKYuT/Kt62JACF9R9iMQUyO7N3NxOHpwJywycq18imZu7x5iiuctDV1TA0B8XYI7keakV2KqhHy/SI8UXhIzaHIWb0Zn+z5ijGI1FEf04iWiKcJuj3mDeW/3DFPbvd1wMy9CBd/8wztl4wpeyjWkRpqxSizUSDxp+PXQE+Yn+VpVMfZ1x7X6d0TfrXf3Ua5kPzIhWfe3E0IsZLF0KvAhe4nTphxhFsFdyVjsWvZ1yECKDBdqpfayW+46E0eFKOFdkqBmRF1j0222Kb52W41RZlQl0HUiyFhvi/4zJ7Tf6yv5PWZY5z7wcYuJPGUrDiRV8wt7b3Cj3WQN/FsHvu0if3263lIWOIoSoGMHUHM8ZD7Suk9vGa86BPHS+Wrag7cd9ywKcbjDFhTzb61I5kPef7Dyb2DtoS1CLXvELEwxMjiWJn1K/4LIWbvhB1ENmmYHBqE2Vga3UAb8e4wMi69/Nc8tm1tGKrtjkYmJUnQK7w17mx9xeOrDc+sZ+mEKPBlPXFX0K0vTBkUw6fx1j8wmGLgmE0tAhYRUb3mnRUtIOz+ElCMz+xvERyxJr0oRpekt2S+BEYQNgHRuaNRwq6qXZldjbXmgxihYQz1YbXY2mQl9yYxv2AAaWe4ZUmMX1Ld7B5smFP0uhktfqtlSTEKKGMmZoC8rWC8D/KKdTreSfSiVTJ5Bn18XbwKMJXQOKBBGON9LYLTdW3ynZNVNp9TFcvWdTDOcy16Vkj3/hNtJVY6x26OEWC7Vfr3LACVs0lkAez3FeK2UwxegHPtjLyrQTM4EZ4JArK8eYO28V6+T4fORdR5ejVFHtiXpixJ+85920FlVsrRAN0jjW0L0+H1SXlo7kaPTkQzXLCRyDfA16wjViH/sHbPqJCj4NVOj8msnHPcHJT5kG9bCor0oLhOVrOUBmkz9tWuB6biCM/zIl5sGQeRcOY/w+49j64hMPsoKVIAcHUh4lb9womo4MTWYYDAmK5iLFINJVVBSAPt6Bk1SMWQJwyUw1+H+TZr/csfCr6j7QXI1MrCpD0Ry/8S+x++GJg/Ppxe6OdHEIk/uwHlB6Q1NbFtOgj62bd15brSSqx5KA73WZUqXO80hy683inXlV5115/hkA8Ic51S6PA5/dsEAVbUOI68CqcrnR0iJdp8JQkCAyJOWFyiZhwwx30Ac4QdqJ6N4mOKp8yOJ0ZBzDU/hy1TlYSKqdu7EypdLt2YZh6825RrfrKP4IAJQ6pqrZZMSqUP7jQ5kN4YZIYxhUXonvAkC5CUmenROjona1F1R7SVufUGolLpQES07YxbOKwXlYTtq6ssMBrkBBTRo83xc5rH4dTPTOR4mxeD1flkQLzaBeIsE86UfZX0paNsP1B6ZuOZhZAKn8uOwUdRUfZjJ728BfBX9ueWV0SK1Rtg1rto=";

    private static DataCaptureManager INSTANCE;

    private static final List<IdCaptureDocument> ACCEPTED_DOCUMENTS = Arrays.asList(
            new IdCard(IdCaptureRegion.BRAZIL),
            new DriverLicense(IdCaptureRegion.BRAZIL),
            new Passport(IdCaptureRegion.BRAZIL)
    );

    private final DataCaptureContext dataCaptureContext;
    private Camera camera;
    private IdCapture idCapture;

    public static DataCaptureManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataCaptureManager();
        }

        return INSTANCE;
    }

    private DataCaptureManager() {
        /*
         * Create DataCaptureContext using your license key.
         */
        dataCaptureContext = DataCaptureContext.forLicenseKey(SCANDIT_LICENSE_KEY);

        initCamera();
        initIdCapture();
    }

    private void initCamera() {
        /*
         * Set the device's default camera as DataCaptureContext's FrameSource. DataCaptureContext
         * passes the frames from it's FrameSource to the added modes to perform capture or
         * tracking.
         *
         * Since we are going to perform TextCapture in this sample, we initiate the camera with
         * the recommended settings for this mode.
         */

        //CAMERA FRONTAL
        //camera = Camera.getCamera(CameraPosition.USER_FACING, IdCapture.createRecommendedCameraSettings());
        //

        camera = Camera.getDefaultCamera(IdCapture.createRecommendedCameraSettings());

        if (camera == null) {
            throw new IllegalStateException("Failed to init camera!");
        }

        dataCaptureContext.setFrameSource(camera);
    }

    private void initIdCapture() {
        /*
         * Create a mode responsible for recognizing documents. This mode is automatically added
         * to the passed DataCaptureContext.
         */
        IdCaptureSettings settings = new IdCaptureSettings();

        // Recognize national ID cards, driver's licenses and passports.
        settings.setAcceptedDocuments(ACCEPTED_DOCUMENTS);
        settings.setScannerType(new SingleSideScanner(true, true, true));

        idCapture = IdCapture.forDataCaptureContext(dataCaptureContext, settings);
    }

    public DataCaptureContext getDataCaptureContext() {
        return dataCaptureContext;
    }

    public Camera getCamera() {
        return camera;
    }

    public IdCapture getIdCapture() {
        return idCapture;
    }
}
