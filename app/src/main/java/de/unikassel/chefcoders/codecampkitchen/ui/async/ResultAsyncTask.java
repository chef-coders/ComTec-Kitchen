package de.unikassel.chefcoders.codecampkitchen.ui.async;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import de.unikassel.chefcoders.codecampkitchen.communication.errorhandling.HttpConnectionException;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ResultAsyncTask<T> extends AsyncTask<Void, Void, T>
{
	// =============== Fields ===============

	private final Supplier<? extends T>       backgroundRunnable;
	private final Consumer<? super T>         successHandler;
	private final Consumer<? super Exception> exceptionHandler;
	private final Runnable                    completionHandler;

	private Exception exception;

	// =============== Constructors ===============

	private ResultAsyncTask(Supplier<? extends T> backgroundRunnable, Consumer<? super T> successHandler,
		Consumer<? super Exception> exceptionHandler, Runnable completionHandler)
	{
		this.backgroundRunnable = backgroundRunnable;
		this.successHandler = successHandler;
		this.exceptionHandler = exceptionHandler;
		this.completionHandler = completionHandler;
	}

	// =============== Static Methods ===============

	public static <T> void execute(Context context, Supplier<? extends T> backgroundTask,
		Consumer<? super T> successHandler)
	{
		execute(backgroundTask, successHandler, defaultExceptionHandler(context), null);
	}

	public static <T> void execute(Context context, Supplier<? extends T> backgroundTask,
		Consumer<? super T> successHandler, Runnable completionHandler)
	{
		execute(backgroundTask, successHandler, defaultExceptionHandler(context), completionHandler);
	}

	public static <T> void execute(Supplier<? extends T> backgroundTask, Consumer<? super T> successHandler,
		Consumer<? super Exception> exceptionHandler)
	{
		execute(backgroundTask, successHandler, exceptionHandler, null);
	}

	public static <T> void execute(Supplier<? extends T> backgroundTask, Consumer<? super T> successHandler,
		Consumer<? super Exception> exceptionHandler, Runnable completionHandler)
	{
		new ResultAsyncTask<>(backgroundTask, successHandler, exceptionHandler, completionHandler).execute();
	}

	private static Consumer<Exception> defaultExceptionHandler(Context context)
	{
		return exception -> defaultHandleException(context, exception);
	}

	public static void defaultHandleException(Context context, Exception exception)
	{
		exception.printStackTrace();
		if (exception instanceof HttpConnectionException)
		{
			final String message = ((HttpConnectionException) exception).smallErrorMessage();
			Toast.makeText(context, message, Toast.LENGTH_LONG).show();
		}
	}

	// =============== Methods ===============

	@Override
	protected T doInBackground(Void... voids)
	{
		try
		{
			return this.backgroundRunnable.get();
		}
		catch (Exception ex)
		{
			this.exception = ex;
			return null;
		}
	}

	@Override
	protected void onPostExecute(T t)
	{
		if (this.exception != null)
		{
			this.exceptionHandler.accept(this.exception);
		}
		else
		{
			this.successHandler.accept(t);
		}

		if (this.completionHandler != null)
		{
			this.completionHandler.run();
		}
	}
}
