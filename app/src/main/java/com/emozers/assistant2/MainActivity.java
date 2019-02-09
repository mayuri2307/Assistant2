package com.emozers.assistant2;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.Emotion;
import com.microsoft.projectoxford.face.contract.Face;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class MainActivity extends AppCompatActivity
{

    private final String apiEndpoint = "https://centralindia.api.cognitive.microsoft.com/face/v1.0";
    private String filepath;
    private Bitmap bm_to_send;
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    // Replace `<Subscription Key>` with your subscription key.
    // For example, subscriptionKey = "0123456789abcdef0123456789ABCDEF"
    private final String subscriptionKey = "ca1bdbeeefa3414fa4d9de1c254429da";

    private Button mButton;
    //private static final int MY_CAMERA_PERMISSION_CODE = 100;
    //private Bitmap photo;
    private final FaceServiceClient faceServiceClient =
            new FaceServiceRestClient(apiEndpoint, subscriptionKey);

    private final int PICK_IMAGE = 1;
    private ProgressDialog detectionProgressDialog;
    private void SaveImage(Bitmap finalBitmap)
    {
        String root = Environment.getExternalStorageDirectory().toString();
        Log.d("Address is",root);
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();

        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File (myDir, fname);

        filepath=myDir+"/"+fname;
        Log.d("Final address",filepath);
        if (file.exists ()) file.delete ();
        try
        {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private String getEmotion(Emotion emotion)
    {
        String emotionType = "";
        double emotionValue = 0.0;
        if (emotion.anger > emotionValue)
        {
            emotionValue = emotion.anger;
            emotionType = "Anger";
        }
        if (emotion.contempt > emotionValue)
        {
            emotionValue = emotion.contempt;
            emotionType = "Contempt";
        }
        if (emotion.disgust > emotionValue)
        {
            emotionValue = emotion.disgust;
            emotionType = "Disgust";
        }
        if (emotion.fear > emotionValue)
        {
            emotionValue = emotion.fear;
            emotionType = "Fear";
        }
        if (emotion.happiness > emotionValue)
        {
            emotionValue = emotion.happiness;
            emotionType = "Happiness";
        }
        if (emotion.neutral > emotionValue)
        {
            emotionValue = emotion.neutral;
            emotionType = "Neutral";
        }
        if (emotion.sadness > emotionValue)
        {
            emotionValue = emotion.sadness;
            emotionType = "Sadness";
        }
        if (emotion.surprise > emotionValue)
        {
            emotionValue = emotion.surprise;
            emotionType = "Surprise";
        }
        //return String.format("%s: %f", emotionType, emotionValue);
        return emotionType;
    }
    public void sendbird(View v)
    {
        Intent t=new Intent(MainActivity.this,MessageListActivity.class);
        startActivity(t);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sid);
        this.imageView = (ImageView)this.findViewById(R.id.imageView1);
        Button photoButton = (Button) this.findViewById(R.id.detect_button);
        photoButton.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED)
                {

                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            MY_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }


        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            //imageView.setImageBitmap(photo);
            SaveImage(photo);
            Log.d("before_bm_to_send",filepath);
            bm_to_send=BitmapFactory.decodeFile(filepath);
            Log.d("After_bm_to_send",filepath);
            detectAndFrame(bm_to_send);
            Log.d("After_detectandframe",filepath);
        }
    }
    private void detectAndFrame( Bitmap imageBitmap)
    {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

        ByteArrayInputStream inputStream =
                new ByteArrayInputStream(outputStream.toByteArray());

        AsyncTask<InputStream, String, Face[]> detectTask =
                new AsyncTask<InputStream, String, Face[]>() {
                    String exceptionMessage = "";

                    @Override
                    protected Face[] doInBackground(InputStream... params) {
                        try {
                            //publishProgress("Detecting...");
                            /*Face[] result = faceServiceClient.detect(
                                    params[0],
                                    true,         // returnFaceId
                                    false,
                                    null       // returnFaceLD
                                    // returnFaceAttributes:
                                     new FaceServiceClient.FaceAttributeType[] {
                                        FaceServiceClient.FaceAttributeType.Age,
                                        FaceServiceClient.FaceAttributeType.Gender }


                            );*/

                            Face[] result = faceServiceClient.detect(params[0],  /* Input stream of image to detect */
                                    true,       /* Whether to return face ID */
                                    true,       /* Whether to return face landmarks */
                        /* Which face attributes to analyze, currently we support:
                           age,gender,headPose,smile,facialHair */
                                    new FaceServiceClient.FaceAttributeType[] {
                                            FaceServiceClient.FaceAttributeType.Age,
                                            FaceServiceClient.FaceAttributeType.Gender,
                                            FaceServiceClient.FaceAttributeType.Smile,
                                            FaceServiceClient.FaceAttributeType.Glasses,
                                            FaceServiceClient.FaceAttributeType.FacialHair,
                                            FaceServiceClient.FaceAttributeType.Emotion,
                                            FaceServiceClient.FaceAttributeType.HeadPose,
                                            FaceServiceClient.FaceAttributeType.Accessories,
                                            FaceServiceClient.FaceAttributeType.Blur,
                                            FaceServiceClient.FaceAttributeType.Exposure,
                                            FaceServiceClient.FaceAttributeType.Hair,
                                            FaceServiceClient.FaceAttributeType.Makeup,
                                            FaceServiceClient.FaceAttributeType.Noise,
                                            FaceServiceClient.FaceAttributeType.Occlusion
                                    });
                            if (result == null){
                               // publishProgress(
                                  //      "Detection Finished. Nothing detected");
                                return null;
                            }
                            //publishProgress(String.format(
                              //      "Detection Finished. %d face(s) detected",
                                //    result.length));
                            return result;
                        } catch (Exception e) {
                            exceptionMessage = String.format(
                                    "Detection failed: %s", e.getMessage());
                            return null;
                        }
                    }

                    @Override
                    protected void onPreExecute() {
                        //TODO: show progress dialog
                        //detectionProgressDialog.show();
                    }
                    @Override
                    protected void onProgressUpdate(String... progress) {
                        //TODO: update progress
                        //detectionProgressDialog.setMessage(progress[0]);
                    }
                    @Override
                    protected void onPostExecute(Face[] result) {
                        //TODO: update face frames
                        //detectionProgressDialog.dismiss();

                        if(!exceptionMessage.equals(""))
                        {
                            showError(exceptionMessage);
                        }
                        if (result == null)
                        {Log.d("nulllllllll","result is null");return;}

                        //ImageView imageView = findViewById(R.id.imageView1);
                        Log.d("Before_display","display1");display(result);Log.d("Before_display","display2");
                        //imageView.setImageBitmap(
                          //      drawFaceRectanglesOnBitmap(imageBitmap, result));
                        //imageBitmap.recycle();
                    }
                };
        Log.d("Just_Inside","Inside bitmap4");

        detectTask.execute(inputStream);
        Log.d("Just_Inside","Inside bitmap5");
    }
    private void display(Face[] faces)
    {
        Log.d("face_and_face",String.valueOf(faces.length));
        if (faces != null)
        {
            for (Face face : faces)
            {
                Log.d("face_and_face",String.valueOf(face.faceAttributes.emotion.neutral));
                Intent i=new Intent(MainActivity.this,MessageListActivity.class);

                //i.putExtra("", "value2");
                //Bundle basket= new Bundle();
                //basket.putString("Neutral_Mood",face.faceAttributes.emotion.neutral);
                String ans=getEmotion(face.faceAttributes.emotion);
                i.putExtra("Mood",ans);
                i.putExtra("Age",String.valueOf(face.faceAttributes.age));
                startActivity(i);
            }
        }

    }
    private void showError(String message)
    {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }})
                .create().show();
    }
}