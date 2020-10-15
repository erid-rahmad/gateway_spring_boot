package com.multipolar.sumsel.kasda.kasdagateway.servlet.filter;

public class FeatureContextHolder {

	private static ThreadLocalFeatureContextHolderStrategy strategy;
	private static int initializeCount = 0;
	
	static {
		initialize();
	}
	
	private static void initialize() {
		strategy = new ThreadLocalFeatureContextHolderStrategy();
		initializeCount++;
	}
	
	public static void clearContext() {
		strategy.clearContext();
	}
	
	public static FeatureContext getContext() {
		return strategy.getContext();
	}
	
	public static int getInitializeCount() {
		return initializeCount;
	}
	
	public static void setContext(FeatureContext context) {
		strategy.setContext(context);
	}
}
