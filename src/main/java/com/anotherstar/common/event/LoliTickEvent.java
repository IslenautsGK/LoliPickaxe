package com.anotherstar.common.event;

import java.util.Queue;
import java.util.TimerTask;

import com.google.common.collect.Queues;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;

public class LoliTickEvent {

	private static final Queue<TimerTask> tickStartTasks = Queues.newArrayDeque();
	private static final Queue<TimerTask> tickEndTasks = Queues.newArrayDeque();

	@SubscribeEvent
	public void onServerTick(ServerTickEvent event) {
		if (event.phase == Phase.START) {
			synchronized (tickStartTasks) {
				while (!tickStartTasks.isEmpty()) {
					tickStartTasks.poll().run();
				}
			}
		} else {
			synchronized (tickEndTasks) {
				while (!tickEndTasks.isEmpty()) {
					tickEndTasks.poll().run();
				}
			}
		}
	}

	public static void addTask(TimerTask task, Phase phase) {
		if (phase == Phase.START) {
			synchronized (tickStartTasks) {
				tickStartTasks.add(task);
			}
		} else {
			synchronized (tickEndTasks) {
				tickEndTasks.add(task);
			}
		}
	}

	@FunctionalInterface
	public interface TickFun {

		void invok();

	}

	public static class TickStartTask extends TimerTask {

		private int tick;
		private TickFun fun;

		public TickStartTask(int tick, TickFun fun) {
			this.tick = tick;
			this.fun = fun;
		}

		@Override
		public void run() {
			if (--tick > 0) {
				addTask(new TickEndTask(this), Phase.END);
			} else {
				fun.invok();
			}
		}

	}

	public static class TickEndTask extends TimerTask {

		private TimerTask nextTask;

		public TickEndTask(TimerTask nextTask) {
			this.nextTask = nextTask;
		}

		@Override
		public void run() {
			addTask(nextTask, Phase.START);
		}

	}

}
