package at.vcity.androidim;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import at.vcity.androidim.interfaces.IAppManager;
import at.vcity.androidim.services.IMService;


/**
 * Created by akbank on 18/11/15.
 */

// splash screen
public class SplashScreen extends Activity{

    public static final String AUTHENTICATION_FAILED = "0";
    SharedPreferences.Editor editor;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String userNameString = "userNameKey";
    public static final String passwordString = "passwordKey";
    Boolean isLogin = false;

    private IAppManager imService;

    Post post = new Post();


    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            imService = ((IMService.IMBinder)service).getService();

            if (imService.isUserAuthenticated() == true)
            {
                Intent i = new Intent(SplashScreen.this, FriendList.class);
                startActivity(i);
                SplashScreen.this.finish();
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            // Because it is running in our same process, we should never
            // see this happen.
            imService = null;
            Toast.makeText(SplashScreen.this, R.string.local_service_stopped,
                    Toast.LENGTH_SHORT).show();
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen); //messaging_screen);

        startService(new Intent(SplashScreen.this, IMService.class));
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		editor = sharedpreferences.edit();
		String deger = sharedpreferences.getString(userNameString,"");
		if(deger.isEmpty()){
			isLogin = false;
            Intent i = new Intent(SplashScreen.this, Login.class);
            //i.putExtra(FRIEND_LIST, result);
            startActivity(i);
            SplashScreen.this.finish();
		}else {
			isLogin = true;
            new Post().execute();
		}


    }

    @Override
    protected void onPause()
    {
        unbindService(mConnection);
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        bindService(new Intent(SplashScreen.this, IMService.class), mConnection, Context.BIND_AUTO_CREATE);

        super.onResume();
    }

    public void loginWebAPI(){
        if (imService == null) {
            Toast.makeText(getApplicationContext(), R.string.not_connected_to_service, Toast.LENGTH_LONG).show();
            //showDialog(NOT_CONNECTED_TO_SERVICE);
            return;
        }
        if (imService.isNetworkConnected() == false)
        {
            Toast.makeText(getApplicationContext(),R.string.not_connected_to_network, Toast.LENGTH_LONG).show();
            //showDialog(NOT_CONNECTED_TO_NETWORK);

        }
        final String userName = sharedpreferences.getString(userNameString,"");
        final String password = sharedpreferences.getString(passwordString,"");
        if (userName.length() > 0 && passwordString.length() > 0 ) {

            Thread loginThread = new Thread(){
                private Handler handler = new Handler();
                @Override
                public void run() {
                    String result = null;
                    try {
                        result = imService.authenticateUser(userName.toString(), password.toString());
                    } catch (UnsupportedEncodingException e) {

                        e.printStackTrace();
                    }
                    if (result == null || result.equals(AUTHENTICATION_FAILED))
                    {
								/*
								 * Authenticatin failed, inform the user
								 */
                        handler.post(new Runnable(){
                            public void run() {
                                Toast.makeText(getApplicationContext(),R.string.make_sure_username_and_password_correct, Toast.LENGTH_LONG).show();

                                //showDialog(MAKE_SURE_USERNAME_AND_PASSWORD_CORRECT);
                            }
                        });

                    }
                    else {

								/*
								 * if result not equal to authentication failed,
								 * result is equal to friend list of the user
								 */
                        handler.post(new Runnable(){
                            public void run() {
                                Intent i = new Intent(SplashScreen.this, FriendList.class);
                                //i.putExtra(FRIEND_LIST, result);
                                startActivity(i);
                                //SplashScreen.this.finish();
                            }
                        });

                    }
                }
            };
            loginThread.start();

        }
        else {
					/*
					 * Username or Password is not filled, alert the user
					 */
            Toast.makeText(getApplicationContext(),R.string.fill_both_username_and_password, Toast.LENGTH_LONG).show();
            //showDialog(FILL_BOTH_USERNAME_AND_PASSWORD);
        }
    }


    class Post extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() { // Post tan önce yapılacak işlemler.

        }

        protected Void doInBackground(Void... unused) { // Arka Planda yapılacaklar. Yani Post işlemi

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void unused) { //Posttan sonra
            if(imService != null) {
                loginWebAPI();
                /*Intent i = new Intent(getApplicationContext(), FriendList.class);
                startActivity(i);*/
                //finish();
            }
        }
    }
}
