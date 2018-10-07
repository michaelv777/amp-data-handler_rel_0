/**
 * 
 */
package amp.data.handler.test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.junit.Test;

import amp.common.api.impl.ToolkitConstants;
import amp.data.handler.aws.DataHandlerAWS;
import amp.data.handler.base.DataHandlerBase;
import amp.data.handler.factory.DataHandlerFactory;
import amp.jpa.entities.Category;
import amp.jpa.entities.Node;
import junit.framework.TestCase;
/**
 * @author MVEKSLER
 *
 */
public class DataHandlerTest extends TestCase 
{
	protected long cSourceId = 101;
	
	protected DataHandlerAWS cDataHandler = null;
	/**
	 * @param name
	 */
	public DataHandlerTest(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link amp.data.handler.aws.DataHandlerAWS#MappingHandler()}.
	 */
//	@Ignore @Test
//	public void testMappingHandler() 
//	{
//		try
//		{
//			this.cDataHandler = new DataHandlerAWS();
//			
//			if ( !this.cDataHandler.isLcRes() )
//			{
//				fail("testMappingHandler failed build MappingHandler object!");
//			}
//		}
//		catch( Exception e)
//		{
//			fail("testToolkitDataProvider failed:" + e.getStackTrace());
//		}
//	}

	/**
	 * Test method for {@link amp.data.handler.aws.DataHandlerAWS#MappingHandler()}.
	 */
	@Test
	public void testHandleBrowseNodesHierarchy() 
	{
		boolean cRes = true;
		
		DataHandlerBase cOperationsBeanAWS = null;
		
		try
		{
			//-----------------------------------------
		    List<Category> cCategories = new LinkedList<Category>();
		   
		    List<Node> cBrowseNodesResults = new LinkedList<Node>();
		    
		    Queue<Node> cNodesQueue = new LinkedList<Node>();
		    
		    cOperationsBeanAWS = DataHandlerFactory.getDataHandler(DataHandlerAWS.class);
		    
		    if ( null == cOperationsBeanAWS )
    		{
		    	cRes = false;
		    	
		    	fail("this.cE2EDataHandlerBase id null  for E2EDataHandlerRM.class!!");
    		}
		    //---get Categories----------
		    cCategories = this.handleCategories( cOperationsBeanAWS );
		    
		    Thread.sleep(5000);
		    
		    //---get first level Nodes-----
		    cBrowseNodesResults = this.handleCategoriesNodes(cOperationsBeanAWS, cCategories);
		    
		    for( Node cBrowseNode : cBrowseNodesResults)
        	{
	    		cNodesQueue.add(cBrowseNode);
        	}
		    
		    Thread.sleep(5000);
		    //---run BFS to get all level Nodes-----
		    if ( cRes )
		    {
		    	while( !cNodesQueue.isEmpty() )
		    	{
		    		Thread.sleep(1000);
		    		
		    		Node cBrowseNode = cNodesQueue.remove();
		    		
		    		System.out.println("Queue size=" + cNodesQueue.size());
		    		
		    		cBrowseNodesResults = this.handleNode(cOperationsBeanAWS, cBrowseNode);
		    		
		    		for( Node cBrowseNodeNext : cBrowseNodesResults)
			    	{
			    		cNodesQueue.add(cBrowseNodeNext);
			    	}
			        //--------
		    	}
		    }
		}
		catch( Exception e)
		{
			fail("testToolkitDataProvider failed:" + e.getStackTrace());
		}
	}

	/**
	 * @param cOperationsBeanAWS
	 * @param cMethodParams
	 * @param cMethodResults
	 * @param cNodesQueue
	 * @param cLevel
	 * @param cType
	 * @param cBrowseNode
	 */
	@SuppressWarnings("unchecked")
	protected List<Node> handleNode(DataHandlerBase cOperationsBeanAWS, Node cBrowseNode) 
	{
		boolean cRes = true;
		
		List<Node> cBrowseNodesResults = new LinkedList<Node>();
	
		try
		{
			//-----------------------------------------
			Map<String, Object> cMethodParams = new HashMap<String, Object>();
		    Map<String, Object> cMethodResults = new HashMap<String, Object>();
		    
		    
			Long cLevel = new Long(3L);
	    	Long cType = new Long(ToolkitConstants.AMP_CLASSIFICATION_NODE_ID);
	    	
			HashMap<String, String> cParams = new HashMap<String, String>();
			cParams.put(ToolkitConstants.BROWSE_NODE_ID_PARAM, cBrowseNode.getBrowsenodeid());
			
			cMethodParams.put("p1", cParams);
			cMethodParams.put("p2", cLevel);
			cMethodParams.put("p3", cType);
			cMethodParams.put("p4", cBrowseNode);
			
			cRes = cOperationsBeanAWS.handleNodeLookup(cMethodParams, cMethodResults);
			
			if ( !cRes )
			{
				//fail("testMappingHandler failed build MappingHandler object!");
			}
			
			//--------
			if ( cRes )
			{
				cBrowseNodesResults = (LinkedList<Node>)cMethodResults.get("r1");
				
				if ( null == cBrowseNodesResults )
			    {
			    	cRes = false;
			    	
			    	fail("cBrowseNodesResults is null!");
			    }
			}
			
			return cBrowseNodesResults;
		}
		catch( Exception e)
		{
			fail("testToolkitDataProvider failed:" + e.getStackTrace());
			
			return new LinkedList<Node>();
		}
	}

	/**
	 * @param cRes
	 * @param cOperationsBeanAWS
	 * @param cMethodParams
	 * @param cMethodResults
	 * @param cCategories
	 * @param cNodesQueue
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected List<Node> handleCategoriesNodes(
			DataHandlerBase cOperationsBeanAWS,
			List<Category> cCategories) 
	{
		boolean cRes = true;
		
		List<Node> cBrowseNodesResults = new LinkedList<Node>();
		
		try
		{
			//-----------------------------------------
			Map<String, Object> cMethodParams = new HashMap<String, Object>();
		    Map<String, Object> cMethodResults = new HashMap<String, Object>();
		    
			if ( cRes )
			{
				for( Category cCategory : cCategories )
				{
				    HashMap<String, String> cParams = new HashMap<String, String>();
				    cParams.put(ToolkitConstants.BROWSE_NODE_ID_PARAM, cCategory.getRootbrowsenode());
				    
				    Long cLevel = new Long(2L);
				    Long cType = new Long(ToolkitConstants.AMP_CATEGORY_NODE_ID);
				    
				    cMethodParams.put("p1", cParams);
				    cMethodParams.put("p2", cLevel);
				    cMethodParams.put("p3", cType);
				    cMethodParams.put("p4", cCategory);
				    
					cRes = cOperationsBeanAWS.handleCategoryNodeLookup(cMethodParams, cMethodResults);
					
					if ( !cRes )
					{
						fail("testMappingHandler failed build MappingHandler object!");
					}
					else
					{
						cBrowseNodesResults.addAll((LinkedList<Node>)cMethodResults.get("r1"));
					}
				}
			}
			return cBrowseNodesResults;
		}
		catch( Exception e)
		{
			fail("testToolkitDataProvider failed:" + e.getStackTrace());
			
			return new LinkedList<Node>();
		}
	}
	
	@SuppressWarnings("unchecked")
	protected List<Category> handleCategories(DataHandlerBase cOperationsBeanAWS )
	{
		boolean cRes = true;
		
		List<Category> cCategories = new LinkedList<Category>();
		
		try
		{
			//-----------------------------------------
			Map<String, Object> cMethodParams = new HashMap<String, Object>();
		    Map<String, Object> cMethodResults = new HashMap<String, Object>();
		    
			if ( cRes )
		    {
		    	cMethodParams.put("p1", "AmazonUS");
		    	
				cRes = cOperationsBeanAWS.getRootCategories(cMethodParams, cMethodResults);
				
				if ( !cRes )
				{
					fail("testMappingHandler failed build MappingHandler object!");
				}
				
				if ( cRes )
		        {
		        	cCategories =  (List<Category>)cMethodResults.get("r1");
		        	
		        	if ( null == cCategories )
		        	{
		        		fail("cCategories list is null!");
		        	}
		        	
		        	//---save categories to node table ( use FK from child to parent node ) 
				    for( Category cCategory : cCategories )
				    {
				    	cMethodParams.put("p1", cCategory);
				    	
				    	cOperationsBeanAWS.handleCategoryNode(cMethodParams, cMethodResults);
				    }
		        }
		    }
			
			return cCategories;
		}
		catch( Exception e)
		{
			fail("testToolkitDataProvider failed:" + e.getStackTrace());
			
			return new LinkedList<Category>();
		}
	}
	
//	//------------------------------------------------------------------------------------
//	/**
//	 * Test method for {@link amp.data.handler.aws.DataHandlerAWS#MappingHandler()}.
//	 */
//	@Ignore @Test
//	public void testHandleCategoryNodeLookup() 
//	{
//		boolean cRes = true;
//		
//		try
//		{
//			Map<String, Object> cMethodParams = new HashMap<String, Object>();
//		    Map<String, Object> cMethodResults = new HashMap<String, Object>();
//		       	 
//		    HashMap<String, String> params = new HashMap<String, String>();
//		    params.put(ToolkitConstants.BROWSE_NODE_ID_PARAM, "677211011");
//		    
//		    cMethodParams.put("p1", params);
//		    
//		    DataHandlerBase cOperationsBeanAWS = 
//		    		DataHandlerFactory.getDataHandler(DataHandlerAWS.class);
//    		
//    		if ( null == cOperationsBeanAWS )
//    		{
//    			System.out.println(
//    					"this.cE2EDataHandlerBase id null  for E2EDataHandlerRM.class!!");
//    			
//    			fail("cDataHandlerBase is null!");
//    		}
//    		
//			cRes = cOperationsBeanAWS.handleNodeLookup(cMethodParams, cMethodResults);
//			
//			if ( !cRes )
//			{
//				fail("testMappingHandler failed build MappingHandler object!");
//			}
//		}
//		catch( Exception e)
//		{
//			fail("testToolkitDataProvider failed:" + e.getStackTrace());
//		}
//	}
}
