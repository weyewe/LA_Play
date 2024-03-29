package com.marakana.yamba;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StatusActivity extends Activity 
implements OnClickListener, TextWatcher{
	private static final String TAG="StatusActivity";
	EditText editText;
	Button updateButton;
	Twitter twitter;
	TextView textCount;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status);
        
        editText= (EditText)findViewById(R.id.editText);
        updateButton = (Button)findViewById(R.id.buttonUpdate);
        
        updateButton.setOnClickListener(this);
        
        twitter = new Twitter("learningandroid", "pass2010");
        twitter.setAPIRootUrl("http://learningandroid.status.net/api");
        
        textCount = (TextView)findViewById(R.id.textCount);
        textCount.setText( Integer.toString(140));
        textCount.setTextColor( Color.GREEN );
        
        editText.addTextChangedListener(this);
        
    }
    
    class PostToTwitter extends AsyncTask<String, Integer, String> {
    	@Override 
    	protected String doInBackground(String... statuses){
    		try{
    			Twitter.Status status = twitter.updateStatus(statuses[0]);
    			return status.text;
    		}catch(TwitterException e ){
    			Log.e(TAG, e.toString());
    			e.printStackTrace();
    			return "Failed to post";
    		}
    	}
    	
    	@Override
    	protected void onProgressUpdate(Integer... values){
    		super.onProgressUpdate(values);
    	}
    	
    	@Override
    	protected void onPostExecute(String result){
    		Toast.makeText(StatusActivity.this, result, Toast.LENGTH_LONG).show();
    	}
    }
    
    public void onClick(View v){
//    	getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    	InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    	imm.hideSoftInputFromWindow(editText.getWindowToken(),0); 

    	
    	String status = editText.getText().toString();
    	new PostToTwitter().execute(status);
    	Log.d("TAG", "onClicked");
    	
    }
    
    public void afterTextChanged(Editable statusText){
    	int count = 140 - statusText.length();
    	textCount.setText( Integer.toString(count) );
    	textCount.setTextColor( Color.GREEN );
    	
    	if( count < 10 ){
    		textCount.setTextColor( Color.YELLOW);
    	}
    	
    	if( count < 0 ) {
    		textCount.setTextColor( Color.RED );
    	}
    }
    
    
    public void beforeTextChanged(CharSequence s, int start, int count , int after ) {
    }
    
    public void onTextChanged(CharSequence s, int start, int before, int count){
    	
    }
}