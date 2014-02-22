package fr.obeo.releng.targetplatform.serializer;

import com.google.inject.Inject;
import com.google.inject.Provider;
import fr.obeo.releng.targetplatform.services.TargetPlatformGrammarAccess;
import fr.obeo.releng.targetplatform.targetplatform.IU;
import fr.obeo.releng.targetplatform.targetplatform.Location;
import fr.obeo.releng.targetplatform.targetplatform.TargetPlatform;
import fr.obeo.releng.targetplatform.targetplatform.TargetplatformPackage;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.serializer.acceptor.ISemanticSequenceAcceptor;
import org.eclipse.xtext.serializer.diagnostic.ISemanticSequencerDiagnosticProvider;
import org.eclipse.xtext.serializer.diagnostic.ISerializationDiagnostic.Acceptor;
import org.eclipse.xtext.serializer.sequencer.AbstractDelegatingSemanticSequencer;
import org.eclipse.xtext.serializer.sequencer.GenericSequencer;
import org.eclipse.xtext.serializer.sequencer.ISemanticSequencer;
import org.eclipse.xtext.serializer.sequencer.ITransientValueService;

@SuppressWarnings("all")
public class TargetPlatformSemanticSequencer extends AbstractDelegatingSemanticSequencer {

	@Inject
	private TargetPlatformGrammarAccess grammarAccess;
	
	public void createSequence(EObject context, EObject semanticObject) {
		if(semanticObject.eClass().getEPackage() == TargetplatformPackage.eINSTANCE) switch(semanticObject.eClass().getClassifierID()) {
			case TargetplatformPackage.IU:
				if(context == grammarAccess.getIURule()) {
					sequence_IU(context, (IU) semanticObject); 
					return; 
				}
				else break;
			case TargetplatformPackage.LOCATION:
				if(context == grammarAccess.getLocationRule()) {
					sequence_Location(context, (Location) semanticObject); 
					return; 
				}
				else break;
			case TargetplatformPackage.TARGET_PLATFORM:
				if(context == grammarAccess.getTargetPlatformRule()) {
					sequence_TargetPlatform(context, (TargetPlatform) semanticObject); 
					return; 
				}
				else break;
			}
		if (errorAcceptor != null) errorAcceptor.accept(diagnosticProvider.createInvalidContextOrTypeDiagnostic(semanticObject, context));
	}
	
	/**
	 * Constraint:
	 *     (ID=QualifiedName (version=STRING | version=VersionRange)?)
	 */
	protected void sequence_IU(EObject context, IU semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (id=ID? uri=STRING ((options+=Option options+=Option*)? ius+=IU*)?)
	 */
	protected void sequence_Location(EObject context, Location semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (name=STRING (targetVersions+=TargetVersion targetVersions+=TargetVersion*)? (options+=Option options+=Option*)? locations+=Location*)?
	 */
	protected void sequence_TargetPlatform(EObject context, TargetPlatform semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
}