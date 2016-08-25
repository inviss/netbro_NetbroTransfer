package kr.co.d2net.netbro.transfer.handlers;

import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.operations.ProvisioningJob;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.operations.Update;
import org.eclipse.equinox.p2.operations.UpdateOperation;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
// Require-Bundle: org.eclipse.equinox.p2.core|engine|operation|metadata.repository
// Feature: org.eclipse.equinox.p2.core.feature
// !!! Do not run from within IDE. Update only works in an exported product !!!
@SuppressWarnings("restriction")
public class UpdateHandler {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Inject @Preference(nodePath = "kr.co.d2net.netbro.preference", value = "updateSite")
	private String updateSite;
	
	@Execute
	public void execute(final IProvisioningAgent agent, final Shell parent,
			final UISynchronize sync, final IWorkbench workbench) {
		Job j = new Job("Update Job") {
			private boolean doInstall = false;
 
			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime
			 * .IProgressMonitor)
			 */
			@Override
			protected IStatus run(final IProgressMonitor monitor) {
 
				/* 1. Prepare update plumbing */
 
				final ProvisioningSession session = new ProvisioningSession(agent);
				final UpdateOperation operation = new UpdateOperation(session);
 
				// Create uri
				URI uri = null;
				try {
					uri = new URI(updateSite);
				} catch (final URISyntaxException e) {
					sync.syncExec(new Runnable() {
						@Override
						public void run() {
							MessageDialog.openError(parent, "URI invalid", e.getMessage());
						}
					});
					return Status.CANCEL_STATUS;
				}
 
				// Set location of artifact and metadata repo
				// (Explain difference between meta und artifact repo)
				operation.getProvisioningContext().setArtifactRepositories(new URI[] { uri });
				operation.getProvisioningContext().setMetadataRepositories(new URI[] { uri });
				
				SubMonitor sub = SubMonitor.convert(monitor, "Checking for application updates...", 200);
 
				/* 2. Check for updates */
				// Run update checks causing I/O
				final IStatus status = operation.resolveModal(sub.newChild(100));
 
				// Failed to find updates (inform user and exit)
				if (status.getCode() == UpdateOperation.STATUS_NOTHING_TO_UPDATE) {
					sync.syncExec(new Runnable() {
						@Override
						public void run() {
							MessageDialog.openWarning(parent, "No update", "No updates for the current installation have been found");
						}
					});
					return Status.CANCEL_STATUS;
				}
 
				/* 3. Ask if updates should be installed and run installation */
				// found updates, ask user if to install?
				if (status.isOK() && status.getSeverity() != IStatus.ERROR) {
					sync.syncExec(new Runnable() {
						@Override
						public void run() {
							String updates = "";
							Update[] possibleUpdates = operation.getPossibleUpdates();
							for (Update update : possibleUpdates) {
								updates += update + "\n";
							}
							if(logger.isDebugEnabled()) {
								logger.debug("update: "+updates);
							}
							if(StringUtils.isNotBlank(updates))
								doInstall = MessageDialog.openQuestion(parent, "Really install updates?", updates);
							else
								MessageDialog.openInformation(parent, "Netbro Transfer Update", "It's not exist to update a item");
								
						}
					});
				}
 
				// Install! (causing I/0)
				if (doInstall) {
					final ProvisioningJob provisioningJob = operation.getProvisioningJob(monitor);
					// Updates cannot run from within Eclipse IDE!!!
					if (provisioningJob == null) {
						System.err.println("Running update from within Eclipse IDE? This won't work!!!");
						throw new NullPointerException();
					}
 
					// Register a job change listener to track
					// installation progress and notify user upon success
					provisioningJob.addJobChangeListener(new JobChangeAdapter() {
								@Override
								public void done(IJobChangeEvent event) {
									if (event.getResult().isOK()) {
										sync.syncExec(new Runnable() {
											@Override
											public void run() {
												boolean restart = MessageDialog.openQuestion(parent,
																"Updates installed, restart?",
																"Updates have been installed successfully, do you want to restart?");
												if (restart) {
													workbench.restart();
												}
											}
										});
									}
									super.done(event);
								}
							});
 
					provisioningJob.schedule();
				}
				return Status.OK_STATUS;
			}
		};
		j.schedule();
	}
}
