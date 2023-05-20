package simpleDelegator6.core.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;
import java.util.Map;

import javax.swing.JLayeredPane;
import javax.swing.JRootPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import simpleDelegator6.api.controller.ViewController;
import simpleDelegator6.api.view.ViewContainer;
import simpleDelegator6.api.view.ViewException;
import simpleDelegator6.api.view.delegator.Delegator;
import simpleDelegator6.api.view.event.ViewContainerEventController;
import simpleDelegator6.application.Application;
import simpleDelegator6.core.view.delegator.NamedComponentsDelegator;
import simpleDelegator6.core.view.delegator.ViewContainerControllerDelegator;
//https://github.com/mariogarcia/viewa/blob/c39f7f46dc39908bd23cd4ded0b60c5f555617b8/core/src/main/java/org/viewaframework/view/AbstractViewContainer.java
public abstract class AbstractViewContainer implements ViewContainer
{

	private static final Logger logger = LoggerFactory.getLogger(AbstractViewContainer.class);
	//private List<ViewActionDescriptor> 				actionDescriptors;
	private Application 							application;
	private List<Delegator>							delegators;
	private Image									iconImage;
	private String 									id;
	private JToolBar								jToolBar;
	private Map<String,List<Component>> 			namedComponents;
	//private ResourceBundle							messageBundle;

	private JRootPane 								rootPane;
	private String									title;
	private List<ViewContainerEventController> viewContainerEventControllers;
	
	private Map<String,List<ViewController<?,?>>> 	viewControllerMap;
	//private Map<String,ViewModel>					viewModelMap;
	//private Map<String,Object>	
	public Container getContentPane() {
		return this.getRootPane().getContentPane(); 
	}
	
								
	
	/* (non-Javadoc)
	 * @see javax.swing.RootPaneContainer#getRootPane()
	 */
	public JRootPane getRootPane() {
		if (this.rootPane == null){
			logger.debug("root_pane_is_null");
			this.rootPane = new JRootPane();
			this.rootPane.setName("ROOTPANE");
		}else {
			logger.debug("root_pane_is_not_null");
		}
		return this.rootPane;
	}
	public Application getApplication() {
		logger.debug("get_application_called");
		return application;
	}
	public void setApplication(Application application) {
		logger.debug("set_application_called");
		this.application = application;
	}
	public String getId() {
		logger.debug("get_id:"+id);
		return id;
	}
	public void setId(String id) {
		logger.debug("set_id:"+id);
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setRootPane(JRootPane rootPane) {
		this.rootPane = rootPane;
	}
	public Component getGlassPane() {
		return this.getRootPane().getGlassPane();
	}
	public void setGlassPane(Component glassPane) {
		this.getRootPane().setGlassPane(glassPane);
	}
	/* (non-Javadoc)
	 * @see javax.swing.RootPaneContainer#setLayeredPane(javax.swing.JLayeredPane)
	 */
	public void setLayeredPane(JLayeredPane layeredPane) {
		this.getRootPane().setLayeredPane(layeredPane);
	}
	/* (non-Javadoc)
	 * @see javax.swing.RootPaneContainer#getLayeredPane()
	 */
	public JLayeredPane getLayeredPane() {
		return this.getRootPane().getLayeredPane();
	}
	/* (non-Javadoc)
	 * @see javax.swing.RootPaneContainer#setContentPane(java.awt.Container)
	 */
	public void setContentPane(Container contentPane) {
		this.getRootPane().setContentPane(contentPane);
	}
	/* (non-Javadoc)
	 * @see javax.swing.RootPaneContainer#getContentPane()
	 */
	public AbstractViewContainer(String id){
		this();
		this.setId(id);
	}
	public AbstractViewContainer(){
		super();
		this.getContentPane().setLayout(new BorderLayout());
		this.viewContainerEventControllers = new ArrayList<ViewContainerEventController>();
	}
	
	/* (non-Javadoc)
	 * @see org.viewa.view.View#viewInit()
	 */
	public void viewInit() throws ViewException {
		
			logger.info("Initializing_view "+this.getClass().getName());
		
		//TODO refactor
		if (this.getContentPane()!=null) this.getContentPane().setName("contentPane");
		
		final ViewContainer thisContainer = this; 
		
		if (SwingUtilities.isEventDispatchThread()){
			for (Delegator delegator : this.getDelegators()){
				delegator.inject(thisContainer);
			}
			//thisContainer.viewInitUIState(); 
		} else {
			SwingUtilities.invokeLater(new Runnable(){
				public void run(){
					try {						
						for (Delegator delegator : getDelegators()){
							delegator.inject(thisContainer);
						}	
						//thisContainer.viewInitUIState(); 
					} catch (ViewException e) {
						logger.error(e.getMessage());
					}					
				}
			});
		}
		
	}
	public Component getComponentByName(String name){
		List<Component> components 		= this.getComponentsByName(name);
		Component 		componentResult = components!=null && components.size() > 0 ? components.get(0) : null; 
		return componentResult;
	}
	/* (non-Javadoc)
	 * @see org.viewaframework.view.ComponentAware#getComponentByName(java.lang.String)
	 */
	public List<Component> getComponentsByName(String name) {
		logger.debug("name:"+name);
		logger.debug("this.getNamedComponents():"+this.getNamedComponents());
		return this.getNamedComponents().get(name);
	}
	/* (non-Javadoc)
	 * @see org.viewaframework.view.ComponentAware#getNamedComponents()
	 */
	public Map<String, List<Component>> getNamedComponents() {
		return this.namedComponents;
	}
	/**
	 * @param namedComponents
	 */
	public void setNamedComponents(Map<String, List<Component>> namedComponents) {
		this.namedComponents = namedComponents;
	}
	/* (non-Javadoc)
	 * @see org.viewaframework.controller.ViewControllerAware#setViewControllerMap(java.util.Map)
	 */
	public void setViewControllerMap(Map<String, List<ViewController<? extends EventListener, ? extends EventObject>>> viewControllerMap) {
		this.viewControllerMap = viewControllerMap;
	}
	/* (non-Javadoc)
	 * @see org.viewaframework.controller.ViewControllerAware#getViewControllerMap()
	 */
	public Map<String, List<ViewController<? extends EventListener, ? extends EventObject>>> getViewControllerMap() {
		return this.viewControllerMap;
	}
	/* (non-Javadoc)
	 * @see org.viewaframework.view.ViewContainerEventControllerAware#setViewContainerListeners(java.util.List)
	 */
	public void setViewContainerListeners(List<ViewContainerEventController> listeners) {
		this.viewContainerEventControllers = listeners;
	}
	/* (non-Javadoc)
	 * @see org.viewaframework.view.ViewContainerEventControllerAware#getViewContainerListeners()
	 */
	public List<ViewContainerEventController> getViewContainerListeners() {
		return this.viewContainerEventControllers;
	}
	/* (non-Javadoc)
	 * @see org.viewaframework.view.event.ViewContainerEventControllerAware#removeViewContainerListener(org.viewaframework.view.event.ViewContainerEventController)
	 */
	public void removeViewContainerListener(ViewContainerEventController listener){
		this.viewContainerEventControllers.remove(listener);
	}
	/* (non-Javadoc)
	 * @see org.viewaframework.view.ViewContainerEventControllerAware#addViewContainerListener(org.viewaframework.view.ViewContainerEventController)
	 */
	public void addViewContainerListener(ViewContainerEventController listener){
		this.viewContainerEventControllers.add(listener);
	}
	public List<Delegator> getDelegators() {
		if (delegators == null){
			this.delegators = new ArrayList<Delegator>(Arrays.asList(
			 /* ActionDescriptor must always be the first delegator because once it has been injected
			  * all initial java.awt.Component are available, like the JToolBar and the JMenuBar */
					new NamedComponentsDelegator(),
				new ViewContainerControllerDelegator()
				));
		}
		return delegators;
	}
	/* (non-Javadoc)
	 * @see org.viewaframework.view.delegator.DelegatorAware#addDelegator(org.viewaframework.view.delegator.Delegator)
	 */
	public void addDelegator(Delegator delegator) {
		this.getDelegators().add(delegator);
	}
	/* (non-Javadoc)
	 * @see org.viewaframework.view.delegator.DelegatorAware#removeDelegator(org.viewaframework.view.delegator.Delegator)
	 */
	public void removeDelegator(Delegator delegator) {
		this.getDelegators().remove(delegator);
	}
	/* (non-Javadoc)
	 * @see org.viewaframework.view.delegator.DelegatorAware#setDelegators(java.util.List)
	 */
	public void setDelegators(List<Delegator> delegators) {
		this.delegators = delegators;
	}
}
