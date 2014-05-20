package fr.insee.bean;

import java.util.concurrent.Executor;


public class ExecutorDecorator implements Executor {

	@Override
	public void execute(Runnable command) {
		command.run();
	}
}
