package com.example.uploadretrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class MainActivity extends AppCompatActivity {


    ImageView im,imm,immm;
    private int GALLERY = 1, CAMERA = 2,RCGALLERY=3,RCCAMERA=4,PGALLERY=5,PCAMERA=6;
    private static final String IMAGE_DIRECTORY = "/driver";
    EditText name1,phone1,email1,password1,city1,vechre1,licno1,rcno1,stp1;
    Button button;
    private Uri uri,ut,up;
    String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestMultiplePermissions();

        im=findViewById(R.id.lICE);
        imm=findViewById(R.id.rc);
        immm=findViewById(R.id.photo);
        name1=findViewById(R.id.name);
        phone1=findViewById(R.id.phone_no);
        email1=findViewById(R.id.email);
        password1=findViewById(R.id.password);
        city1=findViewById(R.id.city);
        vechre1=findViewById(R.id.vechreg);
        licno1=findViewById(R.id.Lino);
        rcno1=findViewById(R.id.rcno);
        stp1=findViewById(R.id.dtk);
        button=findViewById(R.id.button);





        /*final String file = getRealPathFromURIPath(ut, MainActivity.this);
        final String filep = getRealPathFromURIPath(up, MainActivity.this);
*/

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                uploadToServer(filePath);
            }
        });



        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });

        imm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             showPictureDialogrc();
            }
        });

        immm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialogprof();
            }
        });


    }

    private void showPictureDialogprof(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallaryprof();
                                break;
                            case 1:
                                takePhotoFromCameraprof();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallaryprof() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, PGALLERY);
    }

    private void takePhotoFromCameraprof() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, PCAMERA);
    }


    private void showPictureDialogrc(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallaryrc();
                                break;
                            case 1:
                                takePhotoFromCamerarc();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallaryrc() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, RCGALLERY);
    }

    private void takePhotoFromCamerarc() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, RCCAMERA);
    }



    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                uri=data.getData();
                filePath = getRealPathFromURIPath(uri, MainActivity.this);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    Toast.makeText(MainActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    im.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        }  if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            im.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            Toast.makeText(MainActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();

        }
         if(requestCode== RCGALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                ut=data.getData();
                Log.d("RC","MM"+contentURI);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    Toast.makeText(MainActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    imm.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }
             if (requestCode == RCCAMERA) {
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    imm.setImageBitmap(thumbnail);
                    saveImage(thumbnail);
                    Toast.makeText(MainActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();

                }


          if(requestCode==PGALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                up=data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    Toast.makeText(MainActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    immm.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        }
        if(requestCode == PCAMERA){
            Toast.makeText(MainActivity.this,"elbjuugv",Toast.LENGTH_SHORT).show();
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            immm.setImageBitmap(thumbnail);
            //  saveImage(thumbnail);
            Toast.makeText(MainActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();

        }



    }









    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::---&gt;" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private void  requestMultiplePermissions(){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }



                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }



    private void uploadToServer(String filePath) {
        Retrofit retrofit = NetworkClient.getRetrofitClient(this);
        UploadAPIs uploadAPIs = retrofit.create(UploadAPIs.class);
        //Create a file object using file path
        File im = new File(filePath);
        Log.d("mmmmm","mm"+im.getName());
        File imm = new File(filePath);
        Log.d("mmmmmmm","mmm"+imm.getName());
        File immm=new File(filePath);
        Log.d("mmmmmmm","mmm"+immm.getName());
        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), im);
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part part = MultipartBody.Part.createFormData("license", im.getName(), fileReqBody);

        RequestBody photob = RequestBody.create(MediaType.parse("image/*"), imm);
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part part1 = MultipartBody.Part.createFormData("photo", imm.getName(), photob);

        RequestBody rcbookb = RequestBody.create(MediaType.parse("image/*"), immm);
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part part2 = MultipartBody.Part.createFormData("rcbook", immm.getName(), rcbookb);
        //Create request body with text description and text media type
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"),name1.getText().toString());
        RequestBody phone_no = RequestBody.create(MediaType.parse("text/plain"), phone1.getText().toString());
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), email1.getText().toString());
        RequestBody city = RequestBody.create(MediaType.parse("text/plain"), city1.getText().toString());
        RequestBody password = RequestBody.create(MediaType.parse("text/plain"),password1.getText().toString());
        RequestBody vehicle_reg = RequestBody.create(MediaType.parse("text/plain"),vechre1.getText().toString());
        RequestBody license_no = RequestBody.create(MediaType.parse("text/plain"), licno1.getText().toString());
        RequestBody rcno = RequestBody.create(MediaType.parse("text/plain"),rcno1.getText().toString());
        RequestBody stockpoint = RequestBody.create(MediaType.parse("text/plain"), stp1.getText().toString());
        //
        Call<Result> call = uploadAPIs.uploadImage(part,part1,part2, name,phone_no,email,city,password,vehicle_reg,license_no,rcno,stockpoint);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response <Result>response) {
              String result=response.body().getSuccess();

                Toast.makeText(MainActivity.this,"Successfully registered"+result,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Failed registered"+t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    }
