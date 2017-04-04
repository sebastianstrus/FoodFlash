package iths.com.food.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;


public class MyCamera {

    public static final int CAMERA_REQUEST_CODE = 1;
    private static final String TAG = "MY_CAMERA";
    private Uri photoFilePath;
    private Context context;

    public MyCamera(Context context) {
        this.context = context;
    }

    /**
     * To be called when the user clicks the camera button.
     * Creates a photo file and starts the camera.
     */
    public void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(context.getPackageManager()) != null) {
            File photo = createPhotoFile();
            photoFilePath = Uri.fromFile(photo);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoFilePath);
            ((Activity) context).startActivityForResult(intent, CAMERA_REQUEST_CODE);
        }
    }


    /**
     * Should be called in onActivityResult. Creates and saves a bitmap with the approximate size
     * specified in the first two arguments, plus a thumbnail.
     * @param imageViewHeight Height of the image view where the image will be shown.
     * @param imageViewWidth Width of the image view where the image will be shown.
     * @param thumbnailHeight Desired height of thumbnail.
     * @param thumbnailWidth Desired width of thumbnail.
     * @return The bitmap that is created, rotated if necessary.
     */
    public Bitmap createImageBitmap(int imageViewHeight, int imageViewWidth, int thumbnailHeight, int thumbnailWidth) {

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(photoFilePath.getPath(), opt);

        int scaleFactor = 1;
        try {
            scaleFactor = Math.min(opt.outHeight / imageViewHeight, opt.outWidth / imageViewWidth);
        } catch(Exception e) {
            Log.d(TAG, "Could not scale image.");
        }

        opt = new BitmapFactory.Options();
        opt.inSampleSize = scaleFactor;

        int currentOrientation = getOrientation(photoFilePath.getPath());

        Bitmap bitmap = BitmapFactory.decodeFile(photoFilePath.getPath(), opt);
        Bitmap rotatedBitmap = rotateImage(bitmap, currentOrientation);
        saveImage(rotatedBitmap, photoFilePath.getPath());

        Bitmap thumbnail = makeThumbnail(photoFilePath.getPath(), thumbnailHeight, thumbnailWidth);
        saveImage(thumbnail, getThumbnailFilePath(photoFilePath.getPath()));

        return rotatedBitmap;
    }

    public Uri getPhotoFilePath() {
        return photoFilePath;
    }

    /**
     * Get the file path to a thumbnail for a photo.
     * @param filePath The file path to the large-size photo.
     * @return The file path to the thumbnail.
     */
    static String getThumbnailFilePath(String filePath) {
        String newFilePath = filePath.substring(0, filePath.length() - 4) + "m.jpg";
        return newFilePath;
    }

    /**
     * Returns the orientation of a photo from the camera of the device.
     * @param filePath The file path of the photograph.
     * @return The orientation as an int - 1 for landscape, 3 for upside-down landscape,
     * 6 for portrait, 8 for upside-down portrait.
     */
    private static int getOrientation(String filePath) {
        ExifInterface exif;
        int orientation = 0;

        try {
            exif = new ExifInterface(filePath);
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
        } catch (Exception e) {
            Log.d(TAG, "Error with photo file path");
        }
        Log.d(TAG, "Orientation: " + orientation);
        return orientation;
    }

    /**
     * Rotates an image depending on its orientation.
     * @param image The image to be rotated.
     * @param orientation The orientation of the image - can be retrieved with getOrientation
     *                    if the image is from the camera.
     * @return The rotated image.
     */
    private static Bitmap rotateImage(Bitmap image, int orientation) {
        Matrix matrix = new Matrix();
        switch(orientation) {
            case 3: matrix.postRotate(180);
                break;
            case 6: matrix.postRotate(90);
                break;
            case 8: matrix.postRotate(270);
                break;
        }

        return Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
    }

    /**
     * Create a thumbnail.
     * @param largeImageFilePath Where the original image is stored.
     * @param height The height of the image view where the thumbnail will be presented.
     * @param width The width of the image view where the thumbnail will be presented.
     * @return The thumbnail as a Bitmap object.
     */
    private Bitmap makeThumbnail(String largeImageFilePath, int height, int width) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(largeImageFilePath, opt);

        int scaleFactor = 1;
        try {
            scaleFactor = Math.min(opt.outHeight / height, opt.outWidth / width);
        } catch(Exception e) {
            Log.d(TAG, "Could not scale thumbnail.");
        }

        opt = new BitmapFactory.Options();
        opt.inSampleSize = scaleFactor;

        Bitmap thumbnail = BitmapFactory.decodeFile(largeImageFilePath, opt);
        return thumbnail;
    }

    /**
     * Save an image in jpeg format to the specified file path.
     * @param image The image to be saved.
     * @param filePath The file path where the file should be saved.
     */
    private void saveImage(Bitmap image, String filePath) {
        File file = new File(filePath);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100, out);
            Log.d(TAG, "Saved image: " + filePath);
            out.close();
        } catch (Exception e) {
            Log.d(TAG, "Saving image failed");
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Creates - in a directory called "FoodFlash" - a file that the photo can be saved to.
     * Each new file will have a unique name.
     * @return The photo file.
     */
    private File createPhotoFile() {
        File photoDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File ffDir = new File(photoDir, "FoodFlash");
        if(!ffDir.exists()) {
            if(!ffDir.mkdirs()) {
                Log.d(TAG, "Could not create directory" + ffDir.toString());
            }
        }
        return new File(ffDir, "foodflash" + (new Date().getTime()) + ".jpg");
    }
}