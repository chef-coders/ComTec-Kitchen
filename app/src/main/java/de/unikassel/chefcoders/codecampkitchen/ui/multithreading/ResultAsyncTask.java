package de.unikassel.chefcoders.codecampkitchen.ui.multithreading;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ResultAsyncTask<T> extends AsyncTask<Void, Void, T>
{
	private final Context     context;
	private final Supplier<T> backgroundRunnable;
	private final Consumer<T> postExeRunnable;

	private Exception error;

	private ResultAsyncTask(Context context, Supplier<T> backgroundRunnable, Consumer<T> postExeRunnable)
	{
		this.context = context;
		this.backgroundRunnable = backgroundRunnable;
		this.postExeRunnable = postExeRunnable;
	}

	public static <T> void execute(Context context, Supplier<T> backgroundRunnable, Consumer<T> postExeRunnable)
	{
		new ResultAsyncTask<>(context, backgroundRunnable, postExeRunnable).execute();
	}

	@Override
	protected T doInBackground(Void... voids)
	{
		try
		{
			return this.backgroundRunnable.get();
		}
		catch (Exception ex)
		{
			this.error = ex;
			return null;
		}
	}

	@Override
	protected void onPostExecute(T t)
	{
		if (this.error != null)
		{
			Toast.makeText(this.context, this.error.getMessage(), Toast.LENGTH_LONG).show();
			this.error.printStackTrace();
			return;
		}

		this.postExeRunnable.accept(t);
	}
}
