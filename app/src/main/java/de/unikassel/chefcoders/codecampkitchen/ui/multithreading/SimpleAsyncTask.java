package de.unikassel.chefcoders.codecampkitchen.ui.multithreading;

import android.os.AsyncTask;

public class SimpleAsyncTask extends AsyncTask<Void, Void, Void>
{
	private Runnable backgroundRunnable;
	private Runnable postExecuteRunnable;

	public SimpleAsyncTask(Runnable backgroundRunnable, Runnable postExecuteRunnable)
	{
		this.backgroundRunnable = backgroundRunnable;
		this.postExecuteRunnable = postExecuteRunnable;
	}

	@Override
	protected Void doInBackground(Void... voids)
	{
		this.backgroundRunnable.run();
		return null;
	}

	@Override
	protected void onPostExecute(Void v)
	{
		this.postExecuteRunnable.run();
	}
}
