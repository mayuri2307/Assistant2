package com.emozers.assistant2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sendbird.android.BaseMessage;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import ai.api.AIDataService;
import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Metadata;
import ai.api.model.Result;
import ai.api.model.Status;

class Document
{
    public String id, language, text;

    public Document(String id, String language, String text){
        this.id = id;
        this.language = language;
        this.text = text;
    }
}
class Documents
{
    public List<Document> documents;

    public Documents() {
        this.documents = new ArrayList<Document>();
    }
    public void add(String id, String language, String text)
    {
        this.documents.add (new Document (id, language, text));

        Log.d("abc123","Added in documents"+String.valueOf(documents.size()));
    }

}
public class MessageListActivity extends AppCompatActivity
{
    DatabaseReference mDatabaseReference;
    String email,username;
    String activity_selected;
    boolean first_sen=false;
    private ChatListAdapter mAdapter;
    int message_count=0;
    private ListView mChatListView;
    //Adding Code from ChatBot_1 project to send queries to DialogFlow
    final AIConfiguration config = new AIConfiguration("2e7597771bd64ab9a05466bf384c96ec",
            AIConfiguration.SupportedLanguages.English,
            AIConfiguration.RecognitionEngine.System);
    boolean activity_closed;
    final AIDataService aiDataService=new AIDataService(config);
    final AIRequest aiRequest=new AIRequest();
    private String TAG="abcd123";
    //Button mButton=(Button)(findViewById(R.id.Play_Button));
    private String answer_from_DialogFlow_API="";
    private String answer_from_DialogFlow_API2="";
    //ChatBot Over
    private String MOOD="";
    private String task_to_do="";
    private SoundPool mSoundPool;
    private MediaPlayer mediaPlayer;
    private int Sound_Id;
    private final int NR_OF_SIMULTANEOUS_SOUNDS = 1;
    private final float LEFT_VOLUME = 1.0f;
    private final float RIGHT_VOLUME = 1.0f;
    private final int NO_LOOP = 0;
    private final int PRIORITY = 0;
    private final float NORMAL_PLAY_RATE = 1.0f;
    private RecyclerView mMessageRecycler;
    private TextView tv;
    String score="";
    boolean engaging;
    ArrayList<Double> sentiment_score;
    private MessageListAdapter mMessageAdapter;
    static String accessKey = "5658c1a7476a4f81b004c21f7ac8e8aa";
    static String host = "https://centralindia.api.cognitive.microsoft.com/text/analytics/v2.0/sentiment";
    public  String prettify(String json_text,String text)
    {
        //JsonParser parser = new JsonParser();
        //JsonObject json = parser.parse(json_text).getAsJsonObject();
        //JsonArray data=json.getAsJsonArray("score");
        try
        {
        Log.d("abc123",json_text);
        score=json_text.substring(32,37);
        Log.d("abc123",score);
        sentiment_score.add(Double.parseDouble(score));
        sentiment_score sc=new sentiment_score(text,score);
        mDatabaseReference.child("Users").child(username).child("Sentiment_score").push().setValue(sc);
        for(int i=0;i<sentiment_score.size();i++)
            Log.d("abc123","In on click"+String.valueOf(sentiment_score.get(i)));
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        //return gson.toJson(json);
        return null;
        }
        catch(Exception e)
        {}
        return null;
    }
    public void String_Analysis(final String query,final String message_from_user)
    {
        try
        {
        new AsyncTask<String ,Void,String>()
        {
            public String ans="";
            //GetSentiment gs=new GetSentiment();
            String accessKey = "5658c1a7476a4f81b004c21f7ac8e8aa";
            String host = "https://centralindia.api.cognitive.microsoft.com//text/analytics/v2.0/sentiment";
            //String ans="";
            @Override
            protected String doInBackground(String... strings)
            {
                try
                {
                    URL url = new URL(host);
                    Log.d("abc123",strings[0]+"in background");
                    byte[] encoded_text = strings[0].getBytes("UTF-8");
                    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "text/json");
                    connection.setRequestProperty("Ocp-Apim-Subscription-Key", accessKey);
                    connection.setDoOutput(true);
                    Log.d("abc123","befpre output stream declare");
                    OutputStream out=connection.getOutputStream();
                    Log.d("abc123","befpre output stream pass");
                    DataOutputStream wr = new DataOutputStream(out);
                    Log.d("abc123","after output stream");
                    wr.write(encoded_text, 0, encoded_text.length);

                    wr.flush();

                    wr.close();

                    StringBuilder response = new StringBuilder ();
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    Log.d("abc123","after buffered reader in");
                    String line;
                    while ((line = in.readLine()) != null)
                    {
                        response.append(line);
                    }
                    in.close();
                    connection.connect();
                    return response.toString();
                }
                catch (Exception e)
                {
                    Log.d("abc123","exception in bckground");
                    e.printStackTrace();return null;
                }

            }
            @Override
            protected void onPostExecute(String from_analysis) {
                Log.d("abc123", from_analysis + "post execute");
                ans = from_analysis;
                //gs.tv_analysed.setText(ans);
                //TextView tv = (TextView) findViewById(R.id.text_view1);

                //tv.setText(ans);
                prettify(ans,message_from_user);
                return;
            }
        }.execute(query);}
        catch (Exception e)
        {

        }
    }
    public void video_player()
    {
        engaging=false;
        Intent i=new Intent(MessageListActivity.this,video_player.class);
        i.putExtra("Activity","Video_Streaming");
        i.putExtra("Username",username);
        i.putExtra("Engaging",engaging);
        startActivity(i);
    }
    public void Meme_display()
    {
        engaging=false;
        Intent i=new Intent(MessageListActivity.this,Meme_And_Quotes.class);
        i.putExtra("Mood",MOOD);
        i.putExtra("Activity","Memes");
        i.putExtra("Username",username);
        i.putExtra("Engaging",engaging);
        startActivity(i);
    }

    public void Music_Player()
    {
        engaging=false;
        Intent song_intent=new Intent(MessageListActivity.this,Music_Player.class);
        song_intent.putExtra("Activity","Music");
        song_intent.putExtra("Mood",MOOD);
        song_intent.putExtra("Username",username);
        song_intent.putExtra("Engaging",engaging);
        startActivity(song_intent);
    }
    public void Shopping()
    {
        engaging=true;
        Random rand = new Random();
        int random_index = rand.nextInt(2);
        String []shop_apps={"com.flipkart.android","in.amazon.mShop.android.shopping"};
        String game=shop_apps[random_index];
        Intent intent0 = getPackageManager().getLaunchIntentForPackage(game);
        if (intent0!= null)
        {
            intent0.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent0);
            try
            {
                Thread.sleep(10000);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            Intent i=new Intent(MessageListActivity.this,Rating_Activity.class);
            i.putExtra("Engaging",engaging);
            i.putExtra("Username",username);
            i.putExtra("Activity","Shopping");
            startActivity(i);
        }
        else
        {
            intent0 = new Intent(Intent.ACTION_VIEW);
            intent0.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent0.setData(Uri.parse("market://details?id=" + game));
            startActivity(intent0);
        }
    }
    public void Game_Starter()
    {
        engaging=true;
        Random rand = new Random();
        int random_index = rand.nextInt(3);
        String []name_of_games_normal={"com.colorup.game","com.amanotes.beathopper","com.ludo.king"};
        String game="";
        String[]name_of_games_angry={"io.voodoo.holeio","air.com.gamebrain.hocus","com.magicfluids.demo"};
        if(MOOD.equalsIgnoreCase("sadness")||MOOD.equalsIgnoreCase("disgust")
                ||MOOD.equalsIgnoreCase("fear"))
            game=name_of_games_normal[random_index];
        else
            game=name_of_games_angry[random_index];
        Intent intent0 = getPackageManager().getLaunchIntentForPackage(game);
        if (intent0!= null)
        {
            intent0.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent0);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent i=new Intent(MessageListActivity.this,Rating_Activity.class);
            i.putExtra("Engaging",engaging);
            i.putExtra("Username",username);
            i.putExtra("Activity","Games");
            startActivity(i);
        }
        else
        {
            intent0 = new Intent(Intent.ACTION_VIEW);
            intent0.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent0.setData(Uri.parse("market://details?id=" + game));
            startActivity(intent0);
        }
    }
    public void Task_TO_Do(double age)
    {
        String[]  tasks_arr = {"Music","Game","Video", "Shopping","Meme"};
        int[] tasks_index={0,1,2,3,4};
        int[] new_tasks_index={0,1,2,3,4,5,6};
        String task_performed=" ";
        if(age>40)
        {
            new_tasks_index[5]=3;
            new_tasks_index[6]=3;
        }
        if(age>=30 && age<=40)
        {
            new_tasks_index[5]=0;
            new_tasks_index[6]=0;
        }
        if(age>=20 && age<30)
        {
            new_tasks_index[5]=4;
            new_tasks_index[6]=4;
        }
        if(age>=0 && age<20)
        {
            new_tasks_index[5]=3;
            new_tasks_index[6]=2;
            new_tasks_index[4]=3;
        }
        Random rand = new Random();
        int random_index = rand.nextInt(5);
        //random_index=1;
        //System.out.println("Random index is "+random_index);
        int a= new_tasks_index[random_index];
        //System.out.println("a is "+a);
        task_performed = tasks_arr[a];
        //System.out.println("Task performed is "+task_performed);

        task_to_do=task_performed;
        //TextView task_to_do_tv=(TextView)findViewById(R.id.task_to_do);
        //task_to_do_tv.setText("We have a surprise for you.\nClick on the SURPRISE button");
    }
    public void DialogFlow_Api_Function(String query)
    {
        Log.d(TAG,"Processing for"+query);
        aiRequest.setQuery(query);
        new AsyncTask<AIRequest, Void, AIResponse>()
        {
            @Override
            protected AIResponse doInBackground(AIRequest... requests)
            {
                final AIRequest request = requests[0];
                try
                {
                    final AIResponse response = aiDataService.request(aiRequest);

                    /*try
                    {
                        TimeUnit.SECONDS.sleep(5);
                    }
                    catch (Exception e)
                    {

                    }*/

                    return response;
                }
                catch (AIServiceException e)
                {
                    Log.d("abc12345","Response is null");
                }
                return null;
            }
            @Override
            protected void onPostExecute(AIResponse aiResponse)
            {
                Log.d(TAG,"234567");
                if (aiResponse != null)
                {
                    /*try
                    {
                        TimeUnit.SECONDS.sleep(2);
                    }
                    catch (Exception e)
                    {

                    }*/

                    answer_from_DialogFlow_API=Answer_from_dialogflow(aiResponse);
                    if(answer_from_DialogFlow_API.toLowerCase().contains("something"))
                    {
                        //Button mButton=(Button)findViewById(R.id.Play_Button);
                        //mButton.setVisibility(View.VISIBLE);
                    }
                    Log.d("abc1234",answer_from_DialogFlow_API);
                    if(answer_from_DialogFlow_API==null||answer_from_DialogFlow_API==""){
                        answer_from_DialogFlow_API="I am new to this world. Sometimes I get confused.";
                    }
                    //TextView tv_for_bot=(TextView)findViewById(R.id.message_by_bot);
                    //tv_for_bot.setText(answer_from_DialogFlow_API);
                    if(first_sen==false)
                    {
                        Log.d("first_sen","First sentence="+first_sen);
                        InstantMessage mess=new InstantMessage("Hi... I am emozer-your emotional well being assistant.\n" +
                                "I have detected that you are in a "+MOOD+" mood today.\n" +
                                "Tell me about your day!","Chatbot");
                        mDatabaseReference.child("Users").child(username).child("Messages").push().setValue(mess);
                        first_sen=true;
                    }
                    else
                    {
                    InstantMessage mess=new InstantMessage(answer_from_DialogFlow_API,"Chatbot");
                    mDatabaseReference.child("Users").child(username).child("Messages").push().setValue(mess);
                    }
                }
                else
                    {
                    Log.d("abc1234","I am here");
                }
            }
        }.execute(aiRequest);
    }
    public String Answer_from_dialogflow(final AIResponse response)
    {
        //Log.d(TAG,"I am he9re0i,0,kpl");
        // Use the response object to get all the results
        final Status status = response.getStatus();
        //Log.d(TAG, "Status code: " + status.getCode());
        //Log.d(TAG, "Status type: " + status.getErrorType());
        final Result result = response.getResult();
        //Log.d(TAG, "Resolved query: " + result.getResolvedQuery());
        //final Result result = response.getResult();
        //Log.d(TAG, "Action: " + result.getAction());
        //final Result result = response.getResult();
        final String speech = result.getFulfillment().getSpeech();
        Log.d("abc1234", "Speech: " + speech+String.valueOf(speech.length()));
        //final Result result = response.getResult();
        final Metadata metadata = result.getMetadata();
        if (metadata != null)
        {
            //Log.d(TAG, "Intent id: " + metadata.getIntentId());
            //Log.d(TAG, "Intent name: " + metadata.getIntentName());
        }
        //final Result result = response.getResult();
        final HashMap<String, JsonElement> params = result.getParameters();
        if (params != null && !params.isEmpty())
        {
            Log.i(TAG, "Parameters: ");
            for (final Map.Entry<String, JsonElement> entry : params.entrySet()) {
                Log.i(TAG, String.format("%s: %s", entry.getKey(), entry.getValue().toString()));
            }
        }
        Log.d(TAG,"result.getFull"+speech);
        return speech;
    }
    public void on_send_click(View v)
    {
        EditText et=(EditText)findViewById(R.id.messageInput);
        Editable s=et.getText();message_count+=1;
        if(message_count%4==0)
        {
            new AlertDialog.Builder(this)
                    .setTitle("Surprise....Surprise!!!")
                    .setMessage("We have a surprise for you.\nClick on the SURPRISE button")
                    .setPositiveButton("Surprise", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("gaana","Song is played");
                            String td=task_to_do;
                            if(td.equalsIgnoreCase("Game"))
                            {
                                Game_Starter();
                            }
                            else if(td.equalsIgnoreCase("Meme"))
                            {
                                Meme_display();

                            }
                            else if(td.equalsIgnoreCase("video"))
                            {
                                video_player();
                            }
                            else if(td.equalsIgnoreCase("Music"))
                            {
                                Music_Player();
                            }
                            else
                            {
                                Shopping();
                            }
                            //Intent i=new Intent(MessageListActivity.this,Rating_Activity.class);
                            //startActivity(i);
                        }
                    })
                    .show();
        }
        //et.refreshDrawableState();
        et.setText("");
        //TextView tv=(TextView)findViewById(R.id.message_by_user);
        Log.d(TAG,"Entering DialogFlow Api Function");
        //String name_of_author=mDatabaseReference.child("Users").child(username).child("information");
        InstantMessage mess=new InstantMessage(String.valueOf(s),username);
        mDatabaseReference.child("Users").child(username).child("Messages").push().setValue(mess);

        DialogFlow_Api_Function(String.valueOf(s));
        Documents documents = new Documents();
        documents.add("1", "en", String.valueOf(s));

        String_Analysis(new Gson().toJson(documents),String.valueOf(s));

        Log.d(TAG,"Outside DialogFlowApi");
        //tv.setText(String.valueOf(s));
        Log.d(TAG,"Answer from DialogFlowAPi for"+String.valueOf(s)+"is"+answer_from_DialogFlow_API);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        mChatListView=(ListView)findViewById(R.id.chat_list_view);
        Intent i1=getIntent();
        email=i1.getStringExtra("Email");
        username=i1.getStringExtra("Username");
        mDatabaseReference=FirebaseDatabase.getInstance().getReference();
        sentiment_score=new ArrayList<>();
        //mMessageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);

        //messageList.add("abc");
        //mMessageAdapter = new MessageListAdapter(this, messageList);

        //mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        Intent i = getIntent();
        MOOD= i.getStringExtra("Mood");
        //tv=(TextView)(findViewById(R.id.message_by_bot));
        /*tv.setText("Hi... I am emozer-your emotional well being assistant.\n" +
                "I have detected that you are in a "+MOOD+" mood today.\n" +
                "Tell me about your day!");*/
        //tv.setText(MOOD);
        /*InstantMessage mess=new InstantMessage("Hi... I am emozer-your emotional well being assistant.\n" +
                "I have detected that you are in a "+MOOD+" mood today.\n" +
                "Tell me about your day!","Chatbot");
        mDatabaseReference.child("Users").child(username).child("Messages").push().setValue(mess);*/
        String age=i.getStringExtra("Age");
        Task_TO_Do(Double.parseDouble(age));
        Log.d(TAG,age+task_to_do);

        AudioAttributes aa=new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).setUsage(AudioAttributes.USAGE_GAME).build();
        mSoundPool=new SoundPool.Builder().setMaxStreams(100).setAudioAttributes(aa).build();
        Sound_Id=mSoundPool.load(getApplicationContext(),R.raw.all_is_well,1);
        /*final Button mButton=(Button)(findViewById(R.id.Play_Button));
        mButton.setVisibility(View.GONE);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.d("gaana","Song is played");
                String td=task_to_do;
                if(td.equalsIgnoreCase("Game"))
                {
                    Game_Starter();
                }
                else if(td.equalsIgnoreCase("Meme"))
                {
                    Meme_display();

                }
                else if(td.equalsIgnoreCase("video"))
                {
                    video_player();
                }
                else if(td.equalsIgnoreCase("Music"))
                {
                    Music_Player();
                }
                else
                {
                    Shopping();
                }
                //Intent i=new Intent(MessageListActivity.this,Rating_Activity.class);
                //startActivity(i);
            }

        });*/


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_add_key)
        {
            Intent intent = new Intent(MessageListActivity.this, Grapher.class);
            Bundle args = new Bundle();
            args.putSerializable("ARRAYLIST",(Serializable)sentiment_score);
            intent.putExtra("BUNDLE",args);
            startActivity(intent);
        }
        if(id==R.id.profile)
        {
            Intent intent =new Intent(MessageListActivity.this,user_profile.class);
            intent.putExtra("Username",username);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public  void onStart()
    {
        super.onStart();
        /*mAdapter=new ChatListAdapter(this,mDatabaseReference,username);
        mChatListView.setAdapter(mAdapter);*/
        mAdapter=new ChatListAdapter(this,mDatabaseReference,username);
        mChatListView.setAdapter(mAdapter);
        //for(int i=0;i<2000000;i++);
        //DialogFlow_Api_Function("Hi");
    }
    @Override
    public void onStop()
    {
        super.onStop();
        mAdapter.cleanup();
    }
}
