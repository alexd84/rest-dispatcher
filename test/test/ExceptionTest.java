package test;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import restdisp.urltree.LookupTree;
import restdisp.urltree.Node;
import restdisp.urltree.UrlDescriptor;
import restdisp.urltree.UrlTreeBuilder;
import restdisp.validation.HandlerException;
import restdisp.validation.RoutingException;
import restdisp.validation.ConfigurationException;
import restdisp.worker.TreeExecutor;
import test.mock.DispatcherServletMock;
import test.mock.HttpServletRequestMock;
import test.mock.HttpServletResponseMock;
import static org.junit.Assert.*; 

import org.junit.Test;

public class ExceptionTest {
	private InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("test/conf/router.conf");
	private Node root = new UrlTreeBuilder(null).buildUrlTree(is);
	public ExceptionTest() throws Exception {}
	
	@Test
	public void testHandlerException() throws ConfigurationException, IOException, RoutingException, restdisp.validation.HandlerException {
		try {
			UrlDescriptor res = LookupTree.getPath(root, "get", "/svc/exc");
			new TreeExecutor().exec(res, null, null, null);
			assertTrue(false);
		} catch (HandlerException e) {
			assertTrue(e.getMessage().contains("Handler invocation exception [test.actors.Action:getException]. Variables count [0]."));
		}
	}
	
	@Test
	@SuppressWarnings("unused")
	public void testRoutingException() throws ConfigurationException, IOException, RoutingException, restdisp.validation.HandlerException {
		try {
			UrlDescriptor res = LookupTree.getPath(root, "get", "/svc/exc/tst");
			assertTrue(false);
		} catch (RoutingException e) {
			assertTrue(e.getMessage().contains("Path not defined [/get/svc/exc/tst]"));
		}
	}
	
	@Test
	@SuppressWarnings("unused")
	public void testConfigurationExceptionBranch() throws ConfigurationException, IOException {
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("test/conf/router.exception.branch.conf");
			Node root = new UrlTreeBuilder(null).buildUrlTree(is);
			assertTrue(false);
		} catch (ConfigurationException e) {
			Throwable inner = e.getCause();
			assertTrue(e.getMessage().contains("Failed to add branch"));
			assertTrue(inner.getMessage().contains("Method not found [class test.actors.Action:getExceptionCase]. Variables count [0]."));
		}
	}
	
	@Test
	@SuppressWarnings("unused")
	public void testConfigurationExceptionMethod() throws ConfigurationException, IOException {
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("test/conf/router.exception.method.conf");
			Node root = new UrlTreeBuilder(null).buildUrlTree(is);
			assertTrue(false);
		} catch (ConfigurationException e) {
			Throwable inner = e.getCause();
			assertTrue(e.getMessage().contains("Failed to add branch"));
			assertTrue(inner.getMessage().contains("Method not found [class test.actors.Action:getException]. Variables count [1]."));
		}
	}
	
	@Test
	@SuppressWarnings("unused")
	public void testConfigurationExceptionGenMethod() throws ConfigurationException, IOException {
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("test/conf/router.exception.genericmethod.conf");
			Node root = new UrlTreeBuilder(null).buildUrlTree(is);
			assertTrue(false);
		} catch (ConfigurationException e) {
			Throwable inner = e.getCause();
			assertTrue(e.getMessage().contains("Failed to add branch"));
			assertTrue(inner.getMessage().contains("Failed to build leaf. Default constructor not found [test.actors.ActionMethodErr]."));
		}
	}
	
	@Test
	@SuppressWarnings("unused")
	public void testConfigurationGenClassException() throws ConfigurationException, IOException {
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("test/conf/router.exception.genericclass.conf");
			Node root = new UrlTreeBuilder(null).buildUrlTree(is);
			assertTrue(false);
		} catch (ConfigurationException e) {
			Throwable inner = e.getCause();
			assertTrue(e.getMessage().contains("Failed to add branch"));
			assertTrue(inner.getMessage().contains("Failed to build leaf. Class not found [test.actors.ActionErr]."));
		}
	}
	
	@Test
	@SuppressWarnings("unused")
	public void testMethodValiationException() throws ConfigurationException, IOException {
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("test/conf/router.exception.methodvalidation.conf");
			Node root = new UrlTreeBuilder(null).buildUrlTree(is);
			assertTrue(false);
		} catch (ConfigurationException e) {
			Throwable inner = e.getCause();
			assertTrue(e.getMessage().contains("Failed to add branch"));
			assertTrue(inner.getMessage().contains("Wrong method [gets]"));
		}
	}
	
	@Test
	@SuppressWarnings("unused")
	public void testUrlValValiationException() throws ConfigurationException, IOException {
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("test/conf/router.exception.urlvalvalidation.conf");
			Node root = new UrlTreeBuilder(null).buildUrlTree(is);
			assertTrue(false);
		} catch (ConfigurationException e) {
			Throwable inner = e.getCause();
			Throwable inner2 = inner.getCause();
			assertTrue(e.getMessage().contains("Failed to add branch"));
			assertTrue(inner.getMessage().contains("Wrong configuration entry [/svc/exc/{id]"));
			assertTrue(inner2.getMessage().contains("Wrong value [{id]"));
		}
	}
	
	@Test
	public void testAbstractWorkerException() throws ConfigurationException, IOException, RoutingException, HandlerException {
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("test/conf/router.exception.abstractworkerexc.conf");
			Node root = new UrlTreeBuilder(null).buildUrlTree(is);
			UrlDescriptor res = LookupTree.getPath(root, "get", "/svc/exc/1");
			new TreeExecutor().exec(res, null, null, null);
			assertTrue(false);
		} catch (ConfigurationException e) {
			assertTrue(e.getMessage().contains("Failed to add branch [get /svc/exc/{id} test.actors.UsrAbstractWorker:exec]"));
		}
	}
	
	@Test
	public void testAbstractConstructorException() throws ConfigurationException, IOException, RoutingException, HandlerException {
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("test/conf/router.exception.consworkerexc.conf");
			Node root = new UrlTreeBuilder(null).buildUrlTree(is);
			UrlDescriptor res = LookupTree.getPath(root, "get", "/svc/exc/1");
			new TreeExecutor().exec(res, null, null, null);
			assertTrue(false);
		} catch (ConfigurationException e) {
			assertTrue(e.getMessage().contains("Failed to add branch [get /svc/exc/{id} test.actors.ConstructorException:exec]"));
		}
	}
	
	@Test
	public void testWrongArgsException() throws ConfigurationException, IOException, RoutingException, HandlerException {
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("test/conf/router.exception.wrongargs.conf");
			@SuppressWarnings("unused")
			Node root = new UrlTreeBuilder(null).buildUrlTree(is);
			assertTrue(false);
		} catch (ConfigurationException e) {
			Throwable inner = e.getCause();
			Throwable inner2 = inner.getCause();
			assertTrue(e.getMessage().contains("Failed to add branch [get /svc/exc/{id} test.actors.WrongArgs:test]"));
			assertTrue(inner.getMessage().contains("Class method has unsupported argument [test.actors.WrongArgs.test]"));
			assertTrue(inner2.getMessage().contains("Unsupported argument type [java.util.Arrays]"));
		}
	}
	
	@Test
	public void testCastException() throws ConfigurationException, IOException, RoutingException, restdisp.validation.HandlerException {
		try {
			UrlDescriptor res = LookupTree.getPath(root, "get", "/svc/act/1a");
			new TreeExecutor().exec(res, null, null, null);
			assertTrue(false);
		} catch (Exception e) {
			Throwable inner = e.getCause();
			Throwable inner2 = inner.getCause();
			assertTrue(e.getMessage().contains("Failed to call method: [test.actors.Action:getUser()]"));
			assertTrue(inner.getMessage().contains("Failed to cast variable for method call: ['1a' => int]"));
			assertTrue(inner2.getMessage().contains("For input string: \"1a\""));
		}
	}
	
	@Test
	public void testCastCharacterException() throws ConfigurationException, IOException, RoutingException, restdisp.validation.HandlerException {
		try {
			UrlDescriptor res = LookupTree.getPath(root, "get", "/svc/act/true/true/1/1/256/256/c/ss");
			new TreeExecutor().exec(res, null, null, null);
			assertTrue(false);
		} catch (Exception e) {
			Throwable inner = e.getCause();
			Throwable inner2 = inner.getCause();
			assertTrue(e.getMessage().contains("Failed to call method: [test.actors.Action:getShortTypes()]"));
			assertTrue(inner.getMessage().contains("Failed to cast variable for method call: ['ss' => class java.lang.Character]"));
			assertTrue(inner2.getMessage().contains("Failed to cast String to Character [ss]"));
		}
	}
	
	@Test
	public void testWrongWorkerMethodException() throws ConfigurationException, IOException, RoutingException, restdisp.validation.HandlerException {
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("test/conf/router.exception.wrongworkermethod.conf");
			@SuppressWarnings("unused")
			Node root = new UrlTreeBuilder(null).buildUrlTree(is);
			assertTrue(false);
		} catch (ConfigurationException e) {
			ConfigurationException inner = (ConfigurationException) e.getCause();
			assertTrue(e.getMessage().contains("Failed to add branch [get /svc/act test.actors.Action]"));
			assertTrue(inner.getMessage().contains("Wrong class method entry [get /svc/act test.actors.Action]"));
		}
	}
	
	@Test
	public void testDispatcherNoMethodError() throws IOException, ServletException {
		HttpServletResponseMock resp = new HttpServletResponseMock();
		DispatcherServletMock disp = new DispatcherServletMock("/web", "test/conf/router.disp.conf");
		HttpServletRequest req = HttpServletRequestMock.buildHttpServletRequest("get","/web/svc/actERROR/123");
		
		disp.init();
		disp.service(req, resp);
		assertTrue(resp.getErrorCode() == HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		assertEquals(resp.getError(), "Method not found [get] [/svc/actERROR/123]");
	}
	
	@Test
	public void testDispatcherExecError() throws IOException, ServletException {
		HttpServletResponseMock resp = new HttpServletResponseMock();
		DispatcherServletMock disp = new DispatcherServletMock("/web", "test/conf/router.disp.conf");
		HttpServletRequest req = HttpServletRequestMock.buildHttpServletRequest("get","/web/svc/act");
		
		disp.init();
		disp.service(req, resp);
		assertTrue(resp.getErrorCode() == HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		assertTrue(resp.getError().contains("Handler invocation exception [test.actors.ActionError:exec]"));
		assertTrue(resp.getError().contains("/ by zero"));
	}
	
	@Test
	public void testDispatcherNoConfigError() throws IOException, ServletException {
		try {
			DispatcherServletMock disp = new DispatcherServletMock("/web", "test/conf/noconfig.conf");
			disp.init();
		} catch (ServletException e) {
			assertTrue(e.getMessage().contains("Configuration not found [test/conf/noconfig.conf]"));
		}
	}
	
	@Test
	public void testDispatcherTreeBuildError() throws IOException, ServletException {
		try {
			DispatcherServletMock disp = new DispatcherServletMock("/web", "test/conf/router.exception.urlvalvalidation.conf");
			disp.init();
		} catch (ServletException e) {
			ConfigurationException inner = (ConfigurationException) e.getCause();
			ConfigurationException inner2 = (ConfigurationException) inner.getCause();
			ConfigurationException inner3 = (ConfigurationException) inner2.getCause();
			assertTrue(e.getMessage().contains("Wrong configuration"));
			assertTrue(inner.getMessage().contains("Failed to add branch [get /svc/exc/{id Dummy:dummy]"));
			assertTrue(inner2.getMessage().contains("Wrong configuration entry [/svc/exc/{id]"));
			assertTrue(inner3.getMessage().contains("Wrong value [{id]"));
		}
	}
	
	@Test
	public void testWrongConfPathException() throws ConfigurationException, IOException, RoutingException, restdisp.validation.HandlerException {
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("test/conf/router.conf");
			is.close();
			@SuppressWarnings("unused")
			Node root = new UrlTreeBuilder(null).buildUrlTree(is);
			assertTrue(false);
		} catch (ConfigurationException e) {
			assertTrue(e.getMessage().contains("Could not read stream"));
		}
	}
}
