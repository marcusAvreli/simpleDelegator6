package simpleDelegator6.core.controller;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import simpleDelegator6.api.controller.ViewController;
import simpleDelegator6.api.controller.ViewControllerDispatcher;
import simpleDelegator6.api.view.ViewContainer;

/**
 * 
 * This is the class which is responsible for matching all the declared listeners
 * to the components that are going to use them. This class is going to collect
 * all the listeners and inject them in the components of a given view depending
 * on the name of the view and the component.<br/><br/>
 * 
 * Probably this class is going to be moved from the application context to
 * the view context in order to make the framework more modular.
 * 
 * @author Mario Garcia
 * @since 1.0
 *
 */
public class DefaultViewControllerDispatcher implements ViewControllerDispatcher {
	private static final Logger logger = LoggerFactory.getLogger(DefaultViewControllerDispatcher.class);
	private Map<String,ViewController<? extends EventListener,? extends EventObject>> controllers;

	/**
	 * 
	 */
	public DefaultViewControllerDispatcher(){
		logger.info("DefaultViewControllerDispatcher");
		this.controllers = new HashMap<String, ViewController<? extends EventListener,? extends EventObject>>();		
	}
	
	/* (non-Javadoc)
	 * @see org.viewaframework.controller.ViewControllerDispatcher#addController(java.lang.String, org.viewaframework.controller.ViewController)
	 */
	public void addController(String pathExpression,ViewController<? extends EventListener, ? extends EventObject> controller) {
		logger.info("addController");
		this.getControllers().put(pathExpression, controller);
	}

	/* (non-Javadoc)
	 * @see org.viewaframework.controller.ViewControllerDispatcher#getControllers()
	 */
	public Map<String, ViewController<? extends EventListener,? extends EventObject>> getControllers() {
		logger.info("getControllers");
		return this.controllers;
	}
	
	/**
	 * This method loops all the components inserted in the controllers general list and
	 * only takes those from the view given as parameter.
	 * 
	 * @param view The view whose listeners we want to collect
	 * @return The listeners that belongs to the view
	 */	
	public Map<String,List<ViewController<? extends EventListener,? extends EventObject>>> getViewControllers(ViewContainer view) {
		logger.info("getViewControllers");
		String viewId = view.getId();
		Map<String,List<ViewController<? extends EventListener,? extends EventObject>>> 	componentControllerList = new HashMap<String,List<ViewController<? extends EventListener,? extends EventObject>>>();		
	 /* Looping through all controllers */
		for (String st : this.getControllers().keySet()){
		 /* If the next controller belongs to the view... */
			if (st!=null && st.startsWith(viewId)){
			 /* Then we get the controller list for this component and then add this controller */
				List<ViewController<? extends EventListener,? extends EventObject>> componentListeners = componentControllerList.get(st);
					if (componentListeners != null){
						componentListeners.add(this.getControllers().get(st));
					} else {
					 /* If this component doesn't have any listener yet, then we create the listener list */
						List<ViewController<? extends EventListener,? extends EventObject>> newList = new ArrayList<ViewController<? extends EventListener,? extends EventObject>>();
						newList.add(this.getControllers().get(st));
					 /* Controllers are going to be mapped as viewId.componentName */
						componentControllerList.put(st,newList);
					}
			}			
		}			
		return componentControllerList;
	}

	/* (non-Javadoc)
	 * @see org.viewaframework.controller.ViewControllerDispatcher#removeController(java.lang.String)
	 */
	public void removeController(String pathExpression) {
		logger.info("removeController");
		this.getControllers().remove(pathExpression);		
	}

	/* (non-Javadoc)
	 * @see org.viewaframework.controller.ViewControllerDispatcher#setControllers(java.util.Map)
	 */
	public void setControllers(Map<String, ViewController<? extends EventListener,? extends EventObject>> controllers) {
		logger.info("setControllers");
		this.controllers = controllers;
	}

}