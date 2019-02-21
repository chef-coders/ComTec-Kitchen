package de.unikassel.chefcoders.codecampkitchen.ui.multithreading;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class SimpleAsyncTask extends AsyncTask<Void, Void, Void>
{
	private final Context  context;
	private final Runnable backgroundRunnable;
	private final Runnable postExecuteRunnable;

	private Exception error;

	private SimpleAsyncTask(Context context, Runnable backgroundRunnable, Runnable postExecuteRunnable)
	{
		this.context = context;
		this.backgroundRunnable = backgroundRunnable;
		this.postExecuteRunnable = postExecuteRunnable;
	}

	public static void execute(Context context, Runnable backgroundRunnable, Runnable postExecuteRunnable)
	{
		new SimpleAsyncTask(context, backgroundRunnable, postExecuteRunnable).execute();
	}

	@Override
	protected Void doInBackground(Void... voids)
	{
		try
		{
			this.backgroundRunnable.run();
		}
		catch (Exception ex)
		{
			this.error = ex;
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void v)
	{
		if (this.error != null)
		{
			Toast.makeText(this.context, this.error.getMessage(), Toast.LENGTH_LONG).show();
			this.error.printStackTrace();
			return;
		}
		this.postExecuteRunnable.run();
	}
}
