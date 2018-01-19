package personal.dhaval.kavita.screens;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import personal.dhaval.kavita.R;
import personal.dhaval.kavita.utilities.ApplicationUtils;
import personal.dhaval.kavita.firebase.FirebaseEventManager;

public class Home extends AppCompatActivity {
    private ListView listView_fileNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initialize();
        List<String> fileNames = ApplicationUtils.getFileNames();
        setFileNames(fileNames);
    }

    private void initialize() {
        Toolbar toolbar;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fabAdd;
        fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setImageResource(R.drawable.icon_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseEventManager.firebaseAnalyticsEvent(Home.this,
                        FirebaseEventManager.EVENT_NEW_KAVITA,
                        ApplicationUtils.getCurrentTimestamp());
                Intent goToEditScreen = new Intent(Home.this, Add.class);
                startActivity(goToEditScreen);
            }
        });

        listView_fileNames = findViewById(R.id.listview);
        ApplicationUtils.createFolder();

        FirebaseEventManager.firebaseAnalyticsEvent(this,
                FirebaseEventManager.EVENT_APP_LANCH,
                ApplicationUtils.getCurrentTimestamp());
    }

    public void setFileNames(List<String> fileNameList) {
        CustomAdapter customAdapter = new CustomAdapter(this, fileNameList);
        listView_fileNames.setAdapter(customAdapter);
    }

    @Override
    protected void onResume() {
        setFileNames(ApplicationUtils.getFileNames());
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete) {
            ApplicationUtils.clearFolder();
            setFileNames(ApplicationUtils.getFileNames());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        setFileNames(ApplicationUtils.getFileNames());
    }

    private void confirmDelete(final String filename) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        builder.setTitle("Delete " + filename + "?")
                .setMessage("Are you sure you want to delete " + filename + "?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseEventManager.firebaseAnalyticsEvent(Home.this,
                                FirebaseEventManager.EVENT_DELETE_KAVITA,
                                filename);
                        ApplicationUtils.deleteFile(filename);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    public class CustomAdapter extends ArrayAdapter<String> {

        private final Context context;
        private final List<String> values;

        CustomAdapter(Context context, List<String> values) {
            super(context, -1, values);
            this.context = context;
            this.values = values;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View rowView = inflater.inflate(R.layout.list_item, parent, false);

            TextView title = rowView.findViewById(R.id.firstLine);
            title.setText(values.get(position));

            TextView date = rowView.findViewById(R.id.secondLine);
            date.setText(ApplicationUtils.getFileDate(values.get(position)));

            LinearLayout titleLinearLayout = rowView.findViewById(R.id.titleLinearView);
            titleLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, Add.class);
                    intent.putExtra("TITLE", values.get(position));
                    intent.putExtra("CONTENT", ApplicationUtils.readFileContent(values.get(position)));
                    context.startActivity(intent);
                }
            });

            ImageView whatsapp = rowView.findViewById(R.id.imgShareWhatsapp);
            whatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseEventManager.firebaseAnalyticsEvent(context,
                            FirebaseEventManager.EVENT_SHARE_WHATSAPP,
                            values.get(position));
                    ApplicationUtils.whatsappContent(context, values.get(position));
                }
            });

            ImageView email = rowView.findViewById(R.id.imgShareEmail);
            email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseEventManager.firebaseAnalyticsEvent(context,
                            FirebaseEventManager.EVENT_SHARE_EMAIL,
                            values.get(position));
                    ApplicationUtils.emailContent(context, values.get(position));
                }
            });

            ImageView delete = rowView.findViewById(R.id.imgDelete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    confirmDelete(values.get(position));
                }
            });

            return rowView;
        }
    }
}
