package de.unikassel.chefcoders.codecampkitchen.ui.multithreading;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import de.unikassel.chefcoders.codecampkitchen.communication.errorhandling.HttpConnectionException;

public class SimpleAsyncTask extends AsyncTask<Void, Void, Void>
{
	private final Context  context;
	private final Runnable backgroundRunnable;
	private final Runnable postExecuteRunnable;

	private String errorMessage;

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
		catch (HttpConnectionException ex)
		{
			this.errorMessage = ex.smallErrorMessage();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void v)
	{
		if (this.errorMessage != null)
		{
			Toast.makeText(this.context, this.errorMessage, Toast.LENGTH_LONG).show();
			return;
		}
		this.postExecuteRunnable.run();
	}
}
