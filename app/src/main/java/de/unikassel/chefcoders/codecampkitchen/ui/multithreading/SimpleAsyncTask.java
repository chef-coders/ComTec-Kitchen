package de.unikassel.chefcoders.codecampkitchen.ui.multithreading;

import android.content.Context;

public abstract class SimpleAsyncTask
{
	private SimpleAsyncTask()
	{
		// no instances
	}

	public static void execute(Context context, Runnable backgroundRunnable, Runnable postExecuteRunnable)
	{
		ResultAsyncTask.execute(context, () -> {
			backgroundRunnable.run();
			return null;
		}, ignored -> postExecuteRunnable.run());
	}
}
