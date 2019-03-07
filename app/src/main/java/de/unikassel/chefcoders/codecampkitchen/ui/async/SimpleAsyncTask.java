package de.unikassel.chefcoders.codecampkitchen.ui.async;

import android.content.Context;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class SimpleAsyncTask
{
	private SimpleAsyncTask()
	{
		// no instances
	}

	public static void execute(Context context, Runnable backgroundTask, Runnable successHandler)
	{
		ResultAsyncTask.execute(context, voidSupplier(backgroundTask), voidConsumer(successHandler));
	}

	public static void execute(Context context, Runnable backgroundTask, Runnable successHandler,
		Runnable completionHandler)
	{
		ResultAsyncTask.execute(context, voidSupplier(backgroundTask), voidConsumer(successHandler), completionHandler);
	}

	public static void execute(Runnable backgroundTask, Runnable successHandler,
		Consumer<? super Exception> exceptionHandler)
	{
		ResultAsyncTask.execute(voidSupplier(backgroundTask), voidConsumer(successHandler), exceptionHandler);
	}

	public static void execute(Runnable backgroundTask, Runnable successHandler,
		Consumer<? super Exception> exceptionHandler, Runnable completionHandler)
	{
		ResultAsyncTask
			.execute(voidSupplier(backgroundTask), voidConsumer(successHandler), exceptionHandler, completionHandler);
	}

	private static Supplier<Object> voidSupplier(Runnable runnable)
	{
		return () -> {
			runnable.run();
			return null;
		};
	}

	private static Consumer<Object> voidConsumer(Runnable runnable)
	{
		return ignored -> runnable.run();
	}
}
