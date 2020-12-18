package es.alba.sweet.perspective;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
import org.eclipse.nebula.widgets.opal.dialog.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
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

	private ControlDecoration			decorator;

	private String						previousSelection;

	@PostConstruct
	public void createGui(Composite parent) {
		super.set(parent);

		GridLayout compositeLayout = new GridLayout();
		compositeLayout.horizontalSpacing = 10;
		compositeLayout.numColumns = 3;
		composite.setLayout(compositeLayout);

		Label layout = new Label(composite, SWT.NONE | SWT.BOTTOM);
		layout.setText("Layout");

		combo = new Combo(composite, SWT.BORDER);
		combo.addSelectionListener(new SelectionAdapter() {
			public void widgetDefaultSelected(SelectionEvent e) {
				System.out.println("widgetDefaultSelected " + combo.getText());
				System.out.println("widgetDefaultSelected " + e);
				List<String> items = List.of(combo.getItems());
				if (!items.contains(combo.getText())) {
					String layout = combo.getText();
					combo.add(layout);
					configuration.getLayout().add(layout);
					configuration.setSelectedLayout(layout);
					savePerspective(layout);// save perspective layout
					decorator.hide();
					return;
				}

				// load perspective layout
				loadPerspective(combo.getText());
			}

			public void widgetSelected(SelectionEvent e) {
				System.out.println("widgetSelected " + combo.getText());
				System.out.println("widgetSelected " + e);

				loadPerspective(combo.getText());
			}

		});

		// create the decoration for the text UI component
		decorator = new ControlDecoration(combo, SWT.TOP | SWT.LEFT);

		// re-use an existing image
		Image image = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage();
		// set description and image
		decorator.setDescriptionText("Layout of the perspective has been changed but not saved");
		decorator.setImage(image);
		decorator.hide();

		Button save = new Button(composite, SWT.PUSH);
		save.setToolTipText("Save the perspective layout into a file");
		save.setImage(resourceManager.createImage(IconLoader.load("save.png")));

	}

	public void update(PerspectiveConfiguration configuration) {
		this.configuration = configuration;
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				combo.setItems(configuration.layoutToArray());
				combo.setText(configuration.getSelectedLayout());
				combo.clearSelection();
				composite.pack();
			}
		});
	}

	public void setDecoratorDirty() {
		decorator.show();
		this.previousSelection = combo.getText();
	}

	private void savePerspective(String layoutName) {
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
		if (decorator.isVisible()) {
			final boolean confirm = Dialog.isConfirmed("Are you sure you want to quit?", "Please do not quit yet!");
			System.out.println("Choice is..." + confirm);
			// MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Perspective layout changed but not saved", "Do you want to save the layout " + layoutName +
			// "?");
		}
		// create a resource, which is able to store e4 model elements
		E4XMIResourceFactory e4xmiResourceFactory = new E4XMIResourceFactory();
		Resource resource = e4xmiResourceFactory.createResource(null);

		try (FileInputStream inputStream = new FileInputStream(getFilename(configuration.getLabel(), layoutName).toFile())) {

			// load the stored model element
			resource.load(inputStream, null);

			if (!resource.getContents().isEmpty()) {

				// after the model element is loaded it can be obtained from the
				// contents of the resource
				MPerspective loadedPerspective = (MPerspective) resource.getContents().get(0);

				// get the parent perspective stack, so that the loaded
				// perspective can be added to it.
				MPerspective activePerspective = EclipseUI.activePerspective();
				MElementContainer<MUIElement> perspectiveParent = activePerspective.getParent();

				// remove the current perspective, which should be replaced by
				// the loaded one
				List<MPerspective> alreadyPresentPerspective = EclipseUI.modelService().findElements(EclipseUI.window(), loadedPerspective.getElementId(), MPerspective.class,
						null);
				for (MPerspective perspective : alreadyPresentPerspective) {
					EclipseUI.modelService().removePerspectiveModel(perspective, EclipseUI.window());
				}

				// add the loaded perspective and switch to it
				perspectiveParent.getChildren().add(loadedPerspective);

				EclipseUI.partService().switchPerspective(loadedPerspective);
				Output.MESSAGE.info("es.alba.sweet.perspective.Layout.loadPerspective", "Perspective " + getFilename(configuration.getLabel(), layoutName) + " loaded");
			}
		} catch (IOException e) {
			Output.MESSAGE.error("es.alba.sweet.perspective.Layout.loadPerspective", "Error loading perspective " + getFilename(configuration.getLabel(), layoutName));
		}
	}

	public MPerspective loadDefaultPerspective(String perspectiveId) {
		EModelService modelService = EclipseUI.modelService();
		EPartService partService = EclipseUI.partService();
		MApplication application = EclipseUI.application();

		MUIElement element = modelService.findSnippet(application, perspectiveId);
		if (element instanceof MPerspective) {
			MPerspective perspective = (MPerspective) element;
			if (perspective.isVisible()) {
				Output.MESSAGE.info("es.alba.sweet.perspective.Layout.loadDefaultPerspective", perspective.getIconURI());
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

}