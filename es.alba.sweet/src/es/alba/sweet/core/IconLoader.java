package es.alba.sweet.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.resource.ImageDescriptor;

public class IconLoader {

	public final static String ICONS = "icons" + File.separator;

	public static ImageDescriptor load(String filename) {
		URL url = DirectoryLocator.findDirectory(Paths.get(ICONS));

		try {
			URL fileUrl = Paths.get(new File(FileLocator.toFileURL(url).getPath()).getAbsolutePath(), filename).toUri().toURL();
			return ImageDescriptor.createFromURL(fileUrl);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}
}
