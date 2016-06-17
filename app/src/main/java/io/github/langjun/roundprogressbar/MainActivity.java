package io.github.langjun.roundprogressbar;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	private final String TAG = "MainActivity";

	private RoundProgressBar mProgressBar;
	private IUpdateStatus listener;
	private Button mReset;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mProgressBar = (RoundProgressBar) findViewById(R.id.bar);
		listener = new IUpdateStatus() {
			public int progress = 0;

			@Override
			public void onProgress() {
				mProgressBar.setProgress(progress);
				progress += 1;

				// DEMO 中达到最大值重新随机生成进度值
				if (mProgressBar.getProgress() >= mProgressBar.getMax()) {
					resetProgress();
				}
			}

			@Override
			public void resetProgress() {
				progress = (int) Math.floor(Math.random() * mProgressBar.getMax()) + 1;
				Log.i(TAG, "Random=" + progress);
			}
		};

		mReset = (Button) findViewById(R.id.reset);
		mReset.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.resetProgress();
			}
		});

		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {

					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					listener.onProgress();
				}

			}
		}).start();
	}

	public interface IUpdateStatus {
		void onProgress();

		void resetProgress();
	}
}
