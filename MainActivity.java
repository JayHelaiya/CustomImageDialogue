package com.example.nichetech.imagedialoguebox;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public final static int REQUEST_FOR_CAMERA = 11;

    public final static int REQUEST_FOR_GALLERY = 12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_FOR_CAMERA:

                if (resultCode == Activity.RESULT_OK) {
                    //Image name
                    String path = fileUri.getPath();
                    String img_name = path.substring(path.lastIndexOf("/") + 1);
                    if (Utility.isNotNull(pathfordb))
                        pathfordb = pathfordb + "," + img_name;
                    else
                        pathfordb = img_name;

                    Bitmap bmp = Utility.Preview(fileUri.getPath());



                    if(images.size()<5)
                    {
                        images.add(bmp);
                        ImageAdapter img = new ImageAdapter(getActivity(), images);
                        imagegrid.setAdapter(img);

                    }
                    else
                    {
                        Toast.makeText(getActivity(),"maximum 5 image are allowed to add",Toast.LENGTH_SHORT).show();
                    }


                } else {
                    getActivity();
                    if (imagegrid.getChildCount() == 0)
                        imagehint.setVisibility(View.VISIBLE);
                }
                break;

            case REQUEST_FOR_GALLERY:

                if (resultCode == Activity.RESULT_OK) {
                    try {

                        InputStream inputStream = getActivity().getContentResolver().openInputStream(data.getData());
                        Drawable d = Drawable.createFromStream(inputStream, "imagename");
                        Bitmap bitmap = ((BitmapDrawable) d).getBitmap();

                        byte[] bitmapdata = Utility.getBytesFromBitmap(bitmap);

                        Date date = new Date();
                        Calendar calender = Calendar.getInstance();
                        calender.setTime(date);

                        String filename = "CAUSE_" + calender.get(Calendar.DATE) + calender.get(Calendar.MINUTE) + calender.get(Calendar.HOUR_OF_DAY) + calender.get(Calendar.SECOND) + ".png";

                        File sourceFile = new File(getActivity().getFilesDir().getAbsolutePath(), "CAUSE_IMG.png");
                        if (!sourceFile.exists())
                            sourceFile.createNewFile();
                        File f = new File(Utility.MEDIA_STORAGE_DIR.getPath() + File.separator + filename);

                        try {
                            FileUtils.copyFile(sourceFile, f);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (!f.exists())
                            f.createNewFile();

                        // write the bytes in file
                        FileOutputStream fos = new FileOutputStream(f);
                        fos.write(bitmapdata);

                        String filePath = f.getAbsolutePath();

                        String img_name = filePath.substring(filePath.lastIndexOf("/") + 1);

                        if (Utility.isNotNull(pathfordb))
                            pathfordb = pathfordb + "," + img_name;
                        else
                            pathfordb = img_name;


                        if(images.size()<5)
                        {
                            images.add(bitmap);
                            ImageAdapter img = new ImageAdapter(getActivity(),images);
                            int gvsize=img.getCount();
                            Log.e("gridview",gvsize+"");
                            imagegrid.setAdapter(img);
                            imagegrid.setVisibility(View.VISIBLE);

                        }
                        else
                        {

                            Toast.makeText(getActivity(),"maximum 5 image are allowed to add",Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    getActivity();
                    if (imagegrid.getChildCount() == 0)
                        imagehint.setVisibility(View.VISIBLE);
                }
                break;
        }

    }
}
