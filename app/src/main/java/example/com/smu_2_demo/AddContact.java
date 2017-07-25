package example.com.smu_2_demo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AddContact extends AppCompatActivity {
    final int REQ_CODE_SELECT_IMAGE=100;
    static String code1 = null;
    static String name1 = null;
    DataService mService;
    boolean mBound = false;
    public ArrayAdapter<String> listadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        final TextView nameinput = (TextView) findViewById(R.id.nameet);
        final TextView codeinput = (TextView) findViewById(R.id.CODETEXT3);
        Button saveButton = (Button) findViewById(R.id.button3);


        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(nameinput.getText())&&(TextUtils.isEmpty(codeinput.getText()))){
                    Toast.makeText(getApplicationContext(),"이름과 학번 모두 입력되지 않았습니다. 이름과 학번을 입력하세요",Toast.LENGTH_SHORT).show();
                    return;
                }

                else if(TextUtils.isEmpty(nameinput.getText())){
                    Toast.makeText(getApplicationContext(),"이름이 입력되지 않았습니다. 이름을 입력하세요",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(TextUtils.isEmpty(codeinput.getText())){
                    Toast.makeText(getApplicationContext(),"학번이 입력되지 않았습니다. 학번을 입력하세요",Toast.LENGTH_SHORT).show();
                    return;
                }

                else {
                    String name = nameinput.getText().toString();
                    String code = codeinput.getText().toString();

                    //Intent goservice = new Intent(AddContact.this, DataService.class);
                    //goservice.putExtra("name",name);
                    //goservice.putExtra("code",code);
                    //bindService(goservice, mConnection, Context.BIND_AUTO_CREATE);

                    final SharedPreferences pref = getSharedPreferences("MAIN2",MODE_PRIVATE);
                    final SharedPreferences.Editor editor = pref.edit();
                    pref.edit().putString(name,code).apply();

                    Toast.makeText(getApplicationContext(), "저장완료", Toast.LENGTH_SHORT).show();

                    NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    Intent intent = new Intent(AddContact.this,DetailContact.class);
                    intent.putExtra("name",name);
                    intent.putExtra("code",code);
                    PendingIntent pendingIntent = PendingIntent.getActivity(AddContact.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    Notification.Builder mBuilder = new Notification.Builder(AddContact.this);
                    mBuilder.setSmallIcon(R.drawable.contact3);
                    mBuilder.setTicker("주소록에 "+name+"이(가) 추가되었습니다.");
                    mBuilder.setWhen(System.currentTimeMillis());
                    mBuilder.setContentTitle("주소록 추가");
                    mBuilder.setContentText("이름 : "+name);
                    mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
                    mBuilder.setContentIntent(pendingIntent);
                    mBuilder.setAutoCancel(true);

                    nm.notify(111, mBuilder.build());

                    finish();
                }
            }

        });
    }

    public void onClick(View view){
        Toast.makeText(getApplicationContext(),"저장취소",Toast.LENGTH_SHORT).show();
        if(mBound){
            unbindService(mConnection);
            mBound=false;
        }
        finish();
    }

    public void clearname(View view){
        TextView namein = (TextView) findViewById(R.id.nameet);
        if(TextUtils.isEmpty(namein.getText())) {
            Toast.makeText(getApplicationContext(),"이름을 작성하지 않아 지울 수 없습니다.",Toast.LENGTH_SHORT).show();
        }
        else{
            namein.setText(null);
        }
    }
    public void clearcode(View view){
        TextView codein = (TextView) findViewById(R.id.CODETEXT3);
        if(TextUtils.isEmpty(codein.getText())) {
            Toast.makeText(getApplicationContext(),"학번을 작성하지 않아 지울 수 없습니다.",Toast.LENGTH_SHORT).show();
        }
        else {
            codein.setText(null);
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            DataService.LocalBinder binder = (DataService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
}
