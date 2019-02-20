package de.unikassel.chefcoders.codecampkitchen.ui.multithreading;

import android.os.AsyncTask;

public class ResultAsyncTask<T> extends AsyncTask<Void, Void, T>
{
	public interface ResultRunnable<U>
	{
		U run();
	}

	public interface ParamRunnable<U>
	{
		void run(U v);
	}

	private ResultRunnable<T> backgroundRunnable;
	private ParamRunnable<T> postExeRunnable;

	public ResultAsyncTask(ResultRunnable<T> backgroundRunnable, ParamRunnable<T> postExeRunnable)
	{
		this.backgroundRunnable = backgroundRunnable;
		this.postExeRunnable = postExeRunnable;
	}

	public static <T> void exeResultAsyncTask(ResultRunnable<T> backgroundRunnable,
	                                          ParamRunnable<T> postExeRunnable)
	{
		new ResultAsyncTask<T>(backgroundRunnable, postExeRunnable)
				.execute();
	}

	@Override
	protected T doInBackground(Void... voids)
	{
		return this.backgroundRunnable.run();
	}

	@Override
	protected void onPostExecute(T t)
	{
		this.postExeRunnable.run(t);
	}
}
