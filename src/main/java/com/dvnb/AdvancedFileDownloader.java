package com.dvnb;

import java.io.IOException;

import com.vaadin.server.ConnectorResource;
import com.vaadin.server.DownloadStream;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.Resource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.AbstractComponent;

/**
 * an advanced file downloader
 *
 * @author visruth, chautk rewrite some method
 *
 */

public class AdvancedFileDownloader extends FileDownloader {
	
	public AdvancedFileDownloader(Resource resource) {
		super(resource);
	}
	

	private static final long serialVersionUID = 7914516170514586601L;

	private AbstractComponent extendedComponent;

	private AdvancedDownloaderListener dynamicDownloaderListener;
	private DownloaderEvent downloadEvent;

	public abstract class DownloaderEvent {

		/**
		 *
		 * @return
		 */
		public abstract AbstractComponent getExtendedComponent();

		public abstract void setExtendedComponent(AbstractComponent extendedComponent);

	}

	public interface AdvancedDownloaderListener {
		/**
		 * This method will be invoked just before the download starts. Thus, a new file path can be set.
		 *
		 * @param downloadEvent
		 */
		public boolean beforeDownload(DownloaderEvent downloadEvent);
	}

	public boolean fireEvent() {

		if (this.dynamicDownloaderListener != null && this.downloadEvent != null) {
			return this.dynamicDownloaderListener.beforeDownload(this.downloadEvent);
		}
		return false;
	}

	public void addAdvancedDownloaderListener(AdvancedDownloaderListener listener) {
		if (listener != null) {
			DownloaderEvent downloadEvent = new DownloaderEvent() {

				private AbstractComponent extendedComponet;

				@Override
				public void setExtendedComponent(AbstractComponent extendedComponet) {
					this.extendedComponet = extendedComponet;
				}

				@Override
				public AbstractComponent getExtendedComponent() {
					return this.extendedComponet;
				}
			};
			downloadEvent.setExtendedComponent(AdvancedFileDownloader.this.extendedComponent);
			this.dynamicDownloaderListener = listener;
			this.downloadEvent = downloadEvent;

		}
	}

	@Override
	public boolean handleConnectorRequest(VaadinRequest request, VaadinResponse response, String path) throws IOException {

		if (!path.matches("dl(/.*)?")) {
			// Ignore if it isn't for us
			return false;
		}

		if (!AdvancedFileDownloader.this.fireEvent()) {
			return false;
		}

		VaadinSession session = getSession();

		session.lock();

		DownloadStream stream;

		try {
			Resource resource = getFileDownloadResource();
			if (!(resource instanceof ConnectorResource)) {
				return false;
			}
			stream = ((ConnectorResource) resource).getStream();

			if (stream.getParameter("Content-Disposition") == null) {
				// Content-Disposition: attachment generally forces download
				stream.setParameter("Content-Disposition", "attachment; filename=\"" + stream.getFileName() + "\"");
			}

			// Content-Type to block eager browser plug-ins from hijacking
			// the file
			if (isOverrideContentType()) {
				stream.setContentType("application/octet-stream;charset=UTF-8");
			}

		} finally {
			session.unlock();
		}
		stream.writeResponse(request, response);
		return true;

	}
}