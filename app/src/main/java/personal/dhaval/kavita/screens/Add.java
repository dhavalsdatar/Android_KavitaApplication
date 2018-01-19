package personal.dhaval.kavita.screens;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import personal.dhaval.kavita.utilities.ApplicationConstants;
import personal.dhaval.kavita.R;
import personal.dhaval.kavita.utilities.ApplicationUtils;

public class Add extends AppCompatActivity {

    private final int READ_EXTERNAL_STORAGE_PERMISSION_CODE = 0;
    private final int WRITE_EXTERNAL_STORAGE_PERMISSION_CODE = 0;
    private View mLayout;
    private EditText etTitle;
    private EditText etContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        initialize();
    }

    private void initialize() {
        Toolbar toolbar;

        toolbar = findViewById(R.id.toolbar);
        etTitle = findViewById(R.id.title);
        etContent = findViewById(R.id.content);
        mLayout = etContent;

        toolbar.setVisibility(View.GONE);
        requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE_PERMISSION_CODE, "Requesting write permission");
        requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE_PERMISSION_CODE, "Requesting read permission");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            etTitle.setText(bundle.getString("TITLE"));
            etContent.setText(bundle.getString("CONTENT"));
        }
    }

    private void saveTextToFile() throws Exception {
        String title = etTitle.getText().toString();
        if (!isNotEmpty(title)) {
            title = "Draft: " + new SimpleDateFormat("dd-MMM-yyyy").format(new Date());
        }

        String content = etContent.getText().toString();
        File file = new File(String.format("%s%s.txt", ApplicationConstants.kavitaFolder, title));
        FileWriter writer = new FileWriter(file);
        writer.write(content);
        writer.flush();
        writer.close();
    }

    private void requestPermission(final String permission, final int requestCode, String message) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                permission)) {
            Snackbar.make(mLayout, message,
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(Add.this,
                            new String[]{permission}, requestCode);
                }
            }).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }

    private boolean isNotEmpty(String text) {
        return !(text.isEmpty() || text.equals(" "));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == READ_EXTERNAL_STORAGE_PERMISSION_CODE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                System.out.println("Permission has been granted. - READ");
            } else {
                System.out.println("Permission has NOT been granted. - READ");
            }
        }
        if (requestCode == WRITE_EXTERNAL_STORAGE_PERMISSION_CODE) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                System.out.println("Permission has been granted. - WRITE");
            } else {
                System.out.println("Permission has NOT been granted. - WRITE");
            }
        }
    }

    @Override
    public void onBackPressed() {
        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                String orig_title = bundle.getString("TITLE");
                if ((etTitle.getText().toString()).equals(orig_title))
                    saveTextToFile();
                else {
                    ApplicationUtils.deleteFile(orig_title);
                    saveTextToFile();
                }
            } else {
                saveTextToFile();
            }
        } catch (Exception e) {
            System.out.println("Exception!!!!!!!!!!!!!!!! \n" + e);
        }
        super.onBackPressed();
    }
}
