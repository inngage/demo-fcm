package sample.inngage.com.br.inngagefcm;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.inngage.sdk.InngageIntentService;
import br.com.inngage.sdk.InngageUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "InngageFCM";
    // Informe o seu app token
    private static final String inngageAppToken = "818a27c914f021f3fa3865c4f915ca8d";
    // Informe o provedo de cloud do Google a ser usado (FCM ou GCM)
    private static final String googleMessageProvider = "FCM";
    // Informe o ambiente da plataforma Inngage (dev ou prod)
    private static final String inngageEnvironment = "dev";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handleSubscription();
        handleNotification();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void handleSubscription() {

        /* Integração padrão sem identifier ou custom fields */
        //InngageIntentService.startInit(this, inngageAppToken, inngageEnvironment, googleMessageProvider);

        /* Integração padrão com identifier e sem custom fields */
        //InngageIntentService.startInit(this, inngageAppToken, "viniciusdpaula@gmail.com", inngageEnvironment, googleMessageProvider);

        /* Objeto a ser enviado como custom field */
        JSONObject jsonCustomField = new JSONObject();
        try {
            jsonCustomField.put("Primeiro_Nome", "João");
            jsonCustomField.put("Data_Nascimento", "1950-12-01");
            jsonCustomField.put("Cidade", "São Paulo");
            jsonCustomField.put("Estado", "SP");

        } catch (JSONException e) {
            Log.e(TAG, "Error creating JSON object: " + e);
        }
        /* Integração com identifier e custom fields */
        InngageIntentService.startInit(this, inngageAppToken, "viniciusdpaula@gmail.com", inngageEnvironment, googleMessageProvider, jsonCustomField);

    }

    private void handleNotification() {

        String notifyID = "", title = "", body = "";

        if (getIntent().hasExtra("notifyID")) {

            Intent intent = getIntent();
            Bundle bundle  = intent.getExtras();

            if (bundle.getString("notifyID") != null) {

                notifyID = bundle.getString("notifyID");
            }
            if (bundle.getString("title") != null) {

                title =  bundle.getString("title");
            }
            if (bundle.getString("body") != null) {

                body =  bundle.getString("body");
            }

            InngageUtils.showDialog(title, body, notifyID, inngageAppToken, inngageEnvironment, this);

            if (bundle.getString("url") != null) {

                setContentView(R.layout.inngage_webview);
                WebView webView = (WebView) findViewById(R.id.webview);

                if (webView != null) {

                    Log.d(TAG, "Loading URL: " + bundle.getString("url"));
                    webView.loadUrl(bundle.getString("url"));
                    webView.setWebViewClient(new WebViewClient());
                } else {
                    Log.d(TAG, "WebView object has no instance");
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
