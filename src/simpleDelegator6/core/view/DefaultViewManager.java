package simpleDelegator6.core.view;

import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JRootPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import simpleDelegator6.api.view.ViewContainerFrame;
import simpleDelegator6.api.view.ViewException;
import simpleDelegator6.api.view.ViewManagerException;
import simpleDelegator6.api.view.perspective.Perspective;
import simpleDelegator6.application.Application;
import simpleDelegator6.frame.AbstractViewContainerFrame;
//https://github.com/mariogarcia/viewa/blob/c39f7f46dc39908bd23cd4ded0b60c5f555617b8/core/src/main/java/org/viewaframework/view/DefaultViewManager.java
public class DefaultViewManager extends AbstractViewManager
{
	private static final Logger logger = LoggerFactory.getLogger(DefaultViewManager.class);
	private ViewContainerFrame rootView;
	private Perspective perspective;
	/**
	 * Default Constructor
	 */
	public DefaultViewManager(){
		super();
	}

	/**
	 * This constructor gives the manager the application instance as well as
	 * the initial perspective.
	 * 
	 * @param app The application instance
	 * @param perspective The current perspective
	 */
	public DefaultViewManager(Application app,Perspective perspective){
		this.setApplication(app);
		try {
			this.setPerspective(perspective);
		} catch (ViewException e) {
			e.printStackTrace();
		}
		
	}
	public Container arrangeViews() throws ViewManagerException 
	{
		logger.debug("arrange_views_started");
		JFrame 				rootContainer 	= null;
		Container 			container 		= null;
		ViewContainerFrame 	rootView 		= null; 	


		try 
		{
		rootView 		= this.getRootView();
			
		rootContainer 	= rootView.getFrame();
		
		container 		= super.arrangeViews();
		
		
		rootContainer.setContentPane(rootView.getRootPane());
		logger.debug("check_post_2");
		((JRootPane)rootContainer.getContentPane()).getContentPane().add(container);
		logger.debug("check_post_3");
		this.addView(rootView);		
		logger.debug("check_post_4");
		} catch (ViewManagerException | ViewException e) {
			throw new ViewManagerException();
		}
		logger.debug("arrange_views_finished");	
		return rootContainer;
	}
	public ViewContainerFrame getRootView() {
		if (rootView == null){
			rootView = new AbstractViewContainerFrame(){
				//Just for instancing it
			};
			rootView.setApplication(this.getApplication());
		}
		return rootView;
	}
	/**
	 * @param rootView the rootView to set
	 */
	public void setRootView(ViewContainerFrame rootView) {
		this.rootView = rootView;
	}
	/* (non-Javadoc)
	 * @see org.viewa.view.ViewManager#getPerspective()
	 */
	public Perspective getPerspective() {
		return this.perspective;
	}
	/* (non-Javadoc)
	 * @see org.viewa.view.ViewManager#setViewLayout(org.viewa.view.ViewLayout)
	 */
	public void setPerspective(Perspective viewLayout) throws ViewException {
		if (this.getPerspective() == null){
			this.perspective = viewLayout;
		} else {
			this.getPerspective().clear();
			this.perspective = viewLayout;
		}
	}

}
