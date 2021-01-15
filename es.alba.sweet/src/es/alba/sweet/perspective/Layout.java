package es.alba.sweet.perspective;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.annotation.PostConstruct;

import org.eclipse.e4.ui.internal.workbench.E4XMIResourceFactory;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import es.alba.sweet.EclipseUI;
import es.alba.sweet.core.DirectoryLocator;
import es.alba.sweet.core.IconLoader;
import es.alba.sweet.core.constant.Directory;
import es.alba.sweet.core.constant.Id;
import es.alba.sweet.core.output.Output;

@SuppressWarnings("restriction")
public class Layout extends AToolControl {

	private PerspectiveConfiguration	configuration;

	private Combo						combo;
	private Label						help;
	private Button						loadLayout;
	private Button						saveLayout;
	private Button						deleteLayout;
	private Button						renameLayout;

	private ControlDecoration			decoratorChanged;

	private String						previousSelection;

	private LayoutSelectionAdapter		layoutSelectionAdapter	= new LayoutSelectionAdapter();

	private static boolean				firstSelected			= true;

	@PostConstruct
	public void createGui(Composite parent) {
		super.set(parent);

		GridLayout compositeLayout = new GridLayout();
		compositeLayout.horizontalSpacing = 10;
		compositeLayout.numColumns = 7;
		compositeLayout.marginLeft = 30;
		composite.setLayout(compositeLayout);

		Label layout = new Label(composite, SWT.NONE | SWT.BOTTOM);
		layout.setText("Layout");

		combo = new Combo(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
		combo.addSelectionListener(layoutSelectionAdapter);

		// create the decoration for the text UI component
		decoratorChanged = new ControlDecoration(combo, SWT.TOP | SWT.LEFT);

		// re-use an existing image
		Image image = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_WARNING).getImage();
		// set description and image
		decoratorChanged.setDescriptionText("Layout of the perspective has been changed but not saved");
		decoratorChanged.setImage(image);
		decoratorChanged.hide();

		loadLayout = new Button(composite, SWT.PUSH);
		loadLayout.setImage(resourceManager.createImage(IconLoader.load("load.gif")));
		loadLayout.setToolTipText("Load the current layout");
		loadLayout.addSelectionListener(new LoadlayoutAdapter());

		saveLayout = new Button(composite, SWT.PUSH);
		saveLayout.setImage(resourceManager.createImage(IconLoader.load("save.png")));
		saveLayout.setToolTipText("Save the current layout");
		saveLayout.addSelectionListener(new SaveLayoutAdapter());

		deleteLayout = new Button(composite, SWT.PUSH);
		deleteLayout.setImage(resourceManager.createImage(IconLoader.load("delete.png")));
		deleteLayout.setToolTipText("Delete the current layout in the list");
		deleteLayout.addSelectionListener(new DeleteLayoutAdapter());

		renameLayout = new Button(composite, SWT.PUSH);
		renameLayout.setImage(resourceManager.createImage(IconLoader.load("rename.png")));
		renameLayout.setToolTipText("Rename the current layout");

		help = new Label(composite, SWT.NONE);
		GridData imageGridData = new GridData();
		imageGridData.widthHint = 20;
		help.setLayoutData(imageGridData);
		// set description
		String text = "Typing a name will:\n";
		text = text + "\tLoad the associated layout if the name is in the list\n";
		text = text + "\tSave the associated layout if the name is NOT in the list\n\n";
		text = text + "Selecting a name in the list will load the associated layout\n\n";
		text = text + "!!! The layout named default cannot be overwritten\n\n";
		text = text + "!!! Only characters and digits are allowed. All other characters will be removed";
		help.setToolTipText(text);
		help.setImage(resourceManager.createImage(IconLoader.load("info.png")));

	}

	public void update(PerspectiveConfiguration configuration) {
		this.configuration = configuration;
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				combo.setItems(configuration.layoutToArray());
				combo.setText(configuration.getSelectedLayout());
				composite.pack();
				previousSelection = combo.getText();
				if (combo.getText().equals(PerspectiveConfiguration.DEFAULT)) {
					deleteLayout.setEnabled(false);
				}
			}
		});
	}

	public void setDecoratorDirty() {
		decoratorChanged.show();
	}

	private void savePerspective(String layoutName) {
		Output.DEBUG.info("es.alba.sweet.perspective.Layout.savePerspective", "layout name will be saved as " + layoutName);

		// store model of the active perspective
		MPerspective activePerspective = EclipseUI.activePerspective();
		if (activePerspective == null) {
			MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "No perspective found", "Are you using perspectives?");
			// no perspective found, return
			return;
		}

		// create a resource, which is able to store e4 model elements
		E4XMIResourceFactory e4xmiResourceFactory = new E4XMIResourceFactory();
		Resource resource = e4xmiResourceFactory.createResource(null);

		// You must clone the perspective as snippet, otherwise the running
		// application would break, because the saving process of the resource
		// removes the element from the running application model
		MUIElement clonedPerspective = EclipseUI.modelService().cloneElement(activePerspective, EclipseUI.window());

		// add the cloned model element to the resource so that it may be stored
		resource.getContents().add((EObject) clonedPerspective);

		Path filename = getFilename(activePerspective.getLabel(), layoutName);
		File layoutFile = filename.toFile();
		try {
			// if file doesnt exists, then create it
			if (!layoutFile.exists()) {
				layoutFile.createNewFile();
			}
			FileOutputStream outputStream = new FileOutputStream(layoutFile);
			resource.save(outputStream, null);
			Output.MESSAGE.info("es.alba.sweet.toolbar.PerspectiveLayout.savePerspective", "Perspective layout saved in " + filename.toString());

		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	private void loadPerspective(String layoutName) {
		// get the parent perspective stack, so that the loaded perspective can be added to it.
		MPerspective activePerspective = EclipseUI.activePerspective();
		MElementContainer<MUIElement> perspectiveParent = activePerspective.getParent();

		MPerspective loadedPerspective = (layoutName.equals(PerspectiveConfiguration.DEFAULT)) ? loadDefaultPerspective(this.configuration.getId())
				: loadPerspectiveFromFile(layoutName);

		// remove the current perspective, which should be replaced by the loaded one
		List<MPerspective> alreadyPresentPerspective = EclipseUI.modelService().findElements(EclipseUI.window(), loadedPerspective.getElementId(), MPerspective.class, null);
		for (MPerspective perspective : alreadyPresentPerspective) {
			EclipseUI.modelService().removePerspectiveModel(perspective, EclipseUI.window());
		}

		// add the loaded perspective and switch to it
		perspectiveParent.getChildren().add(loadedPerspective);
		EclipseUI.partService().switchPerspective(loadedPerspective);

		String message = (layoutName.equals(PerspectiveConfiguration.DEFAULT)) ? "Default perspective loaded"
				: "Perspective " + getFilename(configuration.getLabel(), layoutName) + " loaded";
		Output.MESSAGE.info("es.alba.sweet.perspective.Layout.loadPerspective", message);
	}

	private MPerspective loadPerspectiveFromFile(String layoutName) {
		// create a resource, which is able to store e4 model elements
		E4XMIResourceFactory e4xmiResourceFactory = new E4XMIResourceFactory();
		Resource resource = e4xmiResourceFactory.createResource(null);

		try (FileInputStream inputStream = new FileInputStream(getFilename(configuration.getLabel(), layoutName).toFile())) {

			// load the stored model element
			resource.load(inputStream, null);

			if (!resource.getContents().isEmpty()) {

				// after the model element is loaded it can be obtained from the contents of the resource
				MPerspective loadedPerspective = (MPerspective) resource.getContents().get(0);

				return loadedPerspective;
			}
		} catch (IOException e) {
			Output.MESSAGE.error("es.alba.sweet.perspective.Layout.loadPerspective", "Error loading perspective " + getFilename(configuration.getLabel(), layoutName));
			MPerspective loadedPerspective = loadDefaultPerspective(this.configuration.getId());
			return loadedPerspective;
		}
		return null;
	}

	public MPerspective loadDefaultPerspective(String perspectiveId) {
		EModelService modelService = EclipseUI.modelService();
		EPartService partService = EclipseUI.partService();
		MApplication application = EclipseUI.application();

		MUIElement element = modelService.findSnippet(application, perspectiveId);
		if (element instanceof MPerspective) {
			MPerspective perspective = (MPerspective) element;
			if (perspective.isVisible()) {
				MPerspective perspectiveClone = (MPerspective) modelService.cloneSnippet(application, perspective.getElementId(), null);

				// Find the output part stack
				String outputId = String.join(".", Id.PARTSTACK, perspectiveClone.getLabel().toLowerCase(), Id.OUTPUT);
				MPartStack outputPartStack = (MPartStack) modelService.find(outputId, perspectiveClone);

				List<String> outputPartIds = Id.OUTPUT_PART_IDS;

				for (String partId : outputPartIds) {
					MPart part = partService.createPart(partId);
					outputPartStack.getChildren().add((MPart) modelService.cloneElement(part, application));
				}

				// remove partStack is no children are to be rendered/visible
				List<MPartStack> partStacks = modelService.findElements(perspectiveClone, null, MPartStack.class);
				for (MPartStack partStack : partStacks) {
					int n = modelService.countRenderableChildren(partStack);
					if (n == 0) partStack.setToBeRendered(false);
				}

				return perspectiveClone;
			}
		}
		return null;
	}

	private Path getFilename(String perspectiveName, String layoutName) {
		String filename = perspectiveName.toLowerCase() + "_" + layoutName + ".xml";
		Path currentDir = DirectoryLocator.findPath(Directory.CONFIG);
		return Paths.get(currentDir.toString(), filename);
	}

	private class LayoutSelectionAdapter extends SelectionAdapter {

		// Selecting a name in the list
		public void widgetSelected(SelectionEvent e) {
			String selectedLayout = combo.getText();
			// if (selectedLayout.equals(previousSelection) && firstSelected) {
			// firstSelected = false;
			// return;
			// }
			if (selectedLayout.equals(previousSelection) && !decoratorChanged.isVisible()) return;

			if (decoratorChanged.isVisible()) {
				String savedLayoutName = LayoutModificationDialog.SaveConfirmation(selectedLayout);
				if (savedLayoutName != null) {
					savePerspective(savedLayoutName);
					List<String> items = List.of(combo.getItems());
					if (!items.contains(savedLayoutName)) combo.add(savedLayoutName);
					configuration.add(savedLayoutName);
				}
			}

			loadPerspective(selectedLayout);
			previousSelection = selectedLayout;
			decoratorChanged.hide();

			if (selectedLayout.equals(PerspectiveConfiguration.DEFAULT)) {
				deleteLayout.setEnabled(false);
				return;
			}
			deleteLayout.setEnabled(true);

		}
	}

	private class LoadlayoutAdapter extends SelectionAdapter {

		public void widgetSelected(SelectionEvent e) {
			String selectedLayout = combo.getText();

			if (selectedLayout.equals(previousSelection) && !decoratorChanged.isVisible()) return;

			if (decoratorChanged.isVisible()) {
				String savedLayoutName = LayoutModificationDialog.SaveConfirmation(previousSelection);
				if (savedLayoutName != null) {
					savePerspective(savedLayoutName);
					List<String> items = List.of(combo.getItems());
					if (!items.contains(savedLayoutName)) combo.add(savedLayoutName);
					configuration.add(savedLayoutName);
				}
			}

			loadPerspective(selectedLayout);
			previousSelection = selectedLayout;
			decoratorChanged.hide();

			if (selectedLayout.equals(PerspectiveConfiguration.DEFAULT)) {
				deleteLayout.setEnabled(false);
				return;
			}
			deleteLayout.setEnabled(true);
		}
	}

	private class SaveLayoutAdapter extends SelectionAdapter {

		// Selecting a name in the list
		public void widgetSelected(SelectionEvent e) {
			String selectedLayout = combo.getText();
			String savedLayoutName = LayoutModificationDialog.CheckLayoutName(selectedLayout);

			if (savedLayoutName == null) {
				Output.DEBUG.info("es.alba.sweet.perspective.Layout.SavelayoutAdapter.widgetSelected", "Layout will not be saved");
				return;
			}

			savePerspective(savedLayoutName);

			List<String> items = List.of(combo.getItems());
			if (!items.contains(savedLayoutName)) combo.add(savedLayoutName);
			combo.setText(savedLayoutName);
			decoratorChanged.hide();
			configuration.add(savedLayoutName);
			configuration.setSelectedLayout(savedLayoutName);
			previousSelection = savedLayoutName;

			if (selectedLayout.equals(PerspectiveConfiguration.DEFAULT)) {
				deleteLayout.setEnabled(false);
				return;
			}
			deleteLayout.setEnabled(true);

		}
	}

	private class DeleteLayoutAdapter extends SelectionAdapter {

		// Selecting a name in the list
		public void widgetSelected(SelectionEvent e) {
			String selectedLayout = combo.getText();

			DeleteLayoutChoice delete = LayoutModificationDialog.deleteLayout(selectedLayout);

			if (delete.isButtonChoice()) {
				System.out.println(combo.getSelectionIndex());
				combo.remove(selectedLayout);

				if (delete.isDiskChoice()) {
					MPerspective activePerspective = EclipseUI.activePerspective();
					Path path = getFilename(activePerspective.getLabel(), selectedLayout).toAbsolutePath();
					try {
						Files.delete(path);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}

}