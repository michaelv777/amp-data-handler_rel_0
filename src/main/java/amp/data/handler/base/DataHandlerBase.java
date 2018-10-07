/**
 * 
 */
package amp.data.handler.base;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.SQLQuery;
import org.hibernate.Session;	
import org.hibernate.Transaction;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import amp.common.api.impl.ToolkitDataProvider;
import amp.common.api.impl.ToolkitReflection;
import amp.common.api.impl.ToolkitSpringConfig;
import amp.jpa.entities.Category;
import amp.jpa.entities.Node;

/**
 * @author MVEKSLER
 *
 */
public abstract class DataHandlerBase implements DataHandlerI 
{
	private static final Logger cLogger = 
			LoggerFactory.getLogger(DataHandlerBase.class);
	
	protected String cFrameworkBeansConfig = "FrameworkBeans.xml";
	
	protected String cFrameworkBeansProps =  "FrameworkBeans.properties";
	
	//---class variables---------------------------------
	//@Autowired
	protected ToolkitDataProvider cToolkitDataProvider = null;
	
	private ToolkitReflection iReflection = null;
	
	protected XmlBeanFactory cBeanFactory = null;

	protected Properties cSpringProps = null;
	
	protected ApplicationContext applicationContext = null;
	
	protected HashMap<String, String> 
		cSystemConfiguration = new  HashMap<String, String>();

	protected HashMap<String, String> 
		cWorkerConfiguration = new  HashMap<String, String>();

	protected boolean lcRes = true;
	 
	//---getters/setters---------------------------------
	
	
	/**
	 * @return the cDataProvider
	 */
	public ToolkitDataProvider getcToolkitDataProvider() {
		return cToolkitDataProvider;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public HashMap<String, String> getcSystemConfiguration() {
		return cSystemConfiguration;
	}

	public void setcSystemConfiguration(HashMap<String, String> cSystemConfiguration) {
		this.cSystemConfiguration = cSystemConfiguration;
	}

	public HashMap<String, String> getcWorkerConfiguration() {
		return cWorkerConfiguration;
	}

	public void setcWorkerConfiguration(HashMap<String, String> cWorkerConfiguration) {
		this.cWorkerConfiguration = cWorkerConfiguration;
	}

	/**
	 * @return the beanFactory
	 */
	
	public XmlBeanFactory getBeanFactory() {
		return cBeanFactory;
	}
	 

	/**
	 * @return the cBeanFactory
	 */
	
	public XmlBeanFactory getcBeanFactory() {
		return cBeanFactory;
	}
	
	
	/**
	 * @param cBeanFactory the cBeanFactory to set
	 */
	
	public void setcBeanFactory(XmlBeanFactory cBeanFactory) {
		this.cBeanFactory = cBeanFactory;
	}
	
	/**
	 * @return the cSpringProps
	 */
	
	public Properties getcSpringProps() {
		return cSpringProps;
	}
	
	/**
	 * @param cSpringProps the cSpringProps to set
	 */
	
	public void setcSpringProps(Properties cSpringProps) {
		this.cSpringProps = cSpringProps;
	}
	
	
	/**
	 * @param cDataProvider the cDataProvider to set
	 */
	@Required
	public void setcToolkitDataProvider(ToolkitDataProvider cDataProvider) {
		this.cToolkitDataProvider = cDataProvider;
	}

	
	/**
	 * @return the cFrameworkBeansConfig
	 */
	public String getcFrameworkBeansConfig() {
		return cFrameworkBeansConfig;
	}

	/**
	 * @param cFrameworkBeansConfig the cFrameworkBeansConfig to set
	 */
	public void setcFrameworkBeansConfig(String cFrameworkBeansConfig) {
		this.cFrameworkBeansConfig = cFrameworkBeansConfig;
	}

	
	
	/**
	 * @return the cFrameworkBeansProps
	 */
	public String getcFrameworkBeansProps() {
		return cFrameworkBeansProps;
	}

	/**
	 * @param cFrameworkBeansProps the cFrameworkBeansProps to set
	 */
	public void setcFrameworkBeansProps(String cFrameworkBeansProps) {
		this.cFrameworkBeansProps = cFrameworkBeansProps;
	}

	

	/**
	 * @param lcRes the lcRes to set
	 */
	public void setLcRes(boolean lcRes) {
		this.lcRes = lcRes;
	}

	/**
	 * @return the lcRes
	 */
	public boolean isLcRes() {
		return lcRes;
	}
	//---class methods---------------------------------
	/**
	 * 
	 */
	public DataHandlerBase() 
	{
		boolean cRes = true;

		try
    	{
			cRes = this.initClassVariables();
			
			if ( !cRes )
			{
				cLogger.error("MappingHandler() is false!");
				
				this.setLcRes(cRes);
			}
			
    	}    		
    	catch( Exception e)
    	{
    		cLogger.error("MappingHandler:" + e.getStackTrace());
    		
    		this.setLcRes(cRes = false);
    	}
	}
	
	public DataHandlerBase(HashMap<String, String> cSystemConfiguration,
						   HashMap<String, String> cWorkerConfiguration) 
	{
		boolean cRes = true;

		try
    	{
			this.setcSystemConfiguration(cSystemConfiguration);
			
			this.setcWorkerConfiguration(cWorkerConfiguration);
			
			cRes = this.initClassVariables();
			
			if ( !cRes )
			{
				cLogger.error("MappingHandler() is false!");
				
				this.setLcRes(cRes);
			}
			
    	}    		
    	catch( Exception e)
    	{
    		cLogger.error("MappingHandler:" + e.getStackTrace());
    		
    		this.setLcRes(cRes = false);
    	}
	}
	
	/*-------------------------------------------------------------------*/
	protected boolean initClassVariables()
	{
		boolean cRes = true;
		
		String  methodName = "";
	
		try
    	{
    		this.iReflection = new ToolkitReflection();
    		
    		methodName = this.iReflection.getMethodName();
    		
    		//-----------------
    		if ( cRes )
    		{
    			cRes = this.configureSpringExt();
    		}
    		//-----------------
    		if ( cRes )
    		{
    			this.cToolkitDataProvider = (ToolkitDataProvider)
    						this.applicationContext.getBean("toolkitDataProvider");
    			
    			if ( null == this.cToolkitDataProvider )
	    		{
    				cRes = false;
    				
	    			cLogger.error(methodName + "::cToolkitDataProvider is NULL!");
	    		}
    			else
    			{
    				cRes = this.cToolkitDataProvider.isLcRes();
    				
    				cLogger.info(methodName + "::cToolkitDataProvider status is " + cRes);
    			}
    		}	
    		//-----------------
    		if ( cRes )
    		{
    			List<Class<? extends Object>> clazzes = this.cToolkitDataProvider.
    					gettDatabase().getPersistanceClasses();
    			
    			this.cToolkitDataProvider.
    					gettDatabase().getHibernateSession(clazzes);
				
    			this.setLcRes(cRes = this.cToolkitDataProvider.
    					gettDatabase().isLcRes());
    		}
    		//-----------------
    		
    		return cRes;	 
    	}
		catch(  NoSuchBeanDefinitionException nbd )
		{
			cLogger.error(methodName + "::" + nbd.getMessage());
    		
    		this.setLcRes(cRes = false);
    		return cRes;
		}
		catch(  BeansException be )
		{
			cLogger.error(methodName + "::" + be.getMessage());
    		
    		this.setLcRes(cRes = false);
    		return cRes;
		}
    	catch( Exception e)
    	{
    		cLogger.error(methodName + "::" + e.getMessage());
    		
    		this.setLcRes(cRes = false);
    		return cRes;
    	}
	}
	/*-----------------------------------------------------------------------------------*/
	protected boolean configureSpring() 
	{
		boolean cRes = true;
		
		try 
		{
			
			FileSystemResource beansDefinition = new FileSystemResource(this.getcFrameworkBeansConfig());
			
			// create the Spring container
			this.cBeanFactory = new XmlBeanFactory(beansDefinition);

			// create a property configurator, that updates properties in the beans definition file with values
			// from a properties file. It replaces ${property name} with the value from the propeties file.
			Resource[] locations = new Resource[] { new FileSystemResource(this.getcFrameworkBeansProps()) };
			
			this.cSpringProps = new Properties();
			
			for (Resource resource : locations) 
			{
				try 
				{
					this.cSpringProps.load(resource.getInputStream());
				} 
				catch (IOException e) 
				{
					cLogger.error(e.getMessage());
					
					this.setLcRes(cRes = false);
					
					return cRes;
				}
			}
			
			
			// create the Spring container
			this.cBeanFactory = new XmlBeanFactory(beansDefinition);

			PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
			propertyPlaceholderConfigurer.setProperties(this.cSpringProps);
			propertyPlaceholderConfigurer.postProcessBeanFactory(this.cBeanFactory);
			
			this.cBeanFactory.getBean("toolkitDataProvider");
			this.cBeanFactory.getBean("toolkitJAXB");
			
			
			return cRes;

		} 
		catch (Exception e) 
		{
			cLogger.error("problem with configuration files!" + e.getMessage());
			
			this.setLcRes(cRes = false);
			
			return cRes;
		}
	}
	/*-----------------------------------------------------------------------------------*/
	protected boolean configureSpringExt() 
	{
		boolean cRes = true;
		
		try 
		{
			this.applicationContext = 
					new AnnotationConfigApplicationContext(ToolkitSpringConfig.class);
			
			//this.applicationContext.getBean("toolkitDataProvider");
			//this.applicationContext.getBean("toolkitJAXB");
			
			return cRes;

		} 
		catch (Exception e) 
		{
			cLogger.error("problem with configuration files!" + e.getMessage());
			
			this.setLcRes(cRes = false);
			
			return cRes;
		}
	}
	
	/*-----------------------------------------------------------------------------------*/
	/**
	 * @param cMappingFile
	 * @return File
	 */
	protected File getMappingFile(String cMappingFile) 
	{
		boolean cRes = true;
		
		String  methodName = "";
		
		try
    	{
    		this.iReflection = new ToolkitReflection();
    		
    		methodName = this.iReflection.getMethodName();
    		
    		File sfile = null;
    		
    		if ( (null == cMappingFile) || (cMappingFile.equals("")) )
    		{
    			cRes = false;
    			
    			cLogger.error(methodName + "::processRequest mappingFile path is wrong!");
    		}
    		
    		if ( cRes )
    		{
	    		sfile = new File(cMappingFile);
			
				if(!sfile.exists())
				{
					cLogger.error(methodName + "::processRequest mappingFile file not exists:" + cMappingFile);
					
					this.setLcRes(cRes = false);
				}
    		}
    		
			return sfile;	 
    	}
    	catch( Exception e)
    	{
    		cLogger.error(methodName + "::" + e.getMessage());
    		
    		this.setLcRes(cRes = false);
    		
    		return null;
    	}
	}
	
	/*-----------------------------------------------------------------------------------*/
	
	/**
	 * @param cMethodParams
	 * @param cMethodResults
	 * @param cSourceName
	 * @return
	 */
	
	@SuppressWarnings("unchecked")
	public Category getRootCategory(String cSourceName,
									String cCategoryBrowseNodeId)
	{
		boolean cRes = true;
		
		String  methodName = "";
		
		String sqlQuery = "";
		
		Session hbsSession = null;
		
		Transaction tx = null;
		
		List<Category> cCategories = new LinkedList<Category>();
		
		Category cCategory = new Category();
		
		try
    	{
    		this.iReflection = new ToolkitReflection();
    		
    		methodName = this.iReflection.getMethodName();
    		
    		if ( null == cSourceName )
    		{
    			cLogger.error(methodName + "::(null == cSourceName)");
    			
    			cRes = false;
    		}
    		
    		if ( null == cCategoryBrowseNodeId )
    		{
    			cLogger.error(methodName + "::(null == cCategoryBrowseNodeId)");
    			
    			cRes = false;
    		}
    		
    		//------
    		if ( cRes )
    		{
	    		if ( null == this.cToolkitDataProvider )
	    		{
	    			cLogger.error(methodName + "::cToolkitDataProvider is NULL for the Method:" + methodName);
	    			
	    			cRes = false;
	    		}
    		}
    		//------
    		if ( cRes )
    		{
    			sqlQuery = this.cToolkitDataProvider.gettSQL().getSqlQueryByFunctionName(methodName);
    			
    			if ( null == sqlQuery )
        		{
        			cLogger.error(methodName + "::sqlQuery is NULL for the Method:" + methodName);
        			
        			cRes = false;
        		}
    		}
    		//------
    		if ( cRes )
    		{
    			hbsSession = cToolkitDataProvider.gettDatabase().getHbsSessions().openSession();
    			
    			SQLQuery cQuery = hbsSession.createSQLQuery(sqlQuery);
    			cQuery.addEntity(Category.class);
    			
    			cQuery.setString("sourceName", cSourceName);
    			cQuery.setString("categoryBrowseNodeId", cCategoryBrowseNodeId);
    			
    			tx = hbsSession.beginTransaction();
    			
    			
				cCategories = (List<Category>)cQuery.list();
    		}
    		//------
    		
    		if ( cRes )
    		{
    			if ( cCategories != null && cCategories.size() >= 1 )
    			{
    				cCategory = cCategories.get(0);
    			}
    		}
    		
    		if ( tx != null )
			{
				tx.commit();
			}
    		
    		return cCategory;	 
    	}
    	catch( Exception e)
    	{
    		cLogger.error(methodName + "::" + e.getMessage());
    		
    		this.setLcRes(cRes = false);
    		
    		if ( tx != null )
    		{
    			tx.rollback();
    		}
    		
    		return new Category();
    	}
		finally
		{
			
			if ( hbsSession != null )
    		{
    			hbsSession.close();
    		}
		}
	}
	
	/*-----------------------------------------------------------------------------------*/
	
	/**
	 * @param cMethodParams
	 * @param cMethodResults
	 * @param cSourceName
	 * @return
	 */
	
	@SuppressWarnings("unchecked")
	public List<Category> getRootCategories(String cSourceName)
	{
		boolean cRes = true;
		
		String  methodName = "";
		
		String sqlQuery = "";
		
		Session hbsSession = null;
		
		Transaction tx = null;
		
		List<Category> cDepartments = new LinkedList<Category>();
		
		try
    	{
    		this.iReflection = new ToolkitReflection();
    		
    		methodName = this.iReflection.getMethodName();
    		
    		if ( null == cSourceName )
    		{
    			cLogger.error(methodName + "::(null == cSourceName)");
    			
    			cRes = false;
    		}
    		
    		//------
    		if ( cRes )
    		{
	    		if ( null == this.cToolkitDataProvider )
	    		{
	    			cLogger.error(methodName + "::cToolkitDataProvider is NULL for the Method:" + methodName);
	    			
	    			cRes = false;
	    		}
    		}
    		//------
    		if ( cRes )
    		{
    			sqlQuery = this.cToolkitDataProvider.gettSQL().getSqlQueryByFunctionName(methodName/*, repParams*/);
    			
    			if ( null == sqlQuery )
        		{
        			cLogger.error(methodName + "::sqlQuery is NULL for the Method:" + methodName);
        			
        			cRes = false;
        		}
    		}
    		//------
    		if ( cRes )
    		{
    			hbsSession = cToolkitDataProvider.gettDatabase().getHbsSessions().openSession();
    			
    			SQLQuery cQuery = hbsSession.createSQLQuery(sqlQuery);
    			cQuery.addEntity(Category.class);
    			
    			cQuery.setString("sourceName", cSourceName);
    			
    			tx = hbsSession.beginTransaction();
    			
    			
				cDepartments = (List<Category>)cQuery.list();
    		}
    		//------
    		
    		if ( tx != null )
			{
				tx.commit();
			}
    		
    		return cDepartments;	 
    	}
    	catch( Exception e)
    	{
    		cLogger.error(methodName + "::" + e.getMessage());
    		
    		this.setLcRes(cRes = false);
    		
    		if ( tx != null )
    		{
    			tx.rollback();
    		}
    		
    		return new LinkedList<Category>();
    	}
		finally
		{
			
			if ( hbsSession != null )
    		{
    			hbsSession.close();
    		}
		}
	}
	//---------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	public List<Node> getRootCategoriesNodes(String cSourceName)
	{
		boolean cRes = true;
		
		String  methodName = "";
		
		String sqlQuery = "";
		
		Session hbsSession = null;
		
		Transaction tx = null;
		
		List<Node> cNodes = new LinkedList<Node>();
		
		try
    	{
    		this.iReflection = new ToolkitReflection();
    		
    		methodName = this.iReflection.getMethodName();
    		
    		if ( null == cSourceName )
    		{
    			cLogger.error(methodName + "::(null == cSourceName)");
    			
    			cRes = false;
    		}
    		
    		//------
    		if ( cRes )
    		{
	    		if ( null == this.cToolkitDataProvider )
	    		{
	    			cLogger.error(methodName + "::cToolkitDataProvider is NULL for the Method:" + methodName);
	    			
	    			cRes = false;
	    		}
    		}
    		//------
    		if ( cRes )
    		{
    			sqlQuery = this.cToolkitDataProvider.gettSQL().getSqlQueryByFunctionName(methodName/*, repParams*/);
    			
    			if ( null == sqlQuery )
        		{
        			cLogger.error(methodName + "::sqlQuery is NULL for the Method:" + methodName);
        			
        			cRes = false;
        		}
    		}
    		//------
    		if ( cRes )
    		{
    			hbsSession = cToolkitDataProvider.gettDatabase().getHbsSessions().openSession();
    			
    			SQLQuery cQuery = hbsSession.createSQLQuery(sqlQuery);
    			cQuery.addEntity(Category.class);
    			
    			cQuery.setString("sourceName", cSourceName);
    			
    			tx = hbsSession.beginTransaction();
    			
    			
				cNodes = (List<Node>)cQuery.list();
    		}
    		//------
    		
    		if ( tx != null )
			{
				tx.commit();
			}
    		
    		return cNodes;	 
    	}
    	catch( Exception e)
    	{
    		cLogger.error(methodName + "::" + e.getMessage());
    		
    		this.setLcRes(cRes = false);
    		
    		if ( tx != null )
    		{
    			tx.rollback();
    		}
    		
    		return new LinkedList<Node>();
    	}
		finally
		{
			
			if ( hbsSession != null )
    		{
    			hbsSession.close();
    		}
		}
	}
	
	//---------------------------------------------------------------------
	/**
	 * @param cMethodParams
	 * @param cMethodResults
	 * @return
	 */
	
	@SuppressWarnings("unchecked")
	public boolean getRootCategories(Map<String, Object> cMethodParams,
			   						 Map<String, Object> cMethodResults)
	{
		boolean cRes = true;
		
		String  methodName = "";
		
		String sqlQuery = "";
		
		String cSourceName = "";
		
		Session hbsSession = null;
		
		Transaction tx = null;
		
		try
		{
			this.iReflection = new ToolkitReflection();
			
			methodName = this.iReflection.getMethodName();
					
			if ( null == cMethodParams )
			{
				cLogger.error(methodName + "::cMethodParams is null!");
				
				cRes = false;
			}
			
			if ( cRes )
			{
				cSourceName = (String)cMethodParams.get("p1");
			}
			
			if ( null == cSourceName )
			{
				cLogger.error(methodName + "::cSourceName is null!");
				
				cRes = false;
			}
			
			//------
			if ( cRes )
			{
	
				if ( null == this.cToolkitDataProvider )
	    		{
	    			cLogger.error(methodName + "::cToolkitDataProvider is NULL for the Method:" + methodName);
	    			
	    			cRes = false;
	    		}
			}
			//------
			if ( cRes )
			{
				sqlQuery = this.cToolkitDataProvider.gettSQL().getSqlQueryByFunctionName(methodName);
				
				if ( null == sqlQuery )
	    		{
	    			cLogger.error(methodName + "::sqlQuery is NULL for the Method:" + methodName);
	    			
	    			cRes = false;
	    		}
			}
			//------
			if ( cRes )
			{
				hbsSession = cToolkitDataProvider.gettDatabase().getHbsSessions().openSession();
				
				SQLQuery cQuery = hbsSession.createSQLQuery(sqlQuery);
				cQuery.addEntity(Category.class);
				
				cQuery.setString("sourceName", cSourceName);
				
				tx = hbsSession.beginTransaction();
				
				
				List<Category> cCategories = (List<Category>)cQuery.list();
				
				cMethodResults.put("r1", cCategories);
			}
			
			if ( tx != null )
			{
				tx.commit();
			}
			
			return cRes;	 
		}
		catch( Exception e)
		{
			cLogger.error(methodName + "::" + e.getMessage());
			
			this.setLcRes(cRes = false);
			
			if ( tx != null )
			{
				tx.rollback();
			}
			
			return cRes;
		}
		finally
		{
			
			if ( hbsSession != null )
			{
				hbsSession.close();
			}
		}
	}

	/**
	 * @param cMethodParams
	 * @param cMethodResults
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean getRootCategoriesNodes(Map<String, Object> cMethodParams,
			   						 	Map<String, Object> cMethodResults)
	{
		boolean cRes = true;
		
		String  methodName = "";
		
		String sqlQuery = "";
		
		String cSourceName = "";
		
		Session hbsSession = null;
		
		Transaction tx = null;
		
		try
    	{
    		this.iReflection = new ToolkitReflection();
    		
    		methodName = this.iReflection.getMethodName();
    				
    		if ( null == cMethodParams )
    		{
    			cLogger.error(methodName + "::cMethodParams is null!");
    			
    			cRes = false;
    		}
    		
    		if ( cRes )
    		{
    			cSourceName = (String)cMethodParams.get("p1");
    		}
    		
    		if ( null == cSourceName )
    		{
    			cLogger.error(methodName + "::cSourceName is null!");
    			
    			cRes = false;
    		}
    		
    		//------
    		if ( cRes )
    		{

    			if ( null == this.cToolkitDataProvider )
	    		{
	    			cLogger.error(methodName + "::cToolkitDataProvider is NULL for the Method:" + methodName);
	    			
	    			cRes = false;
	    		}
    		}
    		//------
    		if ( cRes )
    		{
    			sqlQuery = this.cToolkitDataProvider.gettSQL().getSqlQueryByFunctionName(methodName);
    			
    			if ( null == sqlQuery )
        		{
        			cLogger.error(methodName + "::sqlQuery is NULL for the Method:" + methodName);
        			
        			cRes = false;
        		}
    		}
    		//------
    		if ( cRes )
    		{
    			hbsSession = this.cToolkitDataProvider.gettDatabase().getHbsSessions().openSession();
    			
    			SQLQuery cQuery = hbsSession.createSQLQuery(sqlQuery);
    			
    			cQuery.addEntity(Node.class);
    			
    			cQuery.setString("sourceName", cSourceName);
    			
    			tx = hbsSession.beginTransaction();	
    			
    			List<Node> cNodes = (List<Node>)cQuery.list();
				
				cMethodResults.put("r1", cNodes);
    		}
    		
    		if ( tx != null )
			{
				tx.commit();
			}
    		
    		return cRes;	 
    	}
    	catch( Exception e)
    	{
    		cLogger.error(methodName + "::" + e.getMessage());
    		
    		this.setLcRes(cRes = false);
    		
    		if ( tx != null )
    		{
    			tx.rollback();
    		}
    		
    		return cRes;
    	}
		finally
		{
			
			if ( hbsSession != null )
    		{
    			hbsSession.close();
    		}
		}
	}
		
	/**
	 * @param cMethodParams
	 * @param cMethodResults
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean getRootCategorySubNodes(Map<String, Object> cMethodParams,
			   						 	     Map<String, Object> cMethodResults)
	{
		boolean cRes = true;
		
		String  methodName = "";
		
		String sqlQuery = "";
		
		String cSourceName = "";
		
		String cBrowseNode = "";
		
		Session hbsSession = null;
		
		Transaction tx = null;
		
		try
    	{
    		this.iReflection = new ToolkitReflection();
    		
    		methodName = this.iReflection.getMethodName();
    				
    		if ( null == cMethodParams )
    		{
    			cLogger.error(methodName + "::cMethodParams is null!");
    			
    			cRes = false;
    		}
    		//---
    		if ( cRes )
    		{
    			cSourceName = (String)cMethodParams.get("p1");
    		}
    		if ( null == cSourceName )
    		{
    			cLogger.error(methodName + "::cSourceName is null!");
    			
    			cRes = false;
    		}
    		//---
    		if ( cRes )
    		{
    			cBrowseNode = (String)cMethodParams.get("p2");
    		}
    		if ( null == cBrowseNode )
    		{
    			cLogger.error(methodName + "::cBrowseNode is null!");
    			
    			cRes = false;
    		}
    		//------
    		
    		//------
    		if ( cRes )
    		{

    			if ( null == this.cToolkitDataProvider )
	    		{
	    			cLogger.error(methodName + "::cToolkitDataProvider is NULL for the Method:" + methodName);
	    			
	    			cRes = false;
	    		}
    		}
    		//------
    		if ( cRes )
    		{
    			sqlQuery = this.cToolkitDataProvider.gettSQL().getSqlQueryByFunctionName(methodName);
    			
    			if ( null == sqlQuery )
        		{
        			cLogger.error(methodName + "::sqlQuery is NULL for the Method:" + methodName);
        			
        			cRes = false;
        		}
    		}
    		//------
    		if ( cRes )
    		{
    			hbsSession = this.cToolkitDataProvider.gettDatabase().getHbsSessions().openSession();
    			
    			SQLQuery cQuery = hbsSession.createSQLQuery(sqlQuery);
    			
    			cQuery.addEntity(Node.class);
    			
    			cQuery.setString("sourceName", cSourceName);
    			cQuery.setString("browseNode", cBrowseNode);
    			
    			tx = hbsSession.beginTransaction();	
    			
    			List<Node> cNodes = (List<Node>)cQuery.list();
				
				cMethodResults.put("r1", cNodes);
    		}
    		
    		if ( tx != null )
			{
				tx.commit();
			}
    		
    		return cRes;	 
    	}
    	catch( Exception e)
    	{
    		cLogger.error(methodName + "::" + e.getMessage());
    		
    		this.setLcRes(cRes = false);
    		
    		if ( tx != null )
    		{
    			tx.rollback();
    		}
    		
    		return cRes;
    	}
		finally
		{
			
			if ( hbsSession != null )
    		{
    			hbsSession.close();
    		}
		}
	}
	
	/*-----------------------------------------------------------------------------------*/
	@Override
	public boolean handleBrowseNodesHierarchy()
	{
		return true;
	}
	
	/*-----------------------------------------------------------------------------------*/
	@Override
	public boolean handleNodeLookup(Map<String, Object> cMethodParams, 
									Map<String, Object> cMethodResults) 
	{
		// TODO Auto-generated method stub
		return true;
	}
	/*-----------------------------------------------------------------------------------*/
	@Override
	public boolean handleCategoryNodeLookup(Map<String, Object> cMethodParams, 
									        Map<String, Object> cMethodResults) 
	{
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean handleItemSearchList(Map<String, Object> cMethodParams, 
										Map<String, Object> cMethodResults) 
	{
		// TODO Auto-generated method stub
		return true;
	}

	/*-----------------------------------------------------------------------------------*/
	@Override
	public boolean getNodeLookup(Map<String, Object> cMethodParams, 
								 Map<String, Object> cMethodResults) 
	{
		// TODO Auto-generated method stub
		return true;
	}
	/*-----------------------------------------------------------------------------------*/
	@Override
	public boolean getCategoryNodeLookup(Map<String, Object> cMethodParams, 
								         Map<String, Object> cMethodResults) 
	{
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean getItemSearchList(Map<String, Object> cMethodParams, 
									 Map<String, Object> cMethodResults) 
	{
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean saveCategoryNode(Map<String, Object> cMethodParams,
			    					Map<String, Object> cMethodResults)
	{
		// TODO Auto-generated method stub
		return true;
	}
	/*-----------------------------------------------------------------------------------*/
	@Override
	public boolean saveNodeLookup(Map<String, Object> cMethodParams, 
								  Map<String, Object> cMethodResults) 
	{
		// TODO Auto-generated method stub
		return true;
	}
	/*-----------------------------------------------------------------------------------*/
	@Override
	public boolean saveCategoryNodeLookup(Map<String, Object> cMethodParams, 
								          Map<String, Object> cMethodResults) 
	{
		// TODO Auto-generated method stub
		return true;
	}
}
