package com.tools.togglebutton;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.tools.togglebutton.R;
import com.tools.togglebutton.view.MyToggleButton;
import com.tools.togglebutton.view.MyToggleButton.OnStateChangedListener;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MyToggleButton toggleButton = (MyToggleButton) findViewById(R.id.toggle_button);
//		toggleButton.setToggleBackground(R.drawable.slide_background, R.drawable.slide_icon);
//		toggleButton.setState(true);
		toggleButton.setOnStateChangedListener(new OnStateChangedListener() {
			
			@Override
			public void onStateChanged(boolean isOpen) {
				Toast.makeText(getApplicationContext(), isOpen ? "´ò¿ª" : "¹Ø±Õ", Toast.LENGTH_SHORT).show();
			}
		});
	}

}
