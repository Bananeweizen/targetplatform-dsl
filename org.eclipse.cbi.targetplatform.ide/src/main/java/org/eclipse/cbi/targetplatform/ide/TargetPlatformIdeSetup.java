/*
 * generated by Xtext 2.11.0
 */
package org.eclipse.cbi.targetplatform.ide;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.eclipse.cbi.targetplatform.TargetPlatformRuntimeModule;
import org.eclipse.cbi.targetplatform.TargetPlatformStandaloneSetup;
import org.eclipse.xtext.util.Modules2;

/**
 * Initialization support for running Xtext languages as language servers.
 */
public class TargetPlatformIdeSetup extends TargetPlatformStandaloneSetup {

	@Override
	public Injector createInjector() {
		return Guice.createInjector(Modules2.mixin(new TargetPlatformRuntimeModule(), new TargetPlatformIdeModule()));
	}
	
}